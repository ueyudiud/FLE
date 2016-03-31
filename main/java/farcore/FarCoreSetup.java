package farcore;

import java.io.File;

import org.apache.logging.log4j.LogManager;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.debug.BlockDebug;
import farcore.handler.FarCoreCraftingHandler;
import farcore.handler.FarCoreHarvestHandler;
import farcore.handler.FarCorePlayerHandler;
import farcore.lib.command.CommandWorldData;
import farcore.lib.net.PacketSound;
import farcore.lib.net.gui.PacketFluidSlotClicked;
import farcore.lib.net.gui.PacketFluidUpdate;
import farcore.lib.net.gui.PacketFluidUpdateLarge;
import farcore.lib.net.gui.PacketGuiAction;
import farcore.lib.net.tile.PacketTileAskSync;
import farcore.lib.net.tile.PacketTileSyncable;
import farcore.lib.net.world.PacketWorldDataUpdateAll;
import farcore.lib.net.world.PacketWorldDataUpdateSingle;
import farcore.lib.render.RenderHandler;
import farcore.lib.world.WorldDatas;
import farcore.network.Network;
import farcore.util.FleLog;
import farcore.util.LanguageManager;
import farcore.util.U;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = FarCore.ID, version = "0.3", name = "Far Core")
public class FarCoreSetup
{
    public static final int minForge = 1420;

    public static RenderHandler handlerA;
    public static RenderHandler handlerB;
    
	@Instance(FarCore.ID)
	public static FarCoreSetup setup;
	
	@SidedProxy(serverSide = "farcore.FarCoreSetup$Proxy", clientSide = "farcore.FarCoreSetup$ClientProxy")
	public static Proxy proxy;
	
	public static Network network;
	public static LanguageManager lang;
	
	public FarCoreSetup()
	{
		setup = this;
    	FleLog.coreLogger = LogManager.getLogger(FarCore.ID);
	}
	
	@EventHandler
	public void load(FMLPreInitializationEvent event)
	{
		FleLog.getLogger().info("Far Core start check forge version.");
        int forge = ForgeVersion.getBuildVersion();
        if ((forge > 0) && (forge < minForge))
        {
        	throw new RuntimeException("The currently installed version of "
        			+ "Minecraft Forge (" + ForgeVersion.getMajorVersion() + "." + ForgeVersion.getMinorVersion() + "." + 
        					ForgeVersion.getRevisionVersion() + "." + forge + ") is too old.\n" + 
        			"Please update the Minecraft Forge.\n" + "\n" + 
        					"(Technical information: " + forge + " < " + minForge + ")");
        }
		
    	MinecraftForge.EVENT_BUS.register(WorldDatas.instance);
    	MinecraftForge.EVENT_BUS.register(new FarCorePlayerHandler());
    	MinecraftForge.EVENT_BUS.register(new FarCoreHarvestHandler());
    	FMLCommonHandler.instance().bus().register(new FarCoreCraftingHandler());
		FMLCommonHandler.instance().bus().register(WorldDatas.instance);
		lang = new LanguageManager(
				new File(Minecraft.getMinecraft().mcDataDir, "fle_lang.json"));
		lang.load();
		new BlockDebug();
		proxy.registerClient();
	}
	
	@EventHandler
	public void load(FMLPostInitializationEvent event)
	{
		network = Network.newHandler(FarCore.ID);
		network.registerPacket(PacketWorldDataUpdateSingle.class, Side.CLIENT);
		network.registerPacket(PacketWorldDataUpdateAll.class, Side.CLIENT);
		network.registerPacket(PacketTileAskSync.class, Side.SERVER);
		network.registerPacket(PacketTileSyncable.class, Side.CLIENT);
		network.registerPacket(PacketGuiAction.class, Side.SERVER);
		network.registerPacket(PacketFluidUpdate.class, Side.CLIENT);
		network.registerPacket(PacketFluidUpdateLarge.class, Side.CLIENT);
		network.registerPacket(PacketFluidSlotClicked.class, Side.SERVER);
		network.registerPacket(PacketSound.class, Side.CLIENT);
	}

	@EventHandler
	public void complete(FMLLoadCompleteEvent event)
	{
		lang.save();
		U.Reflect.resetReflectCache();
	}
	
	@EventHandler
	public void load(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandWorldData());
	}
	
	public static class Proxy
	{
		public void registerClient()
		{
			
		}
		
		public void load()
		{
			
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ClientProxy extends Proxy
	{
		@Override
		public void registerClient()
		{
			super.registerClient();
			int id1 = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(id1, handlerA = new RenderHandler(id1, false));
			int id2 = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(id2, handlerB = new RenderHandler(id2, true));
		}
		
		@Override
		public void load()
		{
			super.load();
		}
	}
}