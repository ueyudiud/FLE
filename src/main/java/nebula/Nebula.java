/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula;

import static nebula.common.LanguageManager.registerLocal;
import static net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import io.netty.bootstrap.Bootstrap;
import nebula.client.CreativeTabBase;
import nebula.common.CommonProxy;
import nebula.common.LanguageManager;
import nebula.common.NebulaConfig;
import nebula.common.NebulaItemHandler;
import nebula.common.NebulaKeyHandler;
import nebula.common.NebulaPlayerHandler;
import nebula.common.NebulaSynchronizationHandler;
import nebula.common.NebulaWorldHandler;
import nebula.common.block.BlockBase;
import nebula.common.config.NebulaConfiguration;
import nebula.common.data.DataSerializers;
import nebula.common.entity.EntityFallingBlockExtended;
import nebula.common.entity.EntityProjectileItem;
import nebula.common.item.ItemBase;
import nebula.common.item.ItemFluidDisplay;
import nebula.common.nbt.INBTReaderAndWritter;
import nebula.common.network.Network;
import nebula.common.network.packet.PacketBreakBlock;
import nebula.common.network.packet.PacketChunkNetData;
import nebula.common.network.packet.PacketContainerDataUpdateAll;
import nebula.common.network.packet.PacketContainerDataUpdateSingle;
import nebula.common.network.packet.PacketEntity;
import nebula.common.network.packet.PacketEntityAsk;
import nebula.common.network.packet.PacketFluidSlotClick;
import nebula.common.network.packet.PacketGuiAction;
import nebula.common.network.packet.PacketGuiSyncData;
import nebula.common.network.packet.PacketGuiTickUpdate;
import nebula.common.network.packet.PacketKey;
import nebula.common.network.packet.PacketTEAsk;
import nebula.common.network.packet.PacketTESAsk;
import nebula.common.network.packet.PacketTESync;
import nebula.common.util.EnumChatFormatting;
import nebula.common.util.Game;
import nebula.common.util.L;
import nebula.common.util.Sides;
import nebula.common.world.IBlockDataProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.WorldAccessContainer;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Nebula, is a Minecraft modification provide more useful library for other
 * modification, also changed some game rule for more compact to other mods.
 * 
 * @author ueyudiud
 */
@IFMLLoadingPlugin.MCVersion("1.10.2")
public class Nebula extends DummyModContainer implements WorldAccessContainer
{
	/** The minimum forge version far core required. */
	public static final int MIN_FORGE = 2272;
	
	public static final String	MODID	= "nebula";
	public static final String	NAME	= "Nebula";
	public static final String	VERSION	= "2.3.2";
	
	/**
	 * The built-in render id, for prevent has location collide when naming
	 * resource path.
	 * <p>
	 * Most of Nebula built-in model use this location.
	 */
	@Deprecated
	public static final String INNER_RENDER = "nebula_inner";
	
	@Instance(MODID)
	public static Nebula instance;
	
	/**
	 * The debug mode flag, enable to switch to debug mode.
	 * <p>
	 * The debug mode will give more information of game, and exception will be
	 * always throw instead of log to file.
	 * <p>
	 */
	public static boolean debug = false;
	
	/**
	 * The network instance of nebula modification.
	 */
	public static Network network;
	
	/**
	 * The block state data provider. Use to get unique id of BlockState,
	 * generalized for 4096 id and 16 meta. Specially, if you added ED (Extra
	 * Data mod), the data provider will be Nebula full version one (This
	 * function was split from Nebula), which can store 20 bits meta.
	 */
	public static IBlockDataProvider blockDataProvider = new IBlockDataProvider.Template();
	
	/**
	 * The fluid item.
	 * @see ItemFluidDisplay
	 */
	public static ItemFluidDisplay fluid_displayment;
	
	/**
	 * The world data providers, use to load or save world data from file.
	 * <p>
	 * The provider SHOULD access <tt>null</tt> as input for method <tt>readFromNBT</tt>
	 * for if the data is missing.
	 */
	public static final Map<String, INBTReaderAndWritter<Void, ?>> worldDataProviders = new HashMap<>(4);
	
