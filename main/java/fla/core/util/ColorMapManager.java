package fla.core.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.util.ResourceLocation;
import fla.api.util.IColorMap;
import fla.api.util.IColorMapManager;

public class ColorMapManager implements IColorMapManager
{
	@Override
	public IColorMap getColorMap(ResourceLocation locate) 
	{
		String str = ColorMap.class.getResource("/").getPath() + "assets/" + locate.getResourceDomain() + "/textures/colormap/" + locate.getResourcePath() + ".png";
		File file = new File(str);

		BufferedImage buf = null;
		try 
		{
			buf = ImageIO.read(file);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return new ColorMap(buf);
	}
}
