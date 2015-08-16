package fle;

import java.util.Map;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import fle.api.FleAPI;
import fle.api.FleModHandler;
import fle.api.crop.CropRegister;
import fle.api.energy.RotationNet;
import fle.api.util.ColorMap;
import fle.api.util.FleLog;
import fle.api.util.IAirConditionProvider;
import fle.api.util.IColorMapHandler;
import fle.api.util.IPlatform;
import fle.core.CommonProxy;
import fle.core.Proxy;
import fle.core.energy.FleThermalNet;
import fle.core.net.NetWorkHandler;
import fle.core.util.FleColorMap;
import fle.core.util.FleCropRegister;
import fle.core.util.FluidDictionary;
import fle.core.util.Keyboard;
import fle.core.util.SideGateway;
import fle.core.world.FWM;
import fle.tech.FleTechManager;

@Mod(modid = FLE.MODID, name = FLE.NAME, version = FLE.VERSION)
public class FLE implements FleModHandler
{
    public static final String MODID = "fle";
    public static final String NAME = "Far Land Era";
    public static final String VERSION = "2.02a";
	
    @Instance(MODID)
    public static FLE fle;

    @SidedProxy(modId = MODID, clientSide = "fle.core.ClientProxy", serverSide = "fle.core.CommonProxy")
    public static Proxy proxy = new CommonProxy();
    private SideGateway<IPlatform> p;
    private FWM wm;
    private NetWorkHandler nw;
    private CropRegister cr;
    private FleTechManager tm;
    private FleThermalNet tn;
    private SideGateway<Keyboard> k;
    
    public FLE() 
    {
    	FleAPI.mod = fle = this;
    	FleAPI.fluidDictionary = new FluidDictionary();
    	p = new SideGateway<IPlatform>("fle.core.PlatformCommon", "fle.core.PlatformClient");
    	k = new SideGateway<Keyboard>("fle.core.util.Keyboard", "fle.core.util.KeyboardClient");
    	nw = new NetWorkHandler();
    	cr = new FleCropRegister();
    	wm = new FWM();
    	tm = new FleTechManager();
    	tn = new FleThermalNet();
    }

    @EventHandler
    public void preLoad(FMLPreInitializationEvent event)
    {
    	FleLog.logger.info("Far Land Era start pre load.");
    	proxy.onPreload();
	}

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
    	FleLog.logger.info("Far Land Era start load.");
    	proxy.onLoad();
	}

    @EventHandler
    public void postLoad(FMLPostInitializationEvent event)
    {
    	FleLog.logger.info("Far Land Era start post load.");
    	NetworkRegistry.INSTANCE.registerGuiHandler(MODID, proxy);
    	proxy.onPostload();
	}

    @EventHandler
    public void completeLoad(FMLLoadCompleteEvent event)
    {
    	FleLog.logger.info("Far Land Era start complete load.");
    	proxy.onCompleteLoad();
	}
    
	@Override
	public IPlatform getPlatform() 
	{
		return p.get();
	}

	@Override
	public NetWorkHandler getNetworkHandler() 
	{
		return nw;
	}

	@Override
	public FleThermalNet getThermalNet() 
	{
		return tn;
	}

	@Override
	public RotationNet getRotationNet() 
	{
		return null;
	}

	@Override
	public CropRegister getCropRegister() 
	{
		return cr;
	}

	@NetworkCheckHandler
	public static boolean checkHandler(Map<String, String> version, Side side)
	{
		return true;
	}

	@Override
	public IColorMapHandler getColorMapHandler() 
	{
		return proxy instanceof IColorMapHandler ? (IColorMapHandler) proxy : 
			new IColorMapHandler()
		{
			@Override
			public ColorMap registerColorMap(String aResourceName) 
			{
				return new FleColorMap();
			}
		};
	}

	@Override
	public FWM getWorldManager() 
	{
		return wm;
	}

	@Override
	public FleTechManager getTechManager() 
	{
		return tm;
	}
	
	public Keyboard getKeyboard()
	{
		return k.get();
	}

	@Override
	public FWM getAirConditionProvider()
	{
		return wm;
	}
}