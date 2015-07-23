package fla.core.util;

import java.io.IOException;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.util.FlaValue;
import fla.api.util.IColorMap;
import fla.api.util.IColorMapManager;

@SideOnly(Side.CLIENT)
public class ColorMapManager implements IColorMapManager
{
	public static IColorMap t_c_map;
	
	public ColorMapManager() 
	{
		;
	}
	
	@Override
	public IColorMap getColorMap(IResourceManager manager, ResourceLocation locate) 
	{
		try 
		{
			return new ColorMap(TextureUtil.readImageData(manager, locate));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return new ColorMap();
		}
	}

	@Override
	public void onResourceManagerReload(IResourceManager manager) 
	{
		t_c_map = getColorMap(manager, new ResourceLocation(FlaValue.TEXT_FILE_NAME, "textures/colormap/t_w.png"));
	}
}