package fle.core.util;

import fle.api.util.IChemCondition;

public class ChemCondition implements IChemCondition
{
	EnumPH p;
	EnumOxide o;
	int t;
	
	public ChemCondition(EnumPH ph, EnumOxide ox, int tem)
	{
		p = ph;
		o = ox;
		t = tem;
	}

	@Override
	public EnumPH getPHLevel()
	{
		return p;
	}

	@Override
	public EnumOxide getOxideLevel()
	{
		return o;
	}

	@Override
	public int getTemperature()
	{
		return t;
	}
}