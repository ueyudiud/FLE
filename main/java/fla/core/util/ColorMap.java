package fla.core.util;

import fla.api.util.IColorMap;

public class ColorMap implements IColorMap
{
	private int[] rgb;

	public ColorMap()
	{
		
	}
	public ColorMap(int[] img)
	{
		this.rgb = img;
	}
	
	public int getColor(int x, int y)
	{
		if(rgb == null)
		{
			return 0xFFFFFF;
		}
		return rgb[x * 256 + y];
	}

	@Override
	public int getWidth() 
	{
		return 256;
	}

	@Override
	public int getHeight() 
	{
		return 256;
	}
}