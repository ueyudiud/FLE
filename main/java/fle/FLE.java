package fle;

import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import fle.api.FleModHandler;
import fle.api.crop.CropRegister;
import fle.api.energy.RotationNet;
import fle.api.energy.ThermalNet;
import fle.api.net.FleNetworkHandler;
import fle.api.util.IPlatform;
import fle.core.Proxy;

public class FLE implements FleModHandler
{
    public static final String MODID = "fle";
    public static final String NAME = "Far Land Era";
    public static final String VERSION = "2.02a";
	
    @Instance(MODID)
    public static FLE fle;

    @SidedProxy(modId = MODID, clientSide="fle.core.proxy.ClientProxy", serverSide="fle.core.proxy.CommonProxy")
    public static Proxy proxy;
    
    public FLE() 
    {
    	
	}
    
	@Override
	public IPlatform getPlatform() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FleNetworkHandler getNetworkHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ThermalNet getThermalNet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RotationNet getRotationNet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CropRegister getCropRegister() {
		// TODO Auto-generated method stub
		return null;
	}

}
