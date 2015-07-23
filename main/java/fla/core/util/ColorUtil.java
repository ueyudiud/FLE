package fla.core.util;

import fla.api.util.FlaValue;

public class ColorUtil 
{
	public static int getColorWithTd(double t)
	{
		return getColorWithTi((int) (Math.pow(t, 0.5D) * 63) + FlaValue.Water_Freeze_Tempreture);
	}
	public static int getColorWithTi(int t)
	{
		if(t < FlaValue.Water_Freeze_Tempreture) return 0x000000;
		int i = (int) (Math.log10((double) t / 100D) * 256D / 2.5D);
		return ColorMapManager.t_c_map.getColor(i, 0);
	}

	public static int getColorWithTdWd(double t, double w)
	{
		return getColorWithTiWd(HeatUtil.getFTempretureToInteger(t), w);
	}
	public static int getColorWithTiWd(int t, double w)
	{
		if(t < FlaValue.Water_Freeze_Tempreture) return 0x000000;
		int i = (int) (Math.log10((double) t / 100D) * 256D / 2.5D);
		return ColorMapManager.t_c_map.getColor(i, (int) (w * 256));
	}
}
