package fle.api.enums;

import fle.api.util.IChemCondition.EnumPH;

public enum EnumFLERock
{
	Rhyolite(EnumPH.Weak_Acid, 0, 0),
	Andesite(EnumPH.Water, 0, 0),
	Basalt(EnumPH.Weak_Alkali, 0, 0),
	Peridotite(EnumPH.Strong_Alkali, 0, 0);
	
	public static EnumFLERock getRock(double PH, double weather)
	{
		if(PH > 0.6) return Rhyolite;
		else if(PH > 0.0) return Andesite;
		else if(PH > -0.6) return Basalt;
		else return Peridotite;
	}
	
	EnumPH ph;
	int wl;
	int dl;
	
	EnumFLERock(EnumPH PH, int weatheringLevel, int deepLevel)
	{
		ph = PH;
		wl = weatheringLevel;
		dl = deepLevel;
	}
	
	public EnumPH getPH()
	{
		return ph;
	}
	
	public int getWeatheringLevel()
	{
		return wl;
	}
	
	public int getDeepLevel()
	{
		return dl;
	}
}