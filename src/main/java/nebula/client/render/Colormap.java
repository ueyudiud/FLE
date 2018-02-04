/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client.render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nebula.Log;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Colormap
{
	private static final int[] MISSING_MAPPING = { 0xFFFFFF };
	
	public static Colormap getColormap(String location)
	{
		return ColormapFactory.COLORMAPS.computeIfAbsent(new ResourceLocation(location), Colormap::new);
	}
	
	private String	location;
	private int		width;
	private int		height;
	private int[]	colors;
	
	private Colormap(ResourceLocation location)
	{
		this.location = location.toString();
	}
	
	public ResourceLocation getLocation()
	{
		return new ResourceLocation(this.location);
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public int getColor(float u, float v)
	{
		if (u < 0F || u > 1F || v < 0F || v > 1F) return 0xFFFFFF;
		return this.colors[(int) (this.width * u) + (int) (this.height * v) * this.width];
	}
	
	@SideOnly(Side.CLIENT)
	public static enum ColormapFactory implements IResourceManagerReloadListener
	{
		INSTANCE;
		
		private static final Map<ResourceLocation, Colormap> COLORMAPS = new HashMap();
		
		@Override
		public void onResourceManagerReload(IResourceManager resourceManager)
		{
			Log.reset();
			ProgressBar bar = ProgressManager.push("Far Color Map Reloading", COLORMAPS.size());
			for (Entry<ResourceLocation, Colormap> entry : COLORMAPS.entrySet())
			{
				Colormap colormap = entry.getValue();
				bar.step(entry.getKey().toString());
				try
				{
					IResource resource = resourceManager.getResource(entry.getKey());
					BufferedImage image = TextureUtil.readBufferedImage(resource.getInputStream());
					colormap.height = image.getHeight();
					colormap.width = image.getWidth();
					colormap.colors = image.getRGB(0, 0, colormap.width, colormap.height, null, 0, colormap.width);
				}
				catch (IOException | RuntimeException exception)
				{
					Log.cache(exception);
					colormap.height = 1;
					colormap.width = 1;
					colormap.colors = MISSING_MAPPING;
				}
			}
			ProgressManager.pop(bar);
			Log.logCachedExceptions();
		}
	}
}
