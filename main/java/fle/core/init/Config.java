package fle.core.init;

import java.util.HashMap;
import java.util.Map;

import flapi.util.Values;
import fle.core.FLE;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config
{
	public static boolean useForgeFluidDetermine;
	
	public static boolean waterEvaporation;
	public static boolean alkaliExplosion;
	
	public static void init(Configuration config)
	{
		useForgeFluidDetermine = config.getBoolean("useForgeFluidDetermine", "generic", false, 
				"Disable this option to use far land era fluid determination of movement, " +
				"which makes fluid flow more smooth. This funtion needs more RAM, during " + 
				"updating the fluid. Please check if your computer configuration is enough " +
				"to hold it.");
		if(FLE.doesExist(Values.mod_resource))
		{
			waterEvaporation = config.getBoolean("waterEvaporation", "resource", true, 
					"Enable this option will cause water will slowly evaporate.");
			alkaliExplosion = config.getBoolean("alkliExplosion", "resource", true, 
					"Enable this option, alkali metal will explode when contact water or acid.");
		}
	}
}