package fle.api;

import fle.api.crop.CropRegister;
import fle.api.energy.RotationNet;
import fle.api.energy.ThermalNet;
import fle.api.net.FleNetworkHandler;
import fle.api.util.IPlatform;

public interface FleModHandler 
{
	public IPlatform getPlatform();
	
	public FleNetworkHandler getNetworkHandler();
	
	public ThermalNet getThermalNet();
	
	public RotationNet getRotationNet();
	
	public CropRegister getCropRegister();
}