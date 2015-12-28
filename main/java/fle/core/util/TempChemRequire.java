package fle.core.util;

import flapi.chem.base.IChemCondition;
import flapi.chem.base.IChemistryRequire;

public class TempChemRequire implements IChemistryRequire
{
	int s;
	int m;

	public TempChemRequire(int temp)
	{
		this(temp, temp);
	}
	public TempChemRequire(int startTemp, int maxTemp)
	{
		s = startTemp;
		m = maxTemp;
	}
	
	@Override
	public boolean match(IChemCondition condition)
	{
		return condition.getTemperature() >= s;
	}

	@Override
	public float speed(IChemCondition condition)
	{
		float i = (float) (condition.getTemperature() - s) / (float) (m - s);
		return i > 1.0F ? 1 : i < 0.0F ? 0 : i;
	}
}