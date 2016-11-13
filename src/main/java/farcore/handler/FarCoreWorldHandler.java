package farcore.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import farcore.FarCore;
import farcore.asm.LightFix;
import farcore.lib.block.IExtendedDataBlock;
import farcore.lib.block.IUpdateDelayBlock;
import farcore.lib.net.world.PacketCustomChunkData;
import farcore.lib.util.Log;
import farcore.lib.world.IObjectInWorld;
import farcore.util.U;
import farcore.util.U.L;
import farcore.util.U.Worlds;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

public class FarCoreWorldHandler
{
	private static final int CHUNK_LENGTH = 16 * 256 * 16;
	private static final int[] CACHE = new int[CHUNK_LENGTH];

	private static class NotifyEntry
	{
		int x;
		int y;
		int z;
		BlockPos source;
		IBlockState changedBlock;

		NotifyEntry(IBlockState changed, BlockPos pos)
		{
			this(changed, pos, 0, 0, 0);
		}
		NotifyEntry(IBlockState changed, BlockPos pos, int x, int y, int z)
		{
			changedBlock = changed;
			source = pos;
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		@Override
		public int hashCode()
		{
			return x << 16 ^ y << 8 ^ z;
		}

		@Override
		public boolean equals(Object obj)
		{
			NotifyEntry entry;
			return obj == this ? true :
				!(obj instanceof NotifyEntry) ? false :
					(entry = (NotifyEntry) obj).x == x &&
					entry.y == y &&
					entry.z == z;
		}
	}

	private static final Map<Class<? extends IObjectInWorld>, String> OBJECTS_TO_ID = new HashMap();
	private static final Map<String, Class<? extends IObjectInWorld>> ID_TO_OBJECTS = new HashMap();

	private static final String key = "objsinw";
	private static Map<Integer, List<IObjectInWorld>> objects = new HashMap();
	private static Map<Integer, List<NotifyEntry>> updatePos = new HashMap();
	
	private static Map<Integer, List<IObjectInWorld>> unlistedObjects = new HashMap();
	
	public static void registerObject(String id, Class<? extends IObjectInWorld> clazz)
	{
		OBJECTS_TO_ID.put(clazz, id);
		ID_TO_OBJECTS.put(id, clazz);
	}
	
	public static List<IObjectInWorld> getObjectInRange(World world, BlockPos pos, double range)
	{
		return getObjectInRange(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, range);
	}
	public static List<IObjectInWorld> getObjectInRange(World world, double x, double y, double z, double range)
	{
		double sq = range * range;
		List<IObjectInWorld> list = new ArrayList();
		for(IObjectInWorld obj : Worlds.getListFromWorldDimention(objects, world, false))
		{
			double[] p = obj.position();
			double a;
			double disSQ = (a = p[0] - x) * a + (a = p[1] - y) * a + (a = p[2] - z) * a;
			if(disSQ < sq)
			{
				list.add(obj);
			}
		}
		return list;
	}
	
	public static void putNewObjectInWorld(IObjectInWorld world)
	{
		U.L.put(objects, world.world().provider.getDimension(), world);
	}
	
	public static void markBlockForUpdate(World world, Collection<NotifyEntry> pos)
	{
		U.Worlds.getListFromWorldDimention(updatePos, world, true).addAll(pos);
	}
	public static void markBlockForUpdate(World world, BlockPos pos)
	{
		U.Worlds.getListFromWorldDimention(updatePos, world, true).add(new NotifyEntry(world.getBlockState(pos), pos));
	}

	private boolean notifyFlag = false;

	@SubscribeEvent
	public void onLoad(WorldEvent.Load event)
	{
	}

	@SubscribeEvent
	public void onUnload(WorldEvent.Unload event)
	{
		//Remove all calculation of light.
		LightFix.onWorldUnload(event.getWorld());
		int dim;
		List<IObjectInWorld> list = objects.remove(dim = event.getWorld().provider.getDimension());
		if(list != null)
		{
			U.L.put(unlistedObjects, dim, list);
		}
		updatePos.remove(dim);
	}

	@SubscribeEvent
	public void onLoad(ChunkEvent.Load event)
	{
	}

	@SubscribeEvent
	public void onUnload(ChunkEvent.Unload event)
	{
		int dim;
		int x1 = event.getChunk().xPosition << 4;
		int z1 = event.getChunk().zPosition << 4;
		int x2 = x1 + 16;
		int z2 = z1 + 16;
		List<IObjectInWorld> removed = new ArrayList();
		if(objects.containsKey(dim = event.getWorld().provider.getDimension()))
		{
			for(IObjectInWorld obj : objects.get(dim))
			{
				double[] pos = obj.position();
				if(pos[0] < x2 && pos[0] >= x1 && pos[2] < z2 && pos[2] >= z1)
				{
					removed.add(obj);
				}
			}
		}
		if(!removed.isEmpty())
		{
			objects.get(dim).removeAll(removed);
			U.L.put(unlistedObjects, dim, removed);
		}
	}
	
