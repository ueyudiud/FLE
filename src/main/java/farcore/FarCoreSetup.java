package farcore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;

import com.mojang.realmsclient.gui.ChatFormatting;

import farcore.asm.LightFix;
import farcore.data.CT;
import farcore.data.Config;
import farcore.data.EnumBlock;
import farcore.data.EnumItem;
import farcore.data.M;
import farcore.data.MC;
import farcore.data.Potions;
import farcore.energy.electric.ElectricNet;
import farcore.energy.kinetic.KineticNet;
import farcore.energy.thermal.HeatWave;
import farcore.energy.thermal.ThermalNet;
import farcore.handler.FarCoreEnergyHandler;
import farcore.handler.FarCoreGuiHandler;
import farcore.handler.FarCoreItemHandler;
import farcore.handler.FarCoreKeyHandler;
import farcore.handler.FarCoreWorldHandler;
import farcore.instances.TemperatureHandler;
import farcore.lib.block.BlockBase;
import farcore.lib.block.instance.BlockCarvedRock;
import farcore.lib.block.instance.BlockCrop;
import farcore.lib.block.instance.BlockFire;
import farcore.lib.block.instance.BlockIce;
import farcore.lib.block.instance.BlockOre;
import farcore.lib.block.instance.BlockSapling;
import farcore.lib.block.instance.BlockWater;
import farcore.lib.entity.EntityFallingBlockExtended;
import farcore.lib.entity.EntityProjectileItem;
import farcore.lib.fluid.FluidWater;
import farcore.lib.item.ItemBase;
import farcore.lib.item.ItemMulti;
import farcore.lib.item.instance.ItemDebugger;
import farcore.lib.item.instance.ItemFluidDisplay;
import farcore.lib.item.instance.ItemOreChip;
import farcore.lib.item.instance.ItemSeed;
import farcore.lib.item.instance.ItemStoneChip;
import farcore.lib.item.instance.ItemStoneFragment;
import farcore.lib.material.Mat;
import farcore.lib.model.block.ModelCrop;
import farcore.lib.model.block.ModelFluidBlock;
import farcore.lib.model.block.ModelOre;
import farcore.lib.model.block.ModelSapling;
import farcore.lib.model.block.StateMapperExt;
import farcore.lib.model.entity.RenderFallingBlockExt;
import farcore.lib.model.entity.RenderProjectileItem;
import farcore.lib.model.item.FarCoreItemModelLoader;
import farcore.lib.net.PacketKey;
import farcore.lib.net.entity.PacketEntity;
import farcore.lib.net.entity.PacketEntityAsk;
import farcore.lib.net.gui.PacketFluidSlotClick;
import farcore.lib.net.gui.PacketFluidUpdateAll;
import farcore.lib.net.gui.PacketFluidUpdateSingle;
import farcore.lib.net.gui.PacketGuiTickUpdate;
import farcore.lib.net.tile.PacketTEAsk;
import farcore.lib.net.tile.PacketTEAskType;
import farcore.lib.net.tile.PacketTESAsk;
import farcore.lib.net.tile.PacketTESync;
import farcore.lib.net.tile.PacketTETypeResult;
import farcore.lib.net.world.PacketBreakBlock;
import farcore.lib.oredict.OreDictExt;
import farcore.lib.render.Colormap.ColormapFactory;
import farcore.lib.render.FontRenderExtend;
import farcore.lib.render.instance.FontMap;
import farcore.lib.tesr.TESRCarvedRock;
import farcore.lib.tile.ITilePropertiesAndBehavior.ITB_Containerable;
import farcore.lib.tile.instance.TECoreLeaves;
import farcore.lib.tile.instance.TECrop;
import farcore.lib.tile.instance.TECustomCarvedStone;
import farcore.lib.tile.instance.TELossTile;
import farcore.lib.tile.instance.TEOre;
import farcore.lib.tile.instance.TESapling;
import farcore.lib.util.CreativeTabBase;
import farcore.lib.util.IRenderRegister;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.Log;
import farcore.network.Network;
import farcore.util.U;
import farcore.util.U.L;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
@Mod(modid = FarCore.ID, version = "1.0l", name = "Far Core")
public class FarCoreSetup
{
	public static final int minForge = 2011;
	
	private LanguageManager lang;
	
	@Instance(FarCore.ID)
	public static FarCoreSetup setup;
	
