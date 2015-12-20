package flapi;

import flapi.energy.RotationNet;
import flapi.energy.ThermalNet;
import flapi.net.FleNetworkHandler;
import flapi.plant.CropRegister;
import flapi.plant.PlantRegister;
import flapi.tech.ITechManager;
import flapi.util.IColorMapHandler;
import flapi.util.IPlatform;
import flapi.world.IAirConditionProvider;
import flapi.world.IWorldManager;

/**
 * The interface of main mod.
 * @author ueyudiud
 *
 */
public interface Mod
{
	IPlatform getPlatform();
	ITechManager getTechManager();
	FleNetworkHandler getNetworkHandler();
	
	ThermalNet getThermalNet();
	RotationNet getRotationNet();
	
	CropRegister getCropRegister();
	PlantRegister getPlantRegister();
	
	IColorMapHandler getColorMapHandler();
	IWorldManager getWorldManager();
	IAirConditionProvider getAirConditionProvider();
}