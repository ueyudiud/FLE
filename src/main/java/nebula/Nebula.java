/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula;

import static nebula.common.LanguageManager.registerLocal;
import static net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.mojang.realmsclient.gui.ChatFormatting;

import nebula.client.CreativeTabBase;
import nebula.client.light.LightFix;
import nebula.common.CommonProxy;
import nebula.common.LanguageManager;
import nebula.common.NebulaConfig;
import nebula.common.NebulaItemHandler;
import nebula.common.NebulaKeyHandler;
import nebula.common.NebulaPlayerHandler;
import nebula.common.NebulaWorldHandler;
import nebula.common.block.BlockBase;
import nebula.common.entity.EntityFallingBlockExtended;
import nebula.common.entity.EntityProjectileItem;
import nebula.common.item.ItemBase;
import nebula.common.item.ItemFluidDisplay;
import nebula.common.network.Network;
import nebula.common.network.packet.PacketBreakBlock;
import nebula.common.network.packet.PacketChunkNetData;
import nebula.common.network.packet.PacketEntity;
import nebula.common.network.packet.PacketEntityAsk;
import nebula.common.network.packet.PacketFluidSlotClick;
import nebula.common.network.packet.PacketFluidUpdateAll;
import nebula.common.network.packet.PacketFluidUpdateSingle;
import nebula.common.network.packet.PacketGuiTickUpdate;
import nebula.common.network.packet.PacketKey;
import nebula.common.network.packet.PacketTEAsk;
import nebula.common.network.packet.PacketTESAsk;
import nebula.common.network.packet.PacketTESync;
import nebula.common.util.Game;
import nebula.common.util.NBTs;
import nebula.common.world.chunk.BlockStateContainerExt;
import nebula.common.world.chunk.ExtendedBlockStateRegister;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.WorldAccessContainer;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLModIdMappingEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Nebula, is a Minecraft modification provide more useful
 * library for mod, also changed some game rule for more
 * compact to other mods.
 * 
 * @author ueyudiud
 */
@IFMLLoadingPlugin.MCVersion("1.10.2")
public class Nebula extends DummyModContainer implements WorldAccessContainer
{
	/** The minimum forge version far core required. */
	public static final int MIN_FORGE = 2122;
	
	public static final String MODID = "nebula";
	public static final String NAME = "Nebula";
	public static final String VERSION = "0.7";
	
	public static final String INNER_RENDER = "nebula_inner";
	
	@Instance(MODID)
	public static Nebula instance;
	/**
	 * The debug mode flag, enable to switch to
	 * debug mode.<br>
	 * The debug mode will give more information
	 * of game.<br>
	 */
	public static boolean debug = false;
	/**
	 * The network instance of nebula mod.
	 */
	public static Network network;
	
	@SidedProxy(serverSide = "nebula.common.CommonProxy", clientSide = "nebula.client.ClientProxy")
	public static CommonProxy proxy;
	
	private LanguageManager lang;
	
	public static CreativeTabs tabFluids;
	
	public Nebula()
	{
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = MODID;
		meta.name = NAME;
		meta.version = VERSION;
		meta.credits = "ueyudiud";
		meta.authorList = Arrays.asList("ueyudiud");
		meta.description = "Nebula core.";
		meta.logoFile = "/assets/nebula/textures/logo.png";
		Log.logger = LogManager.getLogger(Nebula.NAME);
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller)
	{
		bus.register(this);
		return true;
	}
	
	@Subscribe
	public void check(FMLFingerprintViolationEvent event)
	{
		/**
		 * The Far Core mod used Java8. There are method
		 * is added in Java8, so it is checked by a type
		 * exist since Java8.
		 */
		Log.info("Nebula start check java version...");
		try
		{
			Function function = arg -> null;
		}
		catch(Exception exception)
		{
			throw new RuntimeException("Java version is out of date, please use java 8 to launch.", exception);
		}
		/**
		 * Coded checking.
		 */
		Log.info("Nebula checking mod version...");
		try
		{
			new BlockPos(1, 2, 3).add(0, 0, 0);
		}
		catch(Exception exception)
		{
			throw new RuntimeException("You may download dev version, please check your mod version and use default version.", exception);
		}
		/**
		 * Checking forge version.
		 */
		Log.info("Nebula checking forge version...");
		int forge = ForgeVersion.getBuildVersion();
		if ((forge > 0) && (forge < MIN_FORGE))
			throw new RuntimeException("The currently installed version of "
					+ "Minecraft Forge (" + ForgeVersion.getMajorVersion() + "." + ForgeVersion.getMinorVersion() + "." +
					ForgeVersion.getRevisionVersion() + "." + forge + ") is out of data.\n" +
					"Please update the Minecraft Forge.\n" + "\n" +
					"(Technical information: " + forge + " < " + MIN_FORGE + ")");
		Log.info("Checking end.");
	}
	
