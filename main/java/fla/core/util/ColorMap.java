package fla.core.util;

import java.awt.image.BufferedImage;

import fla.api.util.IColorMap;

public class ColorMap implements IColorMap
{
	private BufferedImage img;
	
	public ColorMap(BufferedImage img)
	{
		this.img = img;
	}
	
	public int getColor(int x, int y)
	{
		if(img == null)
		{
			return 0xFFFFFF;
		}
		return img.getRGB(x, y) & 0xFFFFFF;
	}

	@Override
	public int getWidth() 
	{
		if(img == null)
		{
			return 256;
		}
		return img.getWidth();
	}

	@Override
	public int getHeight() 
	{
		if(img == null)
		{
			return 256;
		}
		return img.getHeight();
	}
}
