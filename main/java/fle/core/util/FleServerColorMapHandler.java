package fle.core.util;

import farcore.util.ColorMap;
import farcore.util.IColorMapHandler;
import farcore.util.IColorMapProvider;

public class FleServerColorMapHandler implements IColorMapHandler
{
	public static final FleServerColorMapHandler instance = new FleServerColorMapHandler();
	
	private FleServerColorMapHandler()
	{
	}
	
	@Override
	public ColorMap registerColorMap(String name)
	{
		return new FleColorMap();
	}
	
	@Override
	public void addColorMapProvider(IColorMapProvider provider)
	{
		;
	}
}