	@Subscribe
	public void load(FMLPreInitializationEvent event)
	{
		try
		{
			this.lang = new LanguageManager(new File(Game.getMCFile(), "lang"));
			Configuration configuration = new Configuration(event.getSuggestedConfigurationFile());
			configuration.load();
			NebulaConfig.init(configuration);
			configuration.save();
		}
		catch (Exception exception)
		{
			throw exception;
		}
		this.lang.read();
		
		MinecraftForge.EVENT_BUS.register(new NebulaKeyHandler());
		MinecraftForge.EVENT_BUS.register(new NebulaPlayerHandler());
		MinecraftForge.EVENT_BUS.register(new NebulaWorldHandler());
		MinecraftForge.EVENT_BUS.register(new NebulaItemHandler());
		
		if(NebulaConfig.displayFluidInTab)
		{
			tabFluids = new CreativeTabBase("nebula.fluids", "Fluids[Nebula]", () -> new ItemStack(Items.WATER_BUCKET));
		}
		new ItemFluidDisplay().setCreativeTab(tabFluids);
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
		registerLocal("info.shift.click", ChatFormatting.WHITE + "Press " + ChatFormatting.ITALIC + "<%s>" + ChatFormatting.RESET + " to get more information.");
		registerLocal("info.food.label", ChatFormatting.RED + "Food Stat:");
		registerLocal("info.food.display", ChatFormatting.RED + "F-%s S-%s W-%s");
		network = Network.network(Nebula.MODID);
		network.registerPacket(PacketEntity.class, Side.CLIENT);
		network.registerPacket(PacketEntityAsk.class, Side.SERVER);
		network.registerPacket(PacketKey.class, Side.SERVER);
		network.registerPacket(PacketTESync.class, Side.CLIENT);
		network.registerPacket(PacketTESAsk.class, Side.CLIENT);
		network.registerPacket(PacketTEAsk.class, Side.SERVER);
		network.registerPacket(PacketBreakBlock.class, Side.CLIENT);
		network.registerPacket(PacketFluidUpdateAll.class, Side.CLIENT);
		network.registerPacket(PacketFluidUpdateSingle.class, Side.CLIENT);
		network.registerPacket(PacketFluidSlotClick.class, Side.SERVER);
		network.registerPacket(PacketGuiTickUpdate.class, Side.SERVER);
		network.registerPacket(PacketChunkNetData.class, Side.CLIENT);
	}
	
	@Subscribe
	public void load(FMLLoadCompleteEvent event)
	{
		this.lang.write();
		//Start light thread.
		if(NebulaConfig.multiThreadLight)
		{
			LightFix.startThread();
		}
	}
	
	@Subscribe
	public void mappingChanged(FMLModIdMappingEvent evt)
	{
		ExtendedBlockStateRegister.INSTANCE.buildStateMap();
	}
	
