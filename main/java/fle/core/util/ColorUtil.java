package fle.core.util;

import fle.FLE;
import fle.api.FleValue;
import fle.api.util.ColorMap;
import fle.core.energy.FleThermalNet;

public class ColorUtil
{
	private static final ColorMap t_c_map = FLE.fle.getColorMapHandler().registerColorMap(FleValue.TEXTURE_FILE + ":textures/colormap/t_w.png");
	
	public static int getColorWithTd(double t)
	{
		return getColorWithTi((int) (Math.pow(t, 0.5D) * 63) + FleValue.WATER_FREEZE_POINT);
	}
	public static int getColorWithTi(int t)
	{
		if(t < FleValue.WATER_FREEZE_POINT) return 0x000000;
		int i = (int) (Math.log10((double) t / 100D) * 256D / 2.5D);
		return t_c_map.getColorFromCrood(i, 0);
	}

	public static int getColorWithTdWd(double t, double w)
	{
		return getColorWithTiWd(FleThermalNet.getFTempretureToInteger(t), w);
	}
	public static int getColorWithTiWd(int t, double w)
	{
		if(t < FleValue.WATER_FREEZE_POINT) return 0x000000;
		int i = (int) (Math.log10((double) t / 100D) * 256D / 2.5D);
		return t_c_map.getColorFromCrood(i, (int) (w * 256));
	}
}