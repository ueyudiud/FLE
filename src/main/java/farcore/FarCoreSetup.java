package farcore;

import static farcore.FarCoreRegistry.addEnergyNet;
import static farcore.FarCoreRegistry.newTextureMap;
import static farcore.FarCoreRegistry.registerFMLEventHandler;
import static farcore.FarCoreRegistry.registerKey;
import static farcore.FarCoreRegistry.registerMFEventHandler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.alpha.block.BlockRockSlab;
import farcore.alpha.item.ItemDebugger;
import farcore.alpha.lib.PacketTEAsk;
import farcore.alpha.lib.PacketTESAskRender;
import farcore.alpha.lib.PacketTESync;
import farcore.alpha.render.RenderSlab;
import farcore.block.plant.crop.BlockCrop;
import farcore.energy.electric.ElectricNet;
import farcore.energy.kinetic.KineticNet;
import farcore.energy.thermal.ThermalNet;
import farcore.entity.EntityFallingBlockExtended;
import farcore.enums.EnumBlock;
import farcore.handler.FarCoreAchievementHandler;
import farcore.handler.FarCoreChunkHandler;
import farcore.handler.FarCoreCraftingHandler;
import farcore.handler.FarCoreEnergyHandler;
import farcore.handler.FarCoreGuiHandler;
import farcore.handler.FarCoreHarvestHandler;
import farcore.handler.FarCoreKeyHandler;
import farcore.handler.FarCorePlayerHandler;
import farcore.interfaces.item.ILocalizedRegisterListener;
import farcore.item.ItemFluidDisplay;
import farcore.lib.command.CommandCalendar;
import farcore.lib.command.CommandWorldData;
import farcore.lib.crop.TileEntityCrop;
import farcore.lib.net.PacketKey;
import farcore.lib.net.PacketSound;
import farcore.lib.net.entity.PacketEntity;
import farcore.lib.net.entity.PacketEntityAsk;
import farcore.lib.net.entity.player.PacketPlayerStatUpdate;
import farcore.lib.net.gui.PacketFluidSlotClicked;
import farcore.lib.net.gui.PacketFluidUpdate;
import farcore.lib.net.gui.PacketFluidUpdateLarge;
import farcore.lib.net.gui.PacketGuiAction;
import farcore.lib.net.tile.PacketTileAskSync;
import farcore.lib.net.tile.PacketTileSyncable;
import farcore.lib.net.world.PacketWorldDataAskUpdate;
import farcore.lib.net.world.PacketWorldDataUpdateAll;
import farcore.lib.net.world.PacketWorldDataUpdateSingle;
import farcore.lib.potion.PotionBase;
import farcore.lib.render.RenderCrop;
import farcore.lib.render.RenderFallingBlockExtended;
import farcore.lib.render.RenderHandler;
import farcore.lib.render.RenderTreeSapling;
import farcore.network.Network;
import farcore.util.CalendarHandler;
import farcore.util.FleLog;
import farcore.util.FleTextureMap.TextureMapRegistry;
import farcore.util.LanguageManager;
import farcore.util.U;
import farcore.util.V;
import farcore.util.Values;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = FarCore.ID, version = "0.9", name = "Far Core")
public class FarCoreSetup
{
	public static final int minForge = 1558;
	
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
	public void check(FMLFingerprintViolationEvent event)
	{
		FleLog.getLogger().info("Far Core start check java version...");
		try
		{
			Map map = new HashMap();
			map.getOrDefault("", "");
		}
		catch(Exception exception)
		{
			throw new IllegalArgumentException("Java version is out of date, this mod is suggested use java 8 to run.", exception);
		}
		FleLog.getLogger().info("Far Core checking mod version...");
		try
		{
			new ChunkCoordinates(1, 2, 3);
		}
		catch(Exception exception)
		{
			throw new IllegalArgumentException("You may download dev version, please check your mod version and use default version.", exception);
		}
		FleLog.getLogger().info("Far Core checking forge version...");
		int forge = ForgeVersion.getBuildVersion();
		if ((forge > 0) && (forge < minForge))
		{
			throw new RuntimeException("The currently installed version of "
					+ "Minecraft Forge (" + ForgeVersion.getMajorVersion() + "." + ForgeVersion.getMinorVersion() + "." + 
					ForgeVersion.getRevisionVersion() + "." + forge + ") is too old.\n" + 
					"Please update the Minecraft Forge.\n" + "\n" + 
					"(Technical information: " + forge + " < " + minForge + ")");
		}
		FleLog.getCoreLogger().info("Checking end.");
	}
	
