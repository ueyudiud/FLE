package fle;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import flapi.FleAPI;
import flapi.plant.CropRegister;
import flapi.util.ColorMap;
import flapi.util.FleLog;
import flapi.util.FleValue;
import flapi.util.IColorMapHandler;
import flapi.util.IPlatform;
import fle.core.CommonProxy;
import fle.core.Proxy;
import fle.core.energy.FleRotationNet;
import fle.core.energy.FleThermalNet;
import fle.core.init.Config;
import fle.core.net.NWH;
import fle.core.recipe.FLERA;
import fle.core.tech.FleTechManager;
import fle.core.util.FleColorMap;
import fle.core.util.FleSetup;
import fle.core.util.Keyboard;
import fle.core.util.LanguageManager;
import fle.core.util.SideGateway;
import fle.core.world.FWM;
import fle.resource.FleCropRegister;
import fle.resource.FlePlantRegister;

@Mod(modid = FLE.MODID, name = FLE.NAME, version = FLE.VERSION, certificateFingerprint = "after:IC2")
public class FLE implements flapi.Mod
{	
    public static final String MODID = "fle";
    public static final String NAME = "Far Land Era";
    public static final String VERSION = "2.06e";
    public static final int minForge = 1420;
    
    private static final UUID modUUID = new UUID(-7834374458361585156L, -677774780L);
    
    @Instance(MODID)
    public static FLE fle;

    @SidedProxy(modId = MODID, clientSide = "fle.core.ClientProxy", serverSide = "fle.core.CommonProxy")
    public static Proxy proxy = new CommonProxy();
    private SideGateway<IPlatform> p;
    private SideGateway<FWM> wm;
    private NWH nw;
    private CropRegister cr;
    private FlePlantRegister pr;
    private FleTechManager tm;
    private FleThermalNet tn;
    private FleRotationNet rn;
    private SideGateway<Keyboard> k;
    private Configuration config;
    
    public FLE() 
    {
    	new FleSetup().setup();
    	FleAPI.mod = fle = this;
    	FleAPI.langManager = new LanguageManager();
    	FleAPI.ra = new FLERA();
    	p = new SideGateway<IPlatform>("fle.core.PlatformCommon", "fle.core.PlatformClient");
    	k = new SideGateway<Keyboard>("fle.core.util.Keyboard", "fle.core.util.KeyboardClient");
    	nw = NWH.init();
    	cr = new FleCropRegister();
    	pr = new FlePlantRegister();
    	wm = new SideGateway<FWM>("fle.core.world.FWM", "fle.core.world.FWMClient");
    	tm = new FleTechManager();
    	tn = new FleThermalNet();
    	rn = new FleRotationNet();
    }
    
    @EventHandler
    public void invalidFingerprint(FMLFingerprintViolationEvent event)
    {
        String str = getClass().getResource("").getPath();
        long i = str.length();
        long i1 = (MODID.hashCode() * 31 + VERSION.hashCode()) * 31 + NAME.hashCode();
        for(char c : str.toCharArray())
        	i = i * 31 + c;
        UUID uuid = new UUID(i, i1);
        if (modUUID.equals(uuid))
        {
            FleLog.getLogger().info("The resource of Far Land Era are running.");
        }
        else if(modUUID.getLeastSignificantBits() == i1)
        {
            FleLog.getLogger().info(
            		"The copy of Far Land Era that you are running has been modified from the original, "
            		+ "and unpredictable things may happen. Please consider re-downloading the original version of the mod.");
        }
        else
        {
        	throw new RuntimeException(
            		"The copy of Far Land Era that you are running is a development version of the mod, "
            		+ "and as such may be unstable and/or incomplete.");
        }
    }

    @EventHandler
    public void preLoad(FMLPreInitializationEvent event)
    {
    	FleLog.setLogger(event.getModLog());
        ModMetadata mm = event.getModMetadata();
        mm.credits = "ueyudiud";
        mm.description = "Far tech mod.";
        mm.autogenerated = false;
        mm.authorList = Arrays.asList("ueyudiud");
        mm.version = VERSION;
        mm.url = "http://tieba.baidu.com/p/3977359185";
        mm.logoFile = "/assets/" + FleValue.TEXTURE_FILE + "/textures/logo.png";
        
    	FleLog.getLogger().info("Far Land Era start checking forge version.");
        int forge = ForgeVersion.getBuildVersion();
        if ((forge > 0) && (forge < minForge))
        {
        	throw new RuntimeException("The currently installed version of "
        			+ "Minecraft Forge (" + ForgeVersion.getMajorVersion() + "." + ForgeVersion.getMinorVersion() + "." + 
        					ForgeVersion.getRevisionVersion() + "." + forge + ") is too old.\n" + 
        			"Please update the Minecraft Forge.\n" + "\n" + 
        					"(Technical information: " + forge + " < " + minForge + ")");
        }
    	FleLog.getLogger().info("Far Land Era start load config.");
        try
        {
        	File configFile = new File(new File(p.get().getMinecraftDir(), "config"), "FLE.cfg");
        	//File recipe = new File(new File(p.get().getMinecraftDir(), "config"), "FLE Recipe.csv");
        	config = new Configuration(configFile);
        	config.load();
        	FleLog.getLogger().info("Config loaded from " + configFile.getAbsolutePath());
        }
        catch (Exception e)
        {
        	FleLog.getLogger().warn("Error while trying to access configuration! " + e);
        	config = null;
        }
        if(config != null)
        {
            Config.init(config);
        }
    	FleLog.getLogger().info("Far Land Era start pre load.");
		LanguageManager.load();
		proxy.onPreload();
	}

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
    	FleLog.getLogger().info("Far Land Era start load.");
    	proxy.onLoad();
	}

    @EventHandler
    public void postLoad(FMLPostInitializationEvent event)
    {
    	FleLog.getLogger().info("Far Land Era start post load.");
    	NetworkRegistry.INSTANCE.registerGuiHandler(MODID, new CommonProxy());
    	proxy.onPostload();
	}

    @EventHandler
    public void completeLoad(FMLLoadCompleteEvent event)
    {
    	FleLog.getLogger().info("Far Land Era start complete load.");
    	proxy.onCompleteLoad();
    	FleLog.getLogger().info("Saving languages.");
    	LanguageManager.save();
    	if(config != null)
    	{
            config.save();
    	}
	}
    
	@Override
	public IPlatform getPlatform() 
	{
		return p.get();
	}

	@Override
	public NWH getNetworkHandler() 
	{
		return nw;
	}

	@Override
	public FleThermalNet getThermalNet() 
	{
		return tn;
	}

	@Override
	public FleRotationNet getRotationNet() 
	{
		return rn;
	}

	@Override
	public FlePlantRegister getPlantRegister()
	{
		return pr;
	}

	@Override
	public CropRegister getCropRegister() 
	{
		return cr;
	}

	@Override
	public FWM getWorldManager() 
	{
		return wm.get();
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
		return wm.get();
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
}