/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.asm;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import farcore.FarCore;
import farcore.lib.world.chunk.BlockStateContainerExt;
import farcore.lib.world.chunk.ExtendedBlockStateRegister;
import farcore.util.NBTs;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.WorldAccessContainer;
import net.minecraftforge.fml.common.event.FMLModIdMappingEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.10.2")
public class FarOverride extends DummyModContainer implements WorldAccessContainer
{
	public static boolean isFleLoad;
	
	@Instance(FarCore.OVERRIDE_ID)
	public static FarOverride override;
	
	public static final String VERSION = "1.1";
	
	public FarOverride()
	{
		super(new ModMetadata());
		override = this;
		ModMetadata meta = getMetadata();
		meta.modId = FarCore.OVERRIDE_ID;
		meta.name = "Far Override";
		meta.version = VERSION;
		meta.credits = "ueyudiud";
		meta.authorList = Arrays.asList("ueyudiud");
		meta.description = "Far land era child mod, use to override minecraft objects.";
		meta.screenshots = new String[0];
		meta.logoFile = "";
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller)
	{
		bus.register(this);
		return true;
	}
	
	@Subscribe
	public void mappingChanged(FMLModIdMappingEvent evt)
	{
		ExtendedBlockStateRegister.INSTANCE.buildStateMap();
	}
	
	@EventHandler
	public void load(FMLPreInitializationEvent event)
	{
		isFleLoad = U.Mod.isModLoaded("Far Land Era");
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
		boolean flag = !worldIn.provider.getHasNoSky();
		
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
		boolean flag = !worldIn.provider.getHasNoSky();
		
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
				nbttagcompound1.setInteger("t", (int)(nextticklistentry.scheduledTime - j));
				nbttagcompound1.setInteger("p", nextticklistentry.priority);
				nbttaglist3.appendTag(nbttagcompound1);
			}
			
			compound.setTag("TileTicks", nbttaglist3);
		}
	}
}