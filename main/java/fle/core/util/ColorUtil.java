package fle.core.util;

import flapi.util.ColorMap;
import flapi.util.FleValue;
import fle.FLE;
import fle.core.energy.FleThermalNet;

public class ColorUtil
{
	private static final ColorMap t_c_map = FLE.fle.getColorMapHandler().registerColorMap(FleValue.TEXTURE_FILE + ":textures/colormap/tw.png");
	
	public static int getColorWithTd(double t)
	{
		return getColorWithTdWd(t, 0);
	}
	public static int getColorWithTi(int t)
	{
		return getColorWithTiWd(t, 0);
	}

	public static int getColorWithTdWd(double t, double w)
	{
		return getColorWithTiWd(FleThermalNet.getFTempretureToInteger(t), w);
	}
	public static int getColorWithTiWd(int t, double w)
	{
		if(t < FleValue.WATER_FREEZE_POINT) return 0x000000;
		int i = 256 - (int) (Math.log10(1 + t - FleValue.WATER_FREEZE_POINT) * 32D);
		int j = (int) (Math.log(w * 256 + 1) * w * 256D);
		if(i < 0) i = 0;
		if(j > 255) j = 255;
		return t_c_map.getColorFromCrood(j, i);
	}
	
	public static int getColorWithProgress(double progress)
	{
		
		//Rought method.
		return 0xFFFFFF;
	}
}