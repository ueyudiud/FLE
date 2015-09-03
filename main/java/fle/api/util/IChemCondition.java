package fle.api.util;

public interface IChemCondition
{
	EnumPH getPHLevel();
	
	EnumOxide getOxideLevel();
	
	int getTemperature();
	
	public static enum EnumPH
	{
		Super_Acid,
		Strong_Acid,
		Weak_Acid,
		Water,
		Weak_Alkali,
		Strong_Alkali,
		Super_Alkali;
	}
	
	public static enum EnumOxide
	{
		SbF6,
		F2,
		O2,
		Cl2,
		Br2,
		Fe3,
		I2,
		S,
		C;
	}
}