	@SubscribeEvent
	public void onUpdate(TickEvent.WorldTickEvent event)
	{
		if(event.side == Side.CLIENT) return;
		if(event.phase == Phase.END) return;
		event.world.theProfiler.startSection("update.oiw");
		updateAllObjectInWorld(event.world);
		event.world.theProfiler.endStartSection("update.notified");
		updateNotifiedNeighbours(event.world);
		event.world.theProfiler.endSection();
	}

	@SubscribeEvent
	public void onDataLoad(ChunkDataEvent.Load event)
	{
		if(event.getData().hasKey(key))
		{
			NBTTagCompound nbt = event.getData().getCompoundTag(key);
			loadOIW(event.getWorld(), event.getChunk(), nbt);
			loadBlockState(event.getWorld(), event.getChunk(), nbt);
		}
	}

	@SubscribeEvent
	public void onDataSave(ChunkDataEvent.Save event)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		saveOIW(event.getWorld(), event.getChunk(), nbt);
		saveBlockState(event.getWorld(), event.getChunk(), nbt);
		event.getData().setTag(key, nbt);
	}

	@SubscribeEvent
	public void onPlayerWatch(ChunkWatchEvent.Watch event)
	{
		EntityPlayerMP player = event.getPlayer();
		FarCore.network.sendLargeToPlayer(new PacketCustomChunkData(player.worldObj, event.getChunk(), CACHE), player);
	}
	
	public void loadOIW(World world, Chunk chunk, NBTTagCompound nbt)
	{
		if(world.getWorldType() == WorldType.DEBUG_WORLD) return;
		if(nbt.hasKey("oiw"))
		{
			nbt = nbt.getCompoundTag("oiw");
			for(String tag : nbt.getKeySet())
			{
				if(!ID_TO_OBJECTS.containsKey(tag))
				{
					Log.warn("The tag '" + tag + "' is not register in map.");
					continue;
				}
				Class<? extends IObjectInWorld> clazz = ID_TO_OBJECTS.get(tag);
				NBTTagList list = (NBTTagList) nbt.getTag(tag);
				for(int i = 0; i < list.tagCount(); ++i)
				{
					NBTBase nbt1 = list.get(i);
					try
					{
						IObjectInWorld obj = clazz.getConstructor(World.class).newInstance(world);
						obj.readFromNBT(nbt1);
						L.put(objects, world.provider.getDimension(), obj);
					}
					catch(Exception exception)
					{
						throw new RuntimeException("Fail to create object in world.", exception);
					}
				}
			}
		}
	}

	public void saveOIW(World world, Chunk chunk, NBTTagCompound nbt)
	{
		if(world.getWorldType() == WorldType.DEBUG_WORLD) return;
		int dim = world.provider.getDimension();
		if(objects.containsKey(dim) || unlistedObjects.containsKey(dim))
		{
			List<IObjectInWorld> saves = new ArrayList();
			int x1 = chunk.xPosition << 4;
			int z1 = chunk.zPosition << 4;
			int x2 = x1 + 16;
			int z2 = z1 + 16;
			if(objects.containsKey(dim))
			{
				for(IObjectInWorld obj : objects.get(dim))
				{
					double[] pos = obj.position();
					if(pos[0] < x2 && pos[0] >= x1 && pos[2] < z2 && pos[2] >= z1)
					{
						saves.add(obj);
					}
				}
			}
			if(unlistedObjects.containsKey(dim))
			{
				for(IObjectInWorld obj : unlistedObjects.get(dim))
				{
					double[] pos = obj.position();
					if(pos[0] < x2 && pos[0] >= x1 && pos[2] < z2 && pos[2] >= z1)
					{
						saves.add(obj);
					}
				}
			}
			unlistedObjects.clear();
			if(!saves.isEmpty())
			{
				Map<String, List<NBTBase>> map = new HashMap();
				for(IObjectInWorld obj : saves)
				{
					if(!OBJECTS_TO_ID.containsKey(obj.getClass()))
					{
						Log.warn("The object class '" + obj.getClass() + "' isn't registered to list, will not save it!");
						continue;
					}
					String tag = OBJECTS_TO_ID.get(obj.getClass());
					L.put(map, tag, obj.writeFromNBT());
				}
				NBTTagCompound nbt1 = new NBTTagCompound();
				for(Entry<String, List<NBTBase>> entry : map.entrySet())
				{
					NBTTagList list = new NBTTagList();
					for(NBTBase nbt2 : entry.getValue())
					{
						list.appendTag(nbt2);
					}
					nbt1.setTag(entry.getKey(), list);
				}
				nbt.setTag("oiw", nbt1);
			}
		}
	}

