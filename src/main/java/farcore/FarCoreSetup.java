/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;

import farcore.asm.FarOverride;
import farcore.data.Config;
import farcore.lib.command.CommandDate;
import farcore.lib.model.block.statemap.StateMapperExt;
import farcore.lib.oredict.OreDictExt;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_Containerable;
import farcore.lib.util.IRenderRegister;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.Log;
import farcore.load.ClientLoader;
import farcore.load.CommonLoader;
import farcore.util.U;
import farcore.util.U.ClientHandler;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The mod of far core.<br>
 * Some configuration should called when
 * forge initialization step, so there should
 * has a mod to call these configuration to
 * initialize.<br>
 * @author ueyudiud
 * @see farcore.FarCore
 */
@Mod(
		modid = FarCore.ID,
		/*
		 * The version of Far Core not always refresh.
		 * Now Far Core is main update continent. Please
		 * use FLE version to compact your Far Core modification
		 * is needed update.
		 * The major version will not be 1.1 until Far Core is
		 * enough to make FLE.
		 */
		version = "1.0o",
		name = "Far Core")
public class FarCoreSetup
{
	private LanguageManager lang;
	
	/**
	 * The sided proxy of far core.
	 */
	@SidedProxy(serverSide = "farcore.FarCoreSetup$Proxy", clientSide = "farcore.FarCoreSetup$ClientProxy")
	public static Proxy proxy;
	
	@Instance(FarCore.ID)
	public static FarCoreSetup setup;
	
	public FarCoreSetup()
	{
		setup = this;
		Log.logger = LogManager.getLogger(FarCore.ID);
		OreDictExt.init();
	}
	
	@EventHandler
	public void check(FMLFingerprintViolationEvent event)
	{
		/**
		 * The Far Core mod used Java8. There are method
		 * is added in Java8, so I use a method exist since
		 * Java8.
		 */
		Log.info("Far Core start check java version...");
		try
		{
			Map map = new HashMap();
			map.getOrDefault("", "");
		}
		catch(Exception exception)
		{
			throw new IllegalArgumentException("Java version is out of date, this mod is suggested use java 8 to run.", exception);
		}
		/**
		 * Coded checking.
		 */
		Log.info("Far Core checking mod version...");
		try
		{
			new BlockPos(1, 2, 3).add(0, 0, 0);
		}
		catch(Exception exception)
		{
			throw new IllegalArgumentException("You may download dev version, please check your mod version and use default version.", exception);
		}
		/**
		 * Checking forge version.
		 */
		Log.info("Far Core checking forge version...");
		int forge = ForgeVersion.getBuildVersion();
		if ((forge > 0) && (forge < FarCore.MIN_FORGE))
			throw new RuntimeException("The currently installed version of "
					+ "Minecraft Forge (" + ForgeVersion.getMajorVersion() + "." + ForgeVersion.getMinorVersion() + "." +
					ForgeVersion.getRevisionVersion() + "." + forge + ") is too old.\n" +
					"Please update the Minecraft Forge.\n" + "\n" +
					"(Technical information: " + forge + " < " + FarCore.MIN_FORGE + ")");
		Log.info("Checking end.");
	}
	
	@EventHandler
	public void load(FMLPreInitializationEvent event)
	{
		ModMetadata modMetadata = event.getModMetadata();
		modMetadata.authorList.add("ueyudiud");
		modMetadata.name = "Far Core";
		modMetadata.credits = "ueyudiud";
		/**
		 * Put child mods into ModMetadata.
		 */
		for(ModContainer container : Loader.instance().getModList())
		{
			if(FarCore.OVERRIDE_ID.equals(container.getName()))
			{
				modMetadata.childMods.add(container);
				break;
			}
		}
		/**
		 * Loading non-resource-file from Minecraft running path.
		 */
		try
		{
			this.lang = new LanguageManager(new File(U.Mod.getMCFile(), "lang"));
			Log.info("Loading configuration.");
			File file = event.getSuggestedConfigurationFile();
			Configuration configuration = new Configuration(file);
			configuration.load();
			Config.load(configuration);
			configuration.save();
		}
		catch(Exception exception)
		{
			Log.warn("Fail to load configuration.", exception);
		}
		this.lang.read();
		FarCoreSetup.proxy.load(event);
	}
	
	@EventHandler
	public void Load(FMLInitializationEvent event)
	{
		FarCoreSetup.proxy.load(event);
	}
	
	@EventHandler
	public void load(FMLPostInitializationEvent event)
	{
		FarCoreSetup.proxy.load(event);
	}
	
	@EventHandler
	public void complete(FMLLoadCompleteEvent event)
	{
		FarCoreSetup.proxy.load(event);
		this.lang.write();
	}
	
