package fle.core.util;

import flapi.material.IChemCondition;

public class ChemCondition implements IChemCondition
{
	EnumPH p;
	EnumOxide o;
	int t;
	EnumEnviorment e;
	
	public ChemCondition(EnumPH ph, EnumOxide ox, int tem, EnumEnviorment en)
	{
		p = ph;
		o = ox;
		t = tem;
		e = en;
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

	@Override
	public EnumEnviorment isOpenEnviorment()
	{
		return e;
	}
}