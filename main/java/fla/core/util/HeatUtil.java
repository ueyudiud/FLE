package fla.core.util;

import fla.api.util.FlaValue;

public class HeatUtil 
{
	public static int getFTempretureToInteger(double tempreture)
	{
		return (int) (Math.pow(tempreture, 0.5D) / Math.pow(40D, 0.5D) * 40) + FlaValue.Water_Freeze_Tempreture;
	}
}
