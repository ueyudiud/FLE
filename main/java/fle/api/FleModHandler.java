package fle.api;

import fle.api.crop.CropRegister;
import fle.api.energy.RotationNet;
import fle.api.energy.ThermalNet;
import fle.api.net.FleNetworkHandler;
import fle.api.plant.PlantCard;
import fle.api.plant.PlantRegister;
import fle.api.tech.ITechManager;
import fle.api.util.IAirConditionProvider;
import fle.api.util.IColorMapHandler;
import fle.api.util.IPlatform;
import fle.api.world.IWorldManager;

/**
 * The interface of main mod.
 * @author ueyudiud
 *
 */
public interface FleModHandler 
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