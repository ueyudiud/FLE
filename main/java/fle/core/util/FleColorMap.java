package fle.core.util;

import flapi.util.ColorMap;

public class FleColorMap implements ColorMap
{
	private String locate;
	private int[] colors;

	public FleColorMap() 
	{
		colors = null;
	}
	public FleColorMap(String locate) 
	{
		this.locate = locate;
	}
	
	public String getLocate()
	{
		return locate;
	}
	
	public void setColors(int[] colors)
	{
		if(colors.length != 256) 
			throw new IllegalArgumentException("Color map size must be 256x256!");
		this.colors = colors;
	}
	
	@Override
	public int getColorFromCrood(int x, int y) 
	{
		return colors == null ? 0xFFFFFF : colors[(y << 8) + x];
	}

	@Override
	public int getGrayLevelFromCrood(int x, int y) 
	{
		int tColor = getColorFromCrood(x, y);
		int aRed = (tColor >> 16) & 255;
		int aGreen = (tColor >> 8) & 255;
		int aBlue = tColor & 255;
		return (aRed * 2 + aGreen * 5 + aBlue * 3) / 10;
	}

	@Override
	public int getRedLevelFromCrood(int x, int y) 
	{
		int tColor = getColorFromCrood(x, y);
		int aRed = (tColor >> 16) & 255;
		return aRed;
	}

	@Override
	public int getGreenLevelFromCrood(int x, int y) 
	{
		int tColor = getColorFromCrood(x, y);
		int aGreen = (tColor >> 8) & 255;
		return aGreen;
	}

	@Override
	public int getBlueLevelFromCrood(int x, int y) 
	{
		int tColor = getColorFromCrood(x, y);
		int aBlue = tColor & 255;
		return aBlue;
	}
}