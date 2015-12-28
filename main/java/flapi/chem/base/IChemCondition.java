package flapi.chem.base;

public interface IChemCondition
{
	EnumEnviorment isOpenEnviorment();
	
	EnumPH getPHLevel();
	
	EnumOxide getOxideLevel();
	
	int getTemperature();
	
	public static enum EnumEnviorment
	{
		Open,
		Close;
	}
	
	public static enum EnumPH
	{
		MinPH,
		Super_Acid,
		Strong_Acid,
		Weak_Acid,
		Water,
		Weak_Alkali,
		Strong_Alkali,
		Super_Alkali,
		MaxPH;
	}
	
	public static enum EnumOxide
	{
		Highest,
		SbF6,
		F2,
		O2,
		Cl2,
		Br2,
		Fe3,
		I2,
		S, 
		Default,
		C,
		H2,
		Si,
		Mg,
		Lowest;
	}
}