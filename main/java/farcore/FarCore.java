package farcore;

import farcore.energy.electric.ElectricNet;
import farcore.energy.kinetic.KineticNet;
import farcore.energy.thermal.ThermalNet;

public class FarCore
{
	public static final String ID = "farcore";
	public static final String OVERRIDE_ID = "fo";
	
	public static volatile int version = 300;

	public static ThermalNet thermalNet;
	public static KineticNet kineticNet;
	public static ElectricNet electricNet;
	
	public static String translateToLocal(String unlocalized, Object...arg)
	{
		return FarCoreSetup.lang.translateToLocal(unlocalized, arg);
	}
}