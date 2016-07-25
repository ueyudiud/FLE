package farcore;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

import farcore.data.EnumBlock;
import farcore.data.EnumItem;
import farcore.data.M;
import farcore.lib.block.instance.BlockFire;
import farcore.lib.block.instance.BlockSapling;
import farcore.lib.block.instance.BlockWater;
import farcore.lib.entity.EntityFallingBlockExtended;
import farcore.lib.fluid.FluidWater;
import farcore.lib.item.ItemDebugger;
import farcore.lib.model.block.ModelSapling;
import farcore.lib.model.entity.RenderFallingBlockExt;
import farcore.lib.net.PacketKey;
import farcore.lib.net.entity.PacketEntity;
import farcore.lib.net.entity.PacketEntityAsk;
import farcore.lib.net.tile.PacketTEAsk;
import farcore.lib.net.tile.PacketTESAskRender;
import farcore.lib.net.tile.PacketTESync;
import farcore.lib.tile.instance.TECoreLeaves;
import farcore.lib.tile.instance.TESapling;
import farcore.lib.util.CreativeTabBase;
import farcore.lib.util.LanguageManager;
import farcore.lib.util.Log;
import farcore.network.Network;
import farcore.util.U;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;
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
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = FarCore.ID, version = "1.0", name = "Far Core")
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
	}

	@EventHandler
	public void check(FMLFingerprintViolationEvent event)
	{
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
			if(FarCore.OVERRIDE_ID.equals(container.getName()))
			{
				modMetadata.childMods.add(container);
				break;
			}
		try
		{
			lang = new LanguageManager(new File(U.Mod.getMCFile(), "lang"));
			Log.info("Loading configuration.");
			File file = event.getSuggestedConfigurationFile();
			Configuration configuration = new Configuration(file);
			configuration.load();
			//			V.init(configuration);
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
		//		FarCoreKeyHandler.register(V.keyPlace, Keyboard.KEY_P);
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

	public static class Proxy
	{
		public void load(FMLPreInitializationEvent event)
		{
			FarCore.tabResourceBlock = new CreativeTabBase("farcore.resource.block", "Far Resource Block")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(M.peridotite.rock, 1, 2);
				}
			};
			//			FarCore.tabResourceItem = new CreativeTabBase("farcore.resource.item", "Far Resource Item")
			//			{
			//				@Override
			//				public ItemStack getIconItemStack()
			//				{
			//					return new ItemStack(EnumItem.stone_chip.item, 1, M.peridotite.id);
			//				}
			//			};
			FarCore.tabMachine = new CreativeTabBase("farcore.machine", "Far Machine")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(Blocks.CRAFTING_TABLE);
				}
			};
			FarCore.tabMaterial = new CreativeTabBase("farcore.material", "Far Material")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(Items.EMERALD);
				}
			};
			FarCore.tabTool = new CreativeTabBase("farcore.tool", "Far Tool")
			{
				@Override
				public ItemStack getIconItemStack()
				{
					return new ItemStack(EnumItem.debug.item);
				}
			};
			M.init();

			new ItemDebugger().setCreativeTab(FarCore.tabTool);
			//			new ItemTreeLog().setTextureName("grouped/log").setCreativeTab(FarCore.tabResourceItem);
			//			new ItemFluidDisplay();
			//			new ItemStoneChip().setCreativeTab(FarCore.tabResourceItem);
			new BlockSapling().setCreativeTab(FarCore.tabResourceBlock);
			//			new BlockCrop();
			new BlockFire();
			new BlockWater(new FluidWater("pure.water", "Pure Water", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow")));
			GameRegistry.registerTileEntity(TESapling.class, "farcore.sapling");
			GameRegistry.registerTileEntity(TECoreLeaves.class, "farcore.core.leaves");
			//			GameRegistry.registerTileEntity(TECrop.class, "farcore.crop");
			int id = 0;
			EntityRegistry.registerModEntity(EntityFallingBlockExtended.class, "fle.falling.block", id++, FarCore.ID, 32, 20, true);
			//			EntityRegistry.registerModEntity(EntityProjectileItem.class, "fle.projectile", id++, FarCore.ID, 32, 20, true);
		}

		public void load(FMLInitializationEvent event)
		{
			LanguageManager.registerLocal("info.debug.date", "Date : ");
			LanguageManager.registerLocal("info.log.length", "Legnth : %d");
			FarCore.network = Network.network(FarCore.ID);
			//			FarCore.network.registerPacket(PacketGuiAction.class, Side.SERVER);
			FarCore.network.registerPacket(PacketEntity.class, Side.CLIENT);
			FarCore.network.registerPacket(PacketEntityAsk.class, Side.SERVER);
			FarCore.network.registerPacket(PacketKey.class, Side.SERVER);
			//			FarCore.network.registerPacket(PacketPlayerStatUpdate.class, Side.CLIENT);

			FarCore.network.registerPacket(PacketTESync.class, Side.CLIENT);
			FarCore.network.registerPacket(PacketTESAskRender.class, Side.CLIENT);
			FarCore.network.registerPacket(PacketTEAsk.class, Side.SERVER);
		}

		public void load(FMLPostInitializationEvent event)
		{

		}

		public void load(FMLLoadCompleteEvent event)
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
		}

		@Override
		public void load(FMLPreInitializationEvent event)
		{
			super.load(event);
			U.Mod.registerItemModel(EnumItem.debug.item, 0, FarCore.ID, "debugger");
			RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlockExtended.class, RenderFallingBlockExt.Factory.instance);
			//			RenderingRegistry.registerEntityRenderingHandler(EntityProjectileItem.class, new RenderProjectileItem());
			//			MinecraftForge.EVENT_BUS.register(new FarCoreGuiHandler());
			ModelLoaderRegistry.registerLoader(ModelSapling.Loader.instance);
			ModelLoader.setCustomStateMapper(EnumBlock.sapling.block, ModelSapling.BlockModelSelector.instance);
			U.Mod.registerCustomItemModelSelector(Item.getItemFromBlock(EnumBlock.sapling.block), ModelSapling.ItemModelSelector.instance);
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
		}

		@Override
		public void load(FMLLoadCompleteEvent event)
		{
			super.load(event);
			loadComplete = true;
		}

		@Override
		public void onResourceManagerReload(IResourceManager manager)
		{
			
		}
	}
}