	@Override
	public NBTTagCompound getDataForWriting(SaveHandler handler, WorldInfo info)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		return nbt;
	}
	
	@Override
	public void readData(SaveHandler handler, WorldInfo info, Map<String, NBTBase> propertyMap, NBTTagCompound tag)
	{
	}
	
	public static Chunk loadChunkData(World worldIn, NBTTagCompound compound)
	{
		int i = compound.getInteger("xPos");
		int j = compound.getInteger("zPos");
		Chunk chunk = new Chunk(worldIn, i, j);
		
		chunk.setHeightMap(compound.getIntArray("HeightMap"));
		chunk.setTerrainPopulated(compound.getBoolean("TerrainPopulated"));
		chunk.setLightPopulated(compound.getBoolean("LightPopulated"));
		chunk.setInhabitedTime(compound.getLong("InhabitedTime"));
		
		boolean flag1 = compound.getBoolean("Marker");
		
		NBTTagList list = compound.getTagList("Sections", 10);
		int k = 16;
		ExtendedBlockStorage[] aextendedblockstorage = new ExtendedBlockStorage[16];
		boolean flag = !worldIn.provider.hasNoSky();
		
		for (int l = 0; l < list.tagCount(); ++l)
		{
			NBTTagCompound compound2 = list.getCompoundTagAt(l);
			int i1 = compound2.getByte("Y");
			ExtendedBlockStorage storage = new ExtendedBlockStorage(i1 << 4, flag);
			if(flag1)
			{
				int[] stores = NBTs.getIntArrayOrDefault(compound2, "BlockStates", null);
				((BlockStateContainerExt) storage.getData()).setDataFromNBT(stores);
			}
			else
			{
				byte[] abyte = compound2.getByteArray("Blocks");
				NibbleArray nibblearray = new NibbleArray(compound2.getByteArray("Data"));
				NibbleArray nibblearray1 = compound2.hasKey("Add", 7) ? new NibbleArray(compound2.getByteArray("Add")) : null;
				storage.getData().setDataFromNBT(abyte, nibblearray, nibblearray1);
			}
			storage.setBlocklightArray(new NibbleArray(compound2.getByteArray("BlockLight")));
			
			if (flag)
			{
				storage.setSkylightArray(new NibbleArray(compound2.getByteArray("SkyLight")));
			}
			
			storage.removeInvalidBlocks();
			aextendedblockstorage[i1] = storage;
		}
		
		chunk.setStorageArrays(aextendedblockstorage);
		
		if (compound.hasKey("Biomes", 7))
		{
			chunk.setBiomeArray(compound.getByteArray("Biomes"));
		}
		
		// End this method here and split off entity loading to another method
		return chunk;
	}
	
	public static void saveChunkData(Chunk chunkIn, World worldIn, NBTTagCompound compound)
	{
		compound.setInteger("xPos", chunkIn.xPosition);
		compound.setInteger("zPos", chunkIn.zPosition);
		compound.setLong("LastUpdate", worldIn.getTotalWorldTime());
		compound.setIntArray("HeightMap", chunkIn.getHeightMap());
		compound.setBoolean("TerrainPopulated", chunkIn.isTerrainPopulated());
		compound.setBoolean("LightPopulated", chunkIn.isLightPopulated());
		compound.setLong("InhabitedTime", chunkIn.getInhabitedTime());
		compound.setBoolean("Marker", true);//Mark the saver is FarCore.
		ExtendedBlockStorage[] aextendedblockstorage = chunkIn.getBlockStorageArray();
		NBTTagList list = new NBTTagList();
		boolean flag = !worldIn.provider.hasNoSky();
		
		for (ExtendedBlockStorage extendedblockstorage : aextendedblockstorage)
		{
			if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE)
			{
				NBTTagCompound compound2 = new NBTTagCompound();
				compound2.setByte("Y", (byte)(extendedblockstorage.getYLocation() >> 4 & 255));
				compound2.setIntArray("BlockStates", ((BlockStateContainerExt) extendedblockstorage.getData()).getDatasToNBT());
				compound2.setByteArray("BlockLight", extendedblockstorage.getBlocklightArray().getData());
				
				if (flag)
				{
					compound2.setByteArray("SkyLight", extendedblockstorage.getSkylightArray().getData());
				}
				else
				{
					compound2.setByteArray("SkyLight", new byte[extendedblockstorage.getBlocklightArray().getData().length]);
				}
				
				list.appendTag(compound2);
			}
		}
		
		compound.setTag("Sections", list);
		compound.setByteArray("Biomes", chunkIn.getBiomeArray());
		chunkIn.setHasEntities(false);
		NBTTagList nbttaglist1 = new NBTTagList();
		
		for (int i = 0; i < chunkIn.getEntityLists().length; ++i)
		{
			for (Entity entity : chunkIn.getEntityLists()[i])
			{
				NBTTagCompound nbttagcompound2 = new NBTTagCompound();
				
				if (entity.writeToNBTOptional(nbttagcompound2))
				{
					try
					{
						chunkIn.setHasEntities(true);
						nbttaglist1.appendTag(nbttagcompound2);
					}
					catch (Exception e)
					{
						net.minecraftforge.fml.common.FMLLog.log(org.apache.logging.log4j.Level.ERROR, e,
								"An Entity type %s has thrown an exception trying to write state. It will not persist. Report this to the mod author",
								entity.getClass().getName());
					}
				}
			}
		}
		
		compound.setTag("Entities", nbttaglist1);
		NBTTagList nbttaglist2 = new NBTTagList();
		
		for (TileEntity tileentity : chunkIn.getTileEntityMap().values())
		{
			try
			{
				NBTTagCompound nbttagcompound3 = tileentity.writeToNBT(new NBTTagCompound());
				nbttaglist2.appendTag(nbttagcompound3);
			}
			catch (Exception e)
			{
				net.minecraftforge.fml.common.FMLLog.log(org.apache.logging.log4j.Level.ERROR, e,
						"A TileEntity type %s has throw an exception trying to write state. It will not persist. Report this to the mod author",
						tileentity.getClass().getName());
			}
		}
		
		compound.setTag("TileEntities", nbttaglist2);
		List<NextTickListEntry> list1 = worldIn.getPendingBlockUpdates(chunkIn, false);
		
		if (list1 != null)
		{
			long j = worldIn.getTotalWorldTime();
			NBTTagList nbttaglist3 = new NBTTagList();
			
			for (NextTickListEntry nextticklistentry : list1)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(nextticklistentry.getBlock());
				nbttagcompound1.setString("i", resourcelocation == null ? "" : resourcelocation.toString());
				nbttagcompound1.setInteger("x", nextticklistentry.position.getX());
				nbttagcompound1.setInteger("y", nextticklistentry.position.getY());
				nbttagcompound1.setInteger("z", nextticklistentry.position.getZ());
				nbttagcompound1.setInteger("t", (int) (nextticklistentry.scheduledTime - j));
				nbttagcompound1.setInteger("p", nextticklistentry.priority);
				nbttaglist3.appendTag(nbttagcompound1);
			}
			
			compound.setTag("TileTicks", nbttaglist3);
		}
	}
}