package farcore;

import java.io.File;

import org.apache.logging.log4j.LogManager;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.plant.tree.BlockLog;
import farcore.debug.BlockDebug;
import farcore.debug.BlockDebug1;
import farcore.energy.electric.ElectricNet;
import farcore.energy.kinetic.KineticNet;
import farcore.energy.thermal.ThermalNet;
import farcore.entity.EntityFallingBlockExtended;
import farcore.handler.FarCoreCraftingHandler;
import farcore.handler.FarCoreEnergyHandler;
import farcore.handler.FarCoreHarvestHandler;
import farcore.handler.FarCorePlayerHandler;
import farcore.item.ItemDebugger;
import farcore.item.ItemFluidDisplay;
import farcore.lib.command.CommandCalendar;
import farcore.lib.command.CommandWorldData;
import farcore.lib.net.PacketSound;
import farcore.lib.net.entity.PacketEntity;
import farcore.lib.net.entity.PacketEntityAsk;
import farcore.lib.net.gui.PacketFluidSlotClicked;
import farcore.lib.net.gui.PacketFluidUpdate;
import farcore.lib.net.gui.PacketFluidUpdateLarge;
import farcore.lib.net.gui.PacketGuiAction;
import farcore.lib.net.tile.PacketTileAskSync;
import farcore.lib.net.tile.PacketTileSyncable;
import farcore.lib.net.world.PacketWorldDataAskUpdate;
import farcore.lib.net.world.PacketWorldDataUpdateAll;
import farcore.lib.net.world.PacketWorldDataUpdateSingle;
import farcore.lib.render.RenderFallingBlockExtended;
import farcore.lib.render.RenderHandler;
import farcore.network.Network;
import farcore.util.CalendarHandler;
import farcore.util.FleLog;
import farcore.util.LanguageManager;
import farcore.util.U;
import farcore.util.V;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.ChunkDataEvent.Load;

@Mod(modid = FarCore.ID, version = "0.5", name = "Far Core")
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
		FarCore.thermalNet = new ThermalNet();
		FarCore.kineticNet = new KineticNet();
		FarCore.electricNet = new ElectricNet();
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
		try
		{
			File file = event.getSuggestedConfigurationFile();
			Configuration configuration = new Configuration(file);
			configuration.load();
			V.init(configuration);
			configuration.save();
		}
		catch(Exception exception)
		{
			FleLog.getCoreLogger().warn("Fail to load configuration.");
		}
		CalendarHandler.init();
		Object handler;
		MinecraftForge.EVENT_BUS.register(new FarCorePlayerHandler());
		MinecraftForge.EVENT_BUS.register(new FarCoreHarvestHandler());
		handler = new FarCoreEnergyHandler();
		FarCoreEnergyHandler.BUS.register(handler);
		MinecraftForge.EVENT_BUS.register(handler);
		FMLCommonHandler.instance().bus().register(handler);
		FMLCommonHandler.instance().bus().register(new FarCoreCraftingHandler());
		
		FarCoreEnergyHandler.addNet(FarCore.thermalNet);
		FarCoreEnergyHandler.addNet(FarCore.kineticNet);
		FarCoreEnergyHandler.addNet(FarCore.electricNet);
		
		lang = new LanguageManager(
				new File(Minecraft.getMinecraft().mcDataDir, "fle_lang.json"));
		lang.load();
		
		int id = V.fallingBlockEntityID;
		EntityRegistry.registerModEntity(EntityFallingBlockExtended.class, "fle.falling.block", id, FarCore.ID, 32, 20, true);
		new BlockDebug();//Just for debug.
		new BlockDebug1();//Just for debug.
		
		new ItemFluidDisplay();//Initialize fluid and void block icon.
		new ItemDebugger();//Initialize debugger for fle and void item icon.
		proxy.registerClient();
	}
	
	@EventHandler
	public void Load(FMLInitializationEvent event)
	{
		BlockLog.init();
	}
	
	@EventHandler
	public void load(FMLPostInitializationEvent event)
	{
		network = Network.newHandler(FarCore.ID);
		if(!V.disableWM)
		{
			network.registerPacket(PacketWorldDataUpdateSingle.class, Side.CLIENT);
			network.registerPacket(PacketWorldDataUpdateAll.class, Side.CLIENT);
			network.registerPacket(PacketWorldDataAskUpdate.class, Side.SERVER);
		}
		network.registerPacket(PacketTileAskSync.class, Side.SERVER);
		network.registerPacket(PacketTileSyncable.class, Side.CLIENT);
		network.registerPacket(PacketGuiAction.class, Side.SERVER);
		network.registerPacket(PacketFluidUpdate.class, Side.CLIENT);
		network.registerPacket(PacketFluidUpdateLarge.class, Side.CLIENT);
		network.registerPacket(PacketFluidSlotClicked.class, Side.SERVER);
		network.registerPacket(PacketSound.class, Side.CLIENT);
		network.registerPacket(PacketEntity.class, Side.CLIENT);
		network.registerPacket(PacketEntityAsk.class, Side.SERVER);
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
		event.registerServerCommand(new CommandCalendar());
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
			RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlockExtended.class, new RenderFallingBlockExtended());
		}
		
		@Override
		public void load()
		{
			super.load();
		}
	}
}