	public static void addWorldDataProvider(String name, INBTReaderAndWritter<Void, ?> provider)
	{
		worldDataProviders.put(name, provider);
	}
	
	/**
	 * The mod sided proxy.
	 */
	@SidedProxy(modId = MODID, serverSide = "nebula.common.CommonProxy", clientSide = "nebula.client.ClientProxy")
	public static CommonProxy proxy;
	
	static
	{
		// For bootstrap takes too long time to initialize...
		Thread thread = new Thread(Bootstrap::new, "Bootstrap Initalizer");
		// Any exception thrown needn't be print.
		thread.setUncaughtExceptionHandler((t, e)-> {});
		thread.start();
	}
	
	/** The language manager. */
	private LanguageManager	lang;
	
	/** The configuration of Nebula. */
	private Configuration configuration;
	
	public static CreativeTabs tabFluids;
	
	public Nebula()
	{
		super(new ModMetadata());
		instance = this;
		ModMetadata meta = getMetadata();
		meta.modId = MODID;
		meta.name = NAME;
		meta.version = VERSION;
		meta.credits = "ueyudiud";
		meta.authorList = ImmutableList.of("ueyudiud");
		meta.description = "Nebula core.";
		meta.logoFile = "/assets/nebula/textures/logo.png";
		Log.logger = LogManager.getLogger(Nebula.NAME);
	}
	
	@Override
	public Nebula getMod()
	{
		return this;
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller)
	{
		bus.register(this);
		return true;
	}
	
	/**
	 * Get language manager instance.
	 * <p>
	 * Use to internationalize string, if you need't reload
	 * localization file, use state method instead.
	 * 
	 * @return the language manager.
	 * @see nebula.common.LanguageManager
	 */
	public LanguageManager getLanguageManager()
	{
		return this.lang;
	}
	
	@Subscribe
	public void check(FMLConstructionEvent event)
	{
		Log.info("Injecting Nebula proxy...");// Forge does not let Dummy Mod Container auto inject proxy.
		try
		{
			SidedProxy proxy = getClass().getField("proxy").getAnnotation(SidedProxy.class);
			Nebula.proxy = (CommonProxy) Class.forName(Sides.isClient() ? proxy.clientSide() : proxy.serverSide()).newInstance();
		}
		catch (Exception exception)
		{
			throw new RuntimeException("Fail to inject proxy!");
		}
		
		/**
		 * The Nebula and its child mod use Java8. There are method is added in
		 * Java8, so it is checked by a type exist since Java8. This checking
		 * will be removed when forge using Java8 for compile.
		 */
		Log.info("Nebula start check java version...");
		try
		{
			((Function<?, ?>) arg -> null).apply(null);
		}
		catch (Exception exception)
		{
			throw new RuntimeException("Java version is out of date, please use java 8 to launch.", exception);
		}
		/**
		 * Checking forge version.
		 */
		Log.info("Nebula checking forge version...");
		int forge = ForgeVersion.getBuildVersion();
		if ((forge > 0) && (forge < MIN_FORGE)) throw new RuntimeException("The currently installed version of " + "Minecraft Forge (" + ForgeVersion.getMajorVersion() + "." + ForgeVersion.getMinorVersion() + "." + ForgeVersion.getRevisionVersion() + "." + forge + ") is out of data.\n"
				+ "Please update the Minecraft Forge.\n" + "\n" + "(Technical information: " + forge + " < " + MIN_FORGE + ")");
		Log.info("Checking end.");
	}
	
