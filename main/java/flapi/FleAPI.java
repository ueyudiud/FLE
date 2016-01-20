package flapi;

import net.minecraft.world.World;
import flapi.energy.EnergyType;
import flapi.energy.IElectricalNet;
import flapi.energy.IThermalNet;
import fle.core.util.LanguageManager;

public final class FleAPI
{
	/** The mod id */
	public static final String MODID = "fle";
	/** The mod version */
	public volatile int VERSION = 300;
	/** The main class of mod */
	public static FleMod mod;
	public static LanguageManager lang;
	
	private FleAPI()
	{
		
	}
	
	public static IElectricalNet getElectricalNet()
	{
		return (IElectricalNet) mod.getEnergyNet(EnergyType.Electrical);
	}

	public static IThermalNet getThermalNet()
	{
		return (IThermalNet) mod.getEnergyNet(EnergyType.Thermal);
	}

	public static World getWorld(int dim)
	{
		return mod.getPlatform().getWorldInstance(dim);
	}
}