	public void loadBlockState(World world, Chunk chunk, NBTTagCompound nbt)
	{
		if(world.getWorldType() == WorldType.DEBUG_WORLD) return;
		if(nbt.hasKey("edb"))
		{
			int[] array = nbt.getIntArray("edb");
			if(array.length != CHUNK_LENGTH)
				throw new RuntimeException("The block state array length is not valid length, the nbt might broken!");
			int index = 0;
			for(int i = 0; i < 16; ++i)
			{
				ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[i];
				if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE)
				{
					for(int i1 = 0; i1 < 16; ++i1)
					{
						for(int j = 0; j < 16; ++j)
						{
							for(int k = 0; k < 16; ++k)
							{
								IBlockState state = extendedblockstorage.get(j, i1, k);
								if(state.getBlock() instanceof IExtendedDataBlock)
								{
									state = ((IExtendedDataBlock) state.getBlock()).getStateFromData(array[index]);
									extendedblockstorage.set(j, i1, k, state);
								}
								++index;
							}
						}
					}
				}
				else
				{
					index += 0x1000;
				}
			}
		}
	}
	
	public void saveBlockState(World world, Chunk chunk, NBTTagCompound nbt)
	{
		if(world.getWorldType() == WorldType.DEBUG_WORLD) return;
		int[] data = CACHE;
		boolean save = false;
		Arrays.fill(data, 0);
		int index = 0;
		for(int i = 0; i < 16; ++i)
		{
			ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[i];
			if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE)
			{
				for(int i1 = 0; i1 < 16; ++i1)
				{
					for(int j = 0; j < 16; ++j)
					{
						for(int k = 0; k < 16; ++k)
						{
							IBlockState state = extendedblockstorage.get(j, i1, k);
							if(state.getBlock() instanceof IExtendedDataBlock)
							{
								save = true;
								data[index] = ((IExtendedDataBlock) state.getBlock()).getDataFromState(state);
							}
							++index;
						}
					}
				}
			}
			else
			{
				index += 0x1000;
			}
		}
		if(save)
		{
			nbt.setIntArray("edb", data.clone());
		}
	}
	
	private void updateAllObjectInWorld(World world)
	{
		List<IObjectInWorld> list = Worlds.getListFromWorldDimention(objects, world, false);
		for(IObjectInWorld obj : ImmutableList.copyOf(list))
		{
			if(obj.isDead())
			{
				list.remove(obj);
				continue;
			}
			if(obj instanceof ITickable)
			{
				((ITickable) obj).update();
			}
		}
	}
	
	private void updateNotifiedNeighbours(World world)
	{
		List<NotifyEntry> list = updatePos.remove(world.provider.getDimension());
		if(list != null)
		{
			MutableBlockPos pos = new MutableBlockPos();
			for(NotifyEntry entry : list)
			{
				IBlockState state = world.getBlockState(pos.setPos(entry.x, entry.y, entry.z));
				try
				{
					if(state.getBlock() instanceof IUpdateDelayBlock)
					{
						((IUpdateDelayBlock) state.getBlock()).notifyAfterTicking(state, world, pos, entry.changedBlock);
					}
					else
					{
						state.neighborChanged(world, pos, entry.changedBlock.getBlock());
					}
				}
				catch (Throwable throwable)
				{
					Block block = state.getBlock();
					CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
					CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
					crashreportcategory.setDetail("Source block type", () ->
					{
						try
						{
							return String.format("ID #%d (%s // %s)", Block.getIdFromBlock(block), block.getUnlocalizedName(), block.getClass().getCanonicalName());
						}
						catch (Throwable var2)
						{
							return "ID #" + Block.getIdFromBlock(block);
						}
					});
					CrashReportCategory.addBlockInfo(crashreportcategory, pos, state);
					throw new ReportedException(crashreport);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onNotifyNeighbours(NeighborNotifyEvent event)
	{
		if(event.getState().getBlock() instanceof IUpdateDelayBlock)
		{
			event.setCanceled(true);
			if(notifyFlag)
			{
				Set<NotifyEntry> set = new HashSet(event.getNotifiedSides().size());
				for(EnumFacing facing : event.getNotifiedSides())
				{
					set.add(new NotifyEntry(event.getState(), event.getPos(), facing.getFrontOffsetX(), facing.getFrontOffsetY(), facing.getFrontOffsetZ()));
				}
				markBlockForUpdate(event.getWorld(), set);
				return;
			}
			notifyFlag = true;
			IUpdateDelayBlock block = (IUpdateDelayBlock) event.getState().getBlock();
			int range = block.getCheckRange(event.getState());
			int r1 = 2 * range + 1;
			Set<NotifyEntry> set = new HashSet(r1*r1*r1);
			BlockPos pos = event.getPos();
			if(!event.getWorld().isAreaLoaded(pos, range))
				return;
			for(int i = -range; i <= range; ++i)
			{
				for(int j = -range; j <= range; ++j)
				{
					for(int k = -range; k <= range; ++k)
					{
						set.add(new NotifyEntry(event.getState(), pos, pos.getX() + i, pos.getY() + j, pos.getZ() + k));
					}
				}
			}
			markBlockForUpdate(event.getWorld(), set);
			notifyFlag = false;
		}
	}
}