	@EventHandler
	public void load(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandDate());
	}
	
	@NetworkCheckHandler
	public static boolean check(Map<String, String> versions, Side side)
	{
		if(!versions.containsKey(FarCore.OVERRIDE_ID) || !FarOverride.VERSION.equals(versions.get(FarCore.OVERRIDE_ID)))
		{
			Log.warn("Fail to check " + side + " the FarOverride mod is missing or outdate please check your modpack is available to use.");
			return false;
		}
		return true;
	}
	
	public static class Proxy implements IGuiHandler
	{
		CommonLoader loader;
		
		public Proxy()
		{
			this.loader = createLoader();
		}
		
		protected CommonLoader createLoader()
		{
			return new CommonLoader();
		}
		
		public void load(FMLPreInitializationEvent event)
		{
			this.loader.preload();
		}
		
		public void load(FMLInitializationEvent event)
		{
			this.loader.load();
		}
		
		public void load(FMLPostInitializationEvent event)
		{
			this.loader.postload();
		}
		
		public void load(FMLLoadCompleteEvent event)
		{
			this.loader.complete();
		}
		
		@Override
		public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
		{
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			if(tile instanceof ITB_Containerable)
				return ((ITB_Containerable) tile).openContainer(ID, player);
			return null;
		}
		
		@Override
		public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
		{
			return null;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ClientProxy extends Proxy implements IResourceManagerReloadListener
	{
		private static Map<String, List<IRenderRegister>> registers = new HashMap();
		public static List<Block> buildInRender = new ArrayList();
		
		public ClientProxy()
		{
			//Take this proxy into resource manager reload listener.
			((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
		}
		
		@Override
		protected CommonLoader createLoader()
		{
			return new ClientLoader();
		}
		
		@Override
		public void load(FMLPreInitializationEvent event)
		{
			super.load(event);
			//Register all render object.
			//PLACED CALL THIS METHOD ONCE IN CLIENT PROXY IF YOUR MOD CREATE NEW GAMING ELEMENTS(BLOCK, ITEM, ETC).
			registerRenderObject();
		}
		
		@Override
		public void onResourceManagerReload(IResourceManager manager)
		{
			//Checking is reached in preinitalization state.
			if (Loader.instance().hasReachedState(LoaderState.PREINITIALIZATION))
			{
				BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
				ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
				for(Entry<IBlockColor, List<Block>> entry : ClientHandler.blockColorMap.entrySet())
				{
					blockColors.registerBlockColorHandler(
							entry.getKey(), farcore.util.L.cast(entry.getValue(), Block.class));
				}
				for(Entry<IItemColor, List<Item>> entry : ClientHandler.itemColorMap.entrySet())
				{
					itemColors.registerItemColorHandler(
							entry.getKey(), farcore.util.L.cast(entry.getValue(), Item.class));
				}
			}
			
			if (Loader.instance().hasReachedState(LoaderState.POSTINITIALIZATION))
			{
				U.Client.getFontRender().onResourceManagerReload(manager);
			}
		}
		
		@Override
		public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
		{
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			if(tile instanceof ITB_Containerable)
				return ((ITB_Containerable) tile).openGUI(ID, player);
			return null;
		}
		
		public static <T extends Comparable<T>> void registerCompactModel(StateMapperExt mapper, Block block, int metaCount)
		{
			Item item = Item.getItemFromBlock(block);
			for (int i = 0; i < metaCount; ++i)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, mapper.getModelResourceLocation(block.getStateFromMeta(i)));
			}
			ModelLoader.setCustomStateMapper(block, mapper);
		}
		
		public static <T extends Comparable<T>> void registerCompactModel(StateMapperExt mapper, Block block, IProperty<T> property)
		{
			Item item = Item.getItemFromBlock(block);
			IBlockState state = block.getDefaultState();
			if(property != null)
			{
				for (T value : property.getAllowedValues())
				{
					IBlockState state2 = state.withProperty(property, value);
					ModelLoader.setCustomModelResourceLocation(item, block.getMetaFromState(state2), mapper.getModelResourceLocation(state2));
				}
			}
			else
			{
				ModelLoader.setCustomModelResourceLocation(item, 0, mapper.getModelResourceLocation(state));
			}
			ModelLoader.setCustomStateMapper(block, mapper);
		}
		
		public void addRenderRegisterListener(IRenderRegister register)
		{
			if (Loader.instance().hasReachedState(LoaderState.INITIALIZATION))
			{
				Log.warn("Register too late, place register before initalization.");
			}
			else if(register != null)
			{
				farcore.util.L.put(registers, U.Mod.getActiveModID(), register);
			}
		}
		
		/**
		 * Let client proxy called this method when FML pre-initialization.
		 */
		public static void registerRenderObject()
		{
			List<IRenderRegister> reg = registers.remove(U.Mod.getActiveModID());
			if(reg != null)
			{
				for(IRenderRegister register : reg)
				{
					register.registerRender();
				}
			}
		}
		
		/**
		 * Internal, do not use. (Use ASM from forge)
		 */
		public static void onRegisterAllBlocks(BlockModelShapes shapes)
		{
			shapes.registerBuiltInBlocks(farcore.util.L.cast(buildInRender, Block.class));
			buildInRender = null;
		}
	}
}