	@Subscribe
	public void load(FMLPreInitializationEvent event)
	{
		this.lang = new LanguageManager(new File(Game.getMCFile(), "lang"));
		this.configuration = NebulaConfiguration.loadStaticConfig(NebulaConfig.class);
		
		MinecraftForge.EVENT_BUS.register(new NebulaKeyHandler());
		MinecraftForge.EVENT_BUS.register(new NebulaPlayerHandler());
		MinecraftForge.EVENT_BUS.register(new NebulaWorldHandler());
		MinecraftForge.EVENT_BUS.register(new NebulaItemHandler());
		MinecraftForge.EVENT_BUS.register(new NebulaSynchronizationHandler());
		
		if (NebulaConfig.displayFluidInTab)
		{
			tabFluids = new CreativeTabBase("nebula.fluids", "Fluids[Nebula]", () -> new ItemStack(Items.WATER_BUCKET));
		}
		fluid_displayment = (ItemFluidDisplay) new ItemFluidDisplay().setCreativeTab(tabFluids);
		int id = 0;
		registerModEntity(EntityFallingBlockExtended.class, "FallingBlockExtended", id++, "nebula", 32, 20, true);
		registerModEntity(EntityProjectileItem.class, "ProjectileItem", id++, "nebula", 32, 20, true);
		proxy.loadClient();
	}
	
	@Subscribe
	public void load(FMLInitializationEvent event)
	{
		ItemBase.post();
		BlockBase.post();
		registerLocal("info.shift.click", EnumChatFormatting.WHITE + "Press " + EnumChatFormatting.ITALIC + "<%s>" + EnumChatFormatting.RESET + " to get more information.");
		registerLocal("info.food.label", EnumChatFormatting.RED + "Food Stat:");
		registerLocal("info.food.display", EnumChatFormatting.RED + "F-%s S-%s W-%s");
		NetworkRegistry.INSTANCE.registerGuiHandler(MODID, proxy);
		network = Network.network(Nebula.MODID);
		network.registerPacket(PacketEntity.class, Side.CLIENT);
		network.registerPacket(PacketEntityAsk.class, Side.SERVER);
		network.registerPacket(PacketKey.class, Side.SERVER);
		network.registerPacket(PacketTESync.class, Side.CLIENT);
		network.registerPacket(PacketTESAsk.class, Side.CLIENT);
		network.registerPacket(PacketTEAsk.class, Side.SERVER);
		network.registerPacket(PacketBreakBlock.class, Side.CLIENT);
		network.registerPacket(PacketContainerDataUpdateAll.class, Side.CLIENT);
		network.registerPacket(PacketContainerDataUpdateSingle.class, Side.CLIENT);
		network.registerPacket(PacketFluidSlotClick.class, Side.SERVER);
		network.registerPacket(PacketGuiTickUpdate.class, Side.SERVER);
		network.registerPacket(PacketChunkNetData.class, Side.CLIENT);
		network.registerPacket(PacketGuiAction.class, Side.SERVER);
		network.registerPacket(PacketGuiSyncData.class, Side.CLIENT);
	}
	
	@Subscribe
	public void load(FMLLoadCompleteEvent event)
	{
		DataSerializers.init();
		this.lang.write();
	}
	
	@Override
	public String getGuiClassName()
	{
		return "nebula.client.NebulaGuiFactory";
	}
	
	@Subscribe
	public void onConfigChanged(OnConfigChangedEvent event)
	{
		if (event.getModID().equals(MODID))
		{
			NebulaConfiguration.loadStaticConfig(NebulaConfig.class, this.configuration);
		}
	}
	
	@Override
	public NBTTagCompound getDataForWriting(SaveHandler handler, WorldInfo info)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		for (Entry<String, INBTReaderAndWritter<Void, ?>> entry : worldDataProviders.entrySet())
		{
			nbt.setTag(entry.getKey(), entry.getValue().writeToNBT(null));
		}
		return nbt;
	}
	
	@Override
	public void readData(SaveHandler handler, WorldInfo info, Map<String, NBTBase> propertyMap, NBTTagCompound tag)
	{
		for (Entry<String, INBTReaderAndWritter<Void, ?>> entry : worldDataProviders.entrySet())
		{
			entry.getValue().readFromNBT(L.castAny(tag.getTag(entry.getKey())));
		}
	}
}
