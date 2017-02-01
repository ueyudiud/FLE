package nebula.client.render;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import nebula.Log;
import net.minecraft.client.renderer.texture.PngSizeInfo;
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
	private static final int[] MISSING_MAPPING = {0xFFFFFF};
	
	public static Colormap getColormap(String location)
	{
		Colormap colormap = ColormapFactory.COLORMAPS.get(location);
		return colormap != null ? colormap : new Colormap(location);
	}

	private String location;
	private int width;
	private int height;
	private int[] colors;

	private Colormap(String location)
	{
		this.location = location;
		ColormapFactory.COLORMAPS.put(location, this);
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getColor(float u, float v)
	{
		if(u < 0F || u > 1F || v < 0F || v > 1F) return 0xFFFFFF;
		return colors[(int) (width * u) + (int) (height * v) * width];
	}

	@SideOnly(Side.CLIENT)
	public static enum ColormapFactory implements IResourceManagerReloadListener
	{
		INSTANCE;

		private static final Map<String, Colormap> COLORMAPS = new HashMap();

		@Override
		public void onResourceManagerReload(IResourceManager resourceManager)
		{
			Log.reset();
			ProgressBar bar = ProgressManager.push("Far Color Map Reloading", COLORMAPS.size());
			ResourceLocation location;
			for(Colormap colormap : COLORMAPS.values())
			{
				location = new ResourceLocation(colormap.location);
				bar.step(location.toString());
				try
				{
					IResource resource = resourceManager.getResource(location);
					PngSizeInfo info = PngSizeInfo.makeFromResource(resource);
					colormap.height = info.pngHeight;
					colormap.width = info.pngWidth;
					colormap.colors = TextureUtil.readImageData(resourceManager, location);
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