	@EventHandler
	public void load(FMLPreInitializationEvent event)
	{
		for(ModContainer container : Loader.instance().getModList())
		{
			if(FarCore.OVERRIDE_ID.equals(container.getName()))
			{
				event.getModMetadata().childMods.add(container);
				break;
			}
		}
		try
		{
			FleLog.getCoreLogger().info("Loading configuration.");
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
		handler = new FarCorePlayerHandler();
		registerMFEventHandler(handler);
		registerFMLEventHandler(handler);
		registerMFEventHandler(new FarCoreHarvestHandler());
		handler = new FarCoreEnergyHandler();
		FarCoreEnergyHandler.BUS.register(handler);
		registerMFEventHandler(handler);
		registerFMLEventHandler(handler);
		handler = new FarCoreAchievementHandler();
		registerMFEventHandler(handler);
		registerFMLEventHandler(handler);
		handler = new FarCoreChunkHandler();
		registerMFEventHandler(handler);
		registerFMLEventHandler(handler);
		registerFMLEventHandler(new FarCoreCraftingHandler());
		registerFMLEventHandler(new FarCoreKeyHandler());
		
		addEnergyNet(FarCore.thermalNet);
		addEnergyNet(FarCore.kineticNet);
		addEnergyNet(FarCore.electricNet);
		
		lang = new LanguageManager(
				new File(U.Mod.getMCFile(), "fle_lang.json"));
		lang.load();
		
		int id = V.fallingBlockEntityID;
		EntityRegistry.registerModEntity(EntityFallingBlockExtended.class, "fle.falling.block", id, FarCore.ID, 32, 20, true);
		
		new ItemFluidDisplay();//Initialize fluid and void block icon.
		new ItemDebugger().setTextureName("farcore:fle").setCreativeTab(CreativeTabs.tabRedstone);//Initialize debugger for fle and void item icon.
		proxy.registerClient();
	}
	
	@EventHandler
	public void Load(FMLInitializationEvent event)
	{
		farcore.alpha.block.tree.BlockLog.init();
		new BlockCrop("crop");
		GameRegistry.registerTileEntity(TileEntityCrop.class, "farcore.crop");
		registerKey(Values.key_place, Keyboard.KEY_P);
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
		network.registerPacket(PacketKey.class, Side.SERVER);
		network.registerPacket(PacketPlayerStatUpdate.class, Side.CLIENT);

		network.registerPacket(PacketTESync.class, Side.CLIENT);
		network.registerPacket(PacketTESAskRender.class, Side.CLIENT);
		network.registerPacket(PacketTEAsk.class, Side.SERVER);
		proxy.load();
	}

	@EventHandler
	public void complete(FMLLoadCompleteEvent event)
	{
		for(Item item : GameData.getItemRegistry().typeSafeIterable())
		{
			if(item instanceof ILocalizedRegisterListener)
			{
				((ILocalizedRegisterListener) item).registerLocalizedName(lang);
			}
		}
		lang.save();
		U.Reflect.resetReflectCache();
		proxy.completeLoad();
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
		
		public void completeLoad()
		{
			
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ClientProxy extends Proxy implements IResourceManagerReloadListener
	{
		private boolean loadComplete = false;
		
		public ClientProxy()
		{
			((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
			((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(new TextureMapRegistry());
			FarCore.bottonTextureMap = newTextureMap("bottons");
			FarCore.conditionTextureMap = newTextureMap("conditions");
			FarCore.potionTextureMap = newTextureMap("potions");
		}
		
		@Override
		public void registerClient()
		{
			super.registerClient();
			int id1 = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(id1, FarCore.handlerA = new RenderHandler(id1, false));
			int id2 = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(id2, FarCore.handlerB = new RenderHandler(id2, true));
			RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlockExtended.class, new RenderFallingBlockExtended());
			FarCore.potionTextureMap.registerTextureListener((IIconRegister register) -> 
			{
				for(PotionBase potion : PotionBase.list)
				{
					potion.registerIcon(register);
				}
			});
		}
				
		@Override
		public void load()
		{
			super.load();
			Object handler = new FarCoreGuiHandler();
			registerMFEventHandler(handler);
			registerFMLEventHandler(handler);
		}
		
		@Override
		public void completeLoad()
		{
			super.completeLoad();
			FarCore.handlerA.register(EnumBlock.crop.block(), OreDictionary.WILDCARD_VALUE, RenderCrop.class);
			FarCore.handlerA.register(EnumBlock.sapling.block(), OreDictionary.WILDCARD_VALUE, RenderTreeSapling.class);
			for(Object key : GameData.getBlockRegistry().getKeys())
			{
				Block block = (Block) GameData.getBlockRegistry().getObject(key);
				if(block instanceof BlockRockSlab)
				{
					FarCore.handlerB.register(block, OreDictionary.WILDCARD_VALUE, RenderSlab.class);
				}
			}
			loadComplete = true;
		}

		@Override
		public void onResourceManagerReload(IResourceManager manager)
		{
			if(loadComplete)
			{
				reloadLang();
			}
		}
	}

	public static void reloadLang()
	{
		FleLog.getCoreLogger().info("Reload localize map...");
		lang.reset();
		lang.load();
		for(Item item : GameData.getItemRegistry().typeSafeIterable())
		{
			if(item instanceof ILocalizedRegisterListener)
			{
				((ILocalizedRegisterListener) item).registerLocalizedName(lang);
			}
		}
//		for(Fluid fluid : FluidRegistry.getRegisteredFluids().values())
//		{
//			if(fluid instanceof Fluid)
//		}
		lang.save();
	}
}