	@SidedProxy(serverSide = "farcore.FarCoreSetup$Proxy", clientSide = "farcore.FarCoreSetup$ClientProxy")
	public static Proxy proxy;
	
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
		if ((forge > 0) && (forge < minForge))
			throw new RuntimeException("The currently installed version of "
					+ "Minecraft Forge (" + ForgeVersion.getMajorVersion() + "." + ForgeVersion.getMinorVersion() + "." +
					ForgeVersion.getRevisionVersion() + "." + forge + ") is too old.\n" +
					"Please update the Minecraft Forge.\n" + "\n" +
					"(Technical information: " + forge + " < " + minForge + ")");
		Log.info("Checking end.");
	}
	
	@EventHandler
	public void load(FMLPreInitializationEvent event)
	{
		ModMetadata modMetadata = event.getModMetadata();
		modMetadata.authorList.add("ueyudiud");
		modMetadata.name = "Far Core";
		modMetadata.credits = "ueyudiud";
		for(ModContainer container : Loader.instance().getModList())
		{
			if(FarCore.OVERRIDE_ID.equals(container.getName()))
			{
				modMetadata.childMods.add(container);
				break;
			}
		}
		try
		{
			lang = new LanguageManager(new File(U.Mod.getMCFile(), "lang"));
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
		lang.read();
		proxy.load(event);
	}
	
	@EventHandler
	public void Load(FMLInitializationEvent event)
	{
		proxy.load(event);
	}
	
	@EventHandler
	public void load(FMLPostInitializationEvent event)
	{
		proxy.load(event);
	}
	
	@EventHandler
	public void complete(FMLLoadCompleteEvent event)
	{
		proxy.load(event);
		lang.write();
	}
	
	@EventHandler
	public void load(FMLServerStartingEvent event)
	{
	}
	
	public static class Proxy implements IGuiHandler
	{
		public void load(FMLPreInitializationEvent event)
		{
			if(Config.displayFluidInTab)
			{
				CT.tabFluids = new CreativeTabBase("farcore.fluids", "Fluids[FarCore]")
				{
					@Override
					public ItemStack getIconItemStack()
					{
						return new ItemStack(Items.WATER_BUCKET);
					}
				};
			}
			CT.tabTree = new CreativeTabBase("farcore.tree", "Far Tree")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(M.oak.log);
				}
			};
			CT.tabTerria = new CreativeTabBase("farcore.terria", "Far Terria")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(M.peridotite.rock, 1, 2);
				}
			};
			CT.tabBuilding = new CreativeTabBase("farcore.building", "Far Building Blocks")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(M.marble.rock, 1, 5);
				}
			};
			CT.tabResourceItem = new CreativeTabBase("farcore.resource.item", "Far Resource Item")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(EnumItem.stone_chip.item, 1, M.peridotite.id);
				}
			};
			CT.tabMachine = new CreativeTabBase("farcore.machine", "Far Machine")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(Blocks.CRAFTING_TABLE);
				}
			};
			CT.tabMaterial = new CreativeTabBase("farcore.material", "Far Material")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(Items.EMERALD);
				}
			};
			CT.tabTool = new CreativeTabBase("farcore.tool", "Far Tool")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(EnumItem.debug.item);
				}
			};
			MinecraftForge.EVENT_BUS.register(new FarCoreKeyHandler());
			MinecraftForge.EVENT_BUS.register(FarCoreEnergyHandler.getHandler());
			MinecraftForge.EVENT_BUS.register(new FarCoreWorldHandler());
			MinecraftForge.EVENT_BUS.register(new FarCoreItemHandler());
			FarCoreEnergyHandler.addNet(ThermalNet.instance);
			FarCoreEnergyHandler.addNet(KineticNet.instance);
			FarCoreEnergyHandler.addNet(ElectricNet.instance);
			FarCoreWorldHandler.registerObject("heat.wave", HeatWave.class);
			ThermalNet.registerWorldThermalHandler(new TemperatureHandler());
			M.init();

			new ItemDebugger().setCreativeTab(CT.tabTool);
			new ItemFluidDisplay().setCreativeTab(CT.tabFluids);
			new ItemStoneChip().setCreativeTab(CT.tabResourceItem);
			new ItemSeed().setCreativeTab(CT.tabResourceItem);
			new ItemStoneFragment().setCreativeTab(CT.tabResourceItem);
			EnumItem.nugget.set(new ItemMulti(MC.nugget).setCreativeTab(CT.tabResourceItem));
			new ItemOreChip().setCreativeTab(CT.tabResourceItem);
			new BlockSapling().setCreativeTab(CT.tabTree);
			new BlockCrop();
			new BlockFire();
			new BlockOre().setCreativeTab(CT.tabTerria);
			new BlockWater(new FluidWater("pure.water", "Pure Water", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow")));
			new BlockIce().setCreativeTab(CT.tabTerria);
			new BlockCarvedRock();
			GameRegistry.registerTileEntity(TELossTile.class, "farcore.loss.tile");
			GameRegistry.registerTileEntity(TECrop.class, "farcore.crop");
			GameRegistry.registerTileEntity(TEOre.class, "farcore.ore");
			GameRegistry.registerTileEntity(TECustomCarvedStone.class, "farcore.carved.stone");
			GameRegistry.registerTileEntity(TESapling.class, "farcore.sapling");
			GameRegistry.registerTileEntity(TECoreLeaves.class, "farcore.core.leaves");
			int id = 0;
			EntityRegistry.registerModEntity(EntityFallingBlockExtended.class, "fle.falling.block", id++, FarCore.ID, 32, 20, true);
			EntityRegistry.registerModEntity(EntityProjectileItem.class, "fle.projectile", id++, FarCore.ID, 32, 20, true);
			Potions.init();
		}
		
		public void load(FMLInitializationEvent event)
		{
			ItemBase.post();
			BlockBase.post();
			LanguageManager.registerLocal("info.debug.date", "Date : ");
			LanguageManager.registerLocal("info.log.length", "Legnth : %d");
			LanguageManager.registerLocal("info.slab.place", "Place slab in sneaking can let slab only has up or down facing.");
			LanguageManager.registerLocal("info.stone.chip.throwable", "You can throw it out to attack entities.");
			LanguageManager.registerLocal("info.crop.type", "Crop Name : %s");
			LanguageManager.registerLocal("info.crop.generation", "Generation : %d");
			LanguageManager.registerLocal("info.tool.damage", "Durability : " + ChatFormatting.GREEN + " %d / %d");
			LanguageManager.registerLocal("info.tool.harvest.level", "Harvest Level : " + ChatFormatting.YELLOW + " lv%d");
			LanguageManager.registerLocal("info.tool.hardness", "Hardness : " + ChatFormatting.BLUE + "%s");
			LanguageManager.registerLocal("info.tool.head.name", "Tool Head : " + ChatFormatting.LIGHT_PURPLE + "%s");
			LanguageManager.registerLocal("info.tool.handle.name", "Tool Handle : " + ChatFormatting.LIGHT_PURPLE + "%s");
			LanguageManager.registerLocal("info.tool.tie.name", "Tool Tie : " + ChatFormatting.LIGHT_PURPLE + "%s");
			LanguageManager.registerLocal("skill.upgrade.info", "The skill " + ChatFormatting.ITALIC + "%s" + ChatFormatting.RESET + " is upgrade from %d to %d level.");
			FarCore.network = Network.network(FarCore.ID);
			FarCore.network.registerPacket(PacketEntity.class, Side.CLIENT);
			FarCore.network.registerPacket(PacketEntityAsk.class, Side.SERVER);
			FarCore.network.registerPacket(PacketKey.class, Side.SERVER);

			FarCore.network.registerPacket(PacketTESync.class, Side.CLIENT);
			FarCore.network.registerPacket(PacketTETypeResult.class, Side.CLIENT);
			FarCore.network.registerPacket(PacketTESAsk.class, Side.CLIENT);
			FarCore.network.registerPacket(PacketTEAsk.class, Side.SERVER);
			FarCore.network.registerPacket(PacketTEAskType.class, Side.SERVER);
			
			FarCore.network.registerPacket(PacketBreakBlock.class, Side.CLIENT);

			FarCore.network.registerPacket(PacketFluidUpdateAll.class, Side.CLIENT);
			FarCore.network.registerPacket(PacketFluidUpdateSingle.class, Side.CLIENT);
			FarCore.network.registerPacket(PacketFluidSlotClick.class, Side.SERVER);
			FarCore.network.registerPacket(PacketGuiTickUpdate.class, Side.SERVER);
		}
		
		public void load(FMLPostInitializationEvent event)
		{
			for(Mat material : Mat.materials())
			{
				if(material.customDisplayInformation != null)
				{
					LanguageManager.registerLocal("info.material.custom." + material.name, material.customDisplayInformation);
				}
				if(material.chemicalFormula != null)
				{
					LanguageManager.registerLocal("info.material.chemical.formula." + material.name, material.chemicalFormula);
				}
			}
		}
		
		public void load(FMLLoadCompleteEvent event)
		{
			if(Config.multiThreadLight)
			{
				LightFix.startThread();
			}
		}

		@SideOnly(Side.CLIENT)
		public void registerColorMultiplier(IBlockColor color, Block...block)
		{
			
		}

		@SideOnly(Side.CLIENT)
		public void registerColorMultiplier(IItemColor color, Block...block)
		{
			
		}

		@SideOnly(Side.CLIENT)
		public void registerColorMultiplier(IItemColor color, Item...block)
		{
			
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
		private static List<Block> buildInRender = new ArrayList();
		private Map<IBlockColor, List<Block>> blockColorMap = new HashMap();
		private Map<IItemColor, List<Block>> itemBlockColorMap = new HashMap();
		private Map<IItemColor, List<Item>> itemColorMap = new HashMap();
		private boolean loadComplete = false;
		
		public ClientProxy()
		{
			((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
		}
		
		@Override
		public void load(FMLPreInitializationEvent event)
		{
			super.load(event);
			
			((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(ColormapFactory.INSTANCE);

			MinecraftForge.EVENT_BUS.register(new FarCoreGuiHandler());
			FontRenderExtend.addFontMap(new FontMap(new ResourceLocation(FarCore.ID, "textures/font/greeks.png"), "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρστυφχψω"));

			registerRenderObject();

			RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlockExtended.class, RenderFallingBlockExt.Factory.instance);
			RenderingRegistry.registerEntityRenderingHandler(EntityProjectileItem.class, RenderProjectileItem.Factory.instance);

			ModelLoaderRegistry.registerLoader(FarCoreItemModelLoader.instance);

			ModelLoaderRegistry.registerLoader(ModelFluidBlock.Loader.instance);
			ModelLoaderRegistry.registerLoader(ModelSapling.instance);
			ModelLoaderRegistry.registerLoader(ModelCrop.instance);
			ModelLoaderRegistry.registerLoader(ModelOre.instance);
			ModelLoader.setCustomStateMapper(EnumBlock.sapling.block, ModelSapling.instance);
			ModelLoader.setCustomStateMapper(EnumBlock.crop.block, ModelCrop.instance);
			ModelLoader.setCustomStateMapper(EnumBlock.ore.block, ModelOre.instance);
			U.Mod.registerCustomItemModelSelector(EnumBlock.sapling.block, ModelSapling.instance);
			U.Mod.registerCustomItemModelSelector(EnumBlock.ore.block, ModelOre.instance);

			ClientRegistry.bindTileEntitySpecialRenderer(TECustomCarvedStone.class, new TESRCarvedRock());
		}
		
		@Override
		public void load(FMLInitializationEvent event)
		{
			super.load(event);
		}
		
		@Override
		public void load(FMLPostInitializationEvent event)
		{
			super.load(event);
			loadComplete = true;
		}
		
		@Override
		public void load(FMLLoadCompleteEvent event)
		{
			super.load(event);
		}
		
		@Override
		public void onResourceManagerReload(IResourceManager manager)
		{
			if (Loader.instance().hasReachedState(LoaderState.PREINITIALIZATION))
			{
				BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
				for(Entry<IBlockColor, List<Block>> entry : blockColorMap.entrySet())
				{
					blockColors.registerBlockColorHandler(
							entry.getKey(), L.cast(entry.getValue(), Block.class));
				}
				ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
				for(Entry<IItemColor, List<Block>> entry : itemBlockColorMap.entrySet())
				{
					itemColors.registerItemColorHandler(
							entry.getKey(), L.cast(entry.getValue(), Block.class));
				}
				for(Entry<IItemColor, List<Item>> entry : itemColorMap.entrySet())
				{
					itemColors.registerItemColorHandler(
							entry.getKey(), L.cast(entry.getValue(), Item.class));
				}
			}

			if(loadComplete)
			{
				U.Client.getFontRender().onResourceManagerReload(manager);
			}
		}

		@Override
		public void registerColorMultiplier(IBlockColor color, Block...blocks)
		{
			U.L.put(blockColorMap, color, blocks);
		}

		@Override
		public void registerColorMultiplier(IItemColor color, Block...blocks)
		{
			U.L.put(itemBlockColorMap, color, blocks);
		}

		@Override
		public void registerColorMultiplier(IItemColor color, Item...items)
		{
			U.L.put(itemColorMap, color, items);
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
				U.L.put(registers, U.Mod.getActiveModID(), register);
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
		
		public static void registerBuildInModel(Block block)
		{
			if(Loader.instance().hasReachedState(LoaderState.INITIALIZATION))
			{
				Log.warn("The block '" + block.getRegistryName() + "' register buildin model after initialzation, tried register it early.");
			}
			else
			{
				buildInRender.add(block);
			}
		}

		/**
		 * Internal, do not use. (Use ASM from forge)
		 */
		public static void onRegisterAllBlocks(BlockModelShapes shapes)
		{
			shapes.registerBuiltInBlocks(U.L.cast(buildInRender, Block.class));
			buildInRender = null;
		}
	}
}