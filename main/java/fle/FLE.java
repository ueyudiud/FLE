package fle;

import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import fle.api.FleAPI;
import fle.api.FleModHandler;
import fle.api.crop.CropRegister;
import fle.api.energy.RotationNet;
import fle.api.energy.ThermalNet;
import fle.api.net.FleNetworkHandler;
import fle.api.util.IPlatform;
import fle.core.Proxy;
import fle.core.net.NetWorkHandler;
import fle.core.util.SideGateway;

public class FLE implements FleModHandler
{
    public static final String MODID = "fle";
    public static final String NAME = "Far Land Era";
    public static final String VERSION = "2.02a";
	
    @Instance(MODID)
    public static FLE fle;

    @SidedProxy(modId = MODID, clientSide = "fle.core.ClientProxy", serverSide = "fle.core.CommonProxy")
    public static Proxy proxy;
    public SideGateway<IPlatform> p;
    public SideGateway<NetWorkHandler> nw;
    
    public FLE() 
    {
    	FleAPI.mod = fle = this;
    	proxy = new SideGateway<Proxy>("fle.core.CommonProxy", "fle.core.ClientProxy").get();
    	p = new SideGateway<IPlatform>("fle.core.PlatformCommon", "fle.core.PlatformClient");
    	nw = new SideGateway<NetWorkHandler>("fle.core.net.NetWorkHandler", "fle.core.name.NetWorkClient");
    }
    
	@Override
	public IPlatform getPlatform() 
	{
		return p.get();
	}

	@Override
	public FleNetworkHandler getNetworkHandler() 
	{
		return nw.get();
	}

	@Override
	public ThermalNet getThermalNet() 
	{
		return null;
	}

	@Override
	public RotationNet getRotationNet() 
	{
		return null;
	}

	@Override
	public CropRegister getCropRegister() 
	{
		return null;
	}
}