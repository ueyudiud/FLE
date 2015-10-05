package fle.api;

import fle.api.crop.CropRegister;
import fle.api.energy.RotationNet;
import fle.api.energy.ThermalNet;
import fle.api.net.FleNetworkHandler;
import fle.api.tech.ITechManager;
import fle.api.util.IAirConditionProvider;
import fle.api.util.IColorMapHandler;
import fle.api.util.IPlatform;
import fle.api.world.IWorldManager;

public interface FleModHandler 
{
	IPlatform getPlatform();
	ITechManager getTechManager();
	FleNetworkHandler getNetworkHandler();
	
	ThermalNet getThermalNet();
	RotationNet getRotationNet();
	
	CropRegister getCropRegister();
	
	IColorMapHandler getColorMapHandler();
	IWorldManager getWorldManager();
	IAirConditionProvider getAirConditionProvider();
}