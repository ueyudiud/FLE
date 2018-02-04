/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableList;

import nebula.Log;
import nebula.common.block.IUpdateDelayBlock;
import nebula.common.util.Worlds;
import nebula.common.world.IObjectInWorld;
import nebula.common.world.WorldTask;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
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
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author ueyudiud
 */
public class NebulaWorldHandler
{
	private static class NotifyEntry
	{
		int			x;
		int			y;
		int			z;
		BlockPos	source;
		IBlockState	changedBlock;
		
		NotifyEntry(IBlockState changed, BlockPos pos)
		{
			this(changed, pos, 0, 0, 0);
		}
		
		NotifyEntry(IBlockState changed, BlockPos pos, int x, int y, int z)
		{
			this.changedBlock = changed;
			this.source = pos;
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		@Override
		public int hashCode()
		{
			return this.x << 16 ^ this.y << 8 ^ this.z;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			NotifyEntry entry;
			return obj == this ? true : !(obj instanceof NotifyEntry) ? false : (entry = (NotifyEntry) obj).x == this.x && entry.y == this.y && entry.z == this.z;
		}
	}
	
	private static final Map<Class<? extends IObjectInWorld>, String>	OBJECTS_TO_ID	= new HashMap<>();
	private static final Map<String, Class<? extends IObjectInWorld>>	ID_TO_OBJECTS	= new HashMap<>();
	
	private static final String							key			= "objsinw";
	private static Map<Integer, List<IObjectInWorld>>	objects		= new HashMap<>();
	private static Map<Integer, List<NotifyEntry>>		updatePos	= new HashMap<>();
	private static List<WorldTask>						tasks		= new ArrayList<>();
	
	private static Map<Integer, List<IObjectInWorld>> unlistedObjects = new HashMap<>();
	
	public static void registerObject(String id, Class<? extends IObjectInWorld> clazz)
	{
		OBJECTS_TO_ID.put(clazz, id);
		ID_TO_OBJECTS.put(id, clazz);
	}
	
	public static void schedueTask(WorldTask task)
	{
		tasks.add(task);
	}
	
	public static <T extends WorldTask> List<T> getTasks(World world, Class<T> type, Predicate<? super T> predicate)
	{
		List<T> list = new ArrayList<>();
		for (WorldTask task : tasks)
		{
			if (type.isInstance(task) && predicate.test((T) task))
			{
				list.add((T) task);
			}
		}
		return list;
	}
	
	public static List<IObjectInWorld> getObjectInRange(World world, BlockPos pos, double range)
	{
		return getObjectInRange(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, range);
	}
	
	public static List<IObjectInWorld> getObjectInRange(World world, double x, double y, double z, double range)
	{
		double sq = range * range;
		List<IObjectInWorld> list = new ArrayList<>();
		for (IObjectInWorld obj : Worlds.getListFromWorldDimention(objects, world, false))
		{
			double[] p = obj.position();
			double a;
			double disSQ = (a = p[0] - x) * a + (a = p[1] - y) * a + (a = p[2] - z) * a;
			if (disSQ < sq)
			{
				list.add(obj);
			}
		}
		return list;
	}
	
	public static void putNewObjectInWorld(IObjectInWorld world)
	{
		nebula.common.util.L.put(objects, world.world().provider.getDimension(), world);
	}
	
	public static void markBlockForUpdate(World world, Collection<NotifyEntry> pos)
	{
		synchronized (updatePos)
		{
			Worlds.getListFromWorldDimention(updatePos, world, true).addAll(pos);
		}
	}
	
	public static void markBlockForUpdate(World world, BlockPos pos)
	{
		synchronized (updatePos)
		{
			Worlds.getListFromWorldDimention(updatePos, world, true).add(new NotifyEntry(world.getBlockState(pos), pos));
		}
	}
	
	private boolean notifyFlag = false;
	
	@SubscribeEvent
	public void onLoad(WorldEvent.Load event)
	{
	}
	
	@SubscribeEvent
	public void onUnload(WorldEvent.Unload event)
	{
		// Remove all calculation of light.
		int dim;
		List<IObjectInWorld> list = objects.remove(dim = event.getWorld().provider.getDimension());
		if (list != null)
		{
			nebula.common.util.L.put(unlistedObjects, dim, list);
		}
		tasks.removeIf(task -> task.world.provider.getDimension() == dim);
		synchronized (updatePos)
		{
			updatePos.remove(dim);
		}
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
		List<IObjectInWorld> removed = new ArrayList<>();
		if (objects.containsKey(dim = event.getWorld().provider.getDimension()))
		{
			for (IObjectInWorld obj : objects.get(dim))
			{
				double[] pos = obj.position();
				if (pos[0] < x2 && pos[0] >= x1 && pos[2] < z2 && pos[2] >= z1)
				{
					removed.add(obj);
				}
			}
		}
		if (!removed.isEmpty())
		{
			objects.get(dim).removeAll(removed);
			nebula.common.util.L.put(unlistedObjects, dim, removed);
		}
	}
	
	@SubscribeEvent
	public void onUpdate(TickEvent.WorldTickEvent event)
	{
		if (event.side == Side.CLIENT) return;
		if (event.phase == Phase.END) return;
		event.world.theProfiler.startSection("update.oiw");
		updateAllObjectInWorld(event.world);
		event.world.theProfiler.endStartSection("update.notified");
		updateNotifiedNeighbours(event.world);
		event.world.theProfiler.endStartSection("update.custom.tasks");
		updateTasks(event.world);
		event.world.theProfiler.endSection();
	}
	
	@SubscribeEvent
	public void onDataLoad(ChunkDataEvent.Load event)
	{
		if (event.getData().hasKey(key))
		{
			NBTTagCompound nbt = event.getData().getCompoundTag(key);
			loadOIW(event.getWorld(), event.getChunk(), nbt);
		}
	}
	
	@SubscribeEvent
	public void onDataSave(ChunkDataEvent.Save event)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		saveOIW(event.getWorld(), event.getChunk(), nbt);
		event.getData().setTag(key, nbt);
	}
	
	public void loadOIW(World world, Chunk chunk, NBTTagCompound nbt)
	{
		if (world.getWorldType() == WorldType.DEBUG_WORLD) return;
		if (nbt.hasKey("oiw"))
		{
			nbt = nbt.getCompoundTag("oiw");
			for (String tag : nbt.getKeySet())
			{
				if (!ID_TO_OBJECTS.containsKey(tag))
				{
					Log.warn("The tag '" + tag + "' is not register in map.");
					continue;
				}
				Class<? extends IObjectInWorld> clazz = ID_TO_OBJECTS.get(tag);
				NBTTagList list = (NBTTagList) nbt.getTag(tag);
				for (int i = 0; i < list.tagCount(); ++i)
				{
					NBTBase nbt1 = list.get(i);
					try
					{
						IObjectInWorld obj = clazz.getConstructor(World.class).newInstance(world);
						obj.readFromNBT(nbt1);
						nebula.common.util.L.put(objects, world.provider.getDimension(), obj);
					}
					catch (Exception exception)
					{
						throw new RuntimeException("Fail to create object in world.", exception);
					}
				}
			}
		}
	}
	
	public void saveOIW(World world, Chunk chunk, NBTTagCompound nbt)
	{
		if (world.getWorldType() == WorldType.DEBUG_WORLD) return;
		int dim = world.provider.getDimension();
		if (objects.containsKey(dim) || unlistedObjects.containsKey(dim))
		{
			List<IObjectInWorld> saves = new ArrayList<>();
			int x1 = chunk.xPosition << 4;
			int z1 = chunk.zPosition << 4;
			int x2 = x1 + 16;
			int z2 = z1 + 16;
			if (objects.containsKey(dim))
			{
				for (IObjectInWorld obj : objects.get(dim))
				{
					double[] pos = obj.position();
					if (pos[0] < x2 && pos[0] >= x1 && pos[2] < z2 && pos[2] >= z1)
					{
						saves.add(obj);
					}
				}
			}
			if (unlistedObjects.containsKey(dim))
			{
				for (IObjectInWorld obj : unlistedObjects.get(dim))
				{
					double[] pos = obj.position();
					if (pos[0] < x2 && pos[0] >= x1 && pos[2] < z2 && pos[2] >= z1)
					{
						saves.add(obj);
					}
				}
			}
			unlistedObjects.clear();
			if (!saves.isEmpty())
			{
				Map<String, List<NBTBase>> map = new HashMap<>();
				for (IObjectInWorld obj : saves)
				{
					if (!OBJECTS_TO_ID.containsKey(obj.getClass()))
					{
						Log.warn("The object class '" + obj.getClass() + "' isn't registered to list, will not save it!");
						continue;
					}
					String tag = OBJECTS_TO_ID.get(obj.getClass());
					nebula.common.util.L.put(map, tag, obj.writeFromNBT());
				}
				NBTTagCompound nbt1 = new NBTTagCompound();
				for (Entry<String, List<NBTBase>> entry : map.entrySet())
				{
					NBTTagList list = new NBTTagList();
					for (NBTBase nbt2 : entry.getValue())
					{
						list.appendTag(nbt2);
					}
					nbt1.setTag(entry.getKey(), list);
				}
				nbt.setTag("oiw", nbt1);
			}
		}
	}
	
	private void updateTasks(World world)
	{
		Iterator<WorldTask> tasks$itr = tasks.iterator();
		while (tasks$itr.hasNext())
		{
			WorldTask task = tasks$itr.next();
			if (task.handleTask())
			{
				tasks$itr.remove();
			}
		}
	}
	
	private void updateAllObjectInWorld(World world)
	{
		List<IObjectInWorld> list = Worlds.getListFromWorldDimention(objects, world, false);
		for (IObjectInWorld obj : ImmutableList.copyOf(list))
		{
			if (obj.isDead())
			{
				list.remove(obj);
				continue;
			}
			if (obj instanceof ITickable)
			{
				((ITickable) obj).update();
			}
		}
	}
	
	private void updateNotifiedNeighbours(World world)
	{
		List<NotifyEntry> list;
		synchronized (updatePos)
		{
			list = updatePos.remove(world.provider.getDimension());
		}
		if (list != null)
		{
			MutableBlockPos pos = new MutableBlockPos();
			for (NotifyEntry entry : list)
			{
				IBlockState state = world.getBlockState(pos.setPos(entry.x, entry.y, entry.z));
				try
				{
					if (state.getBlock() instanceof IUpdateDelayBlock)
					{
						((IUpdateDelayBlock) state.getBlock()).notifyAfterTicking(state, world, pos.toImmutable(), entry.changedBlock);
					}
					else
					{
						state.neighborChanged(world, pos.toImmutable(), entry.changedBlock.getBlock());
					}
				}
				catch (Throwable throwable)
				{
					Block block = state.getBlock();
					CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
					CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
					crashreportcategory.setDetail("Source block type", () -> {
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
		if (event.getState().getBlock() instanceof IUpdateDelayBlock)
		{
			event.setCanceled(true);
			if (this.notifyFlag)
			{
				Set<NotifyEntry> set = new HashSet<>(event.getNotifiedSides().size());
				for (EnumFacing facing : event.getNotifiedSides())
				{
					set.add(new NotifyEntry(event.getState(), event.getPos(), facing.getFrontOffsetX(), facing.getFrontOffsetY(), facing.getFrontOffsetZ()));
				}
				markBlockForUpdate(event.getWorld(), set);
				return;
			}
			this.notifyFlag = true;
			IUpdateDelayBlock block = (IUpdateDelayBlock) event.getState().getBlock();
			int range = block.getCheckRange(event.getState());
			int r1 = 2 * range + 1;
			Set<NotifyEntry> set = new HashSet<>(r1 * r1 * r1);
			BlockPos pos = event.getPos();
			if (!event.getWorld().isAreaLoaded(pos, range)) return;
			for (int i = -range; i <= range; ++i)
			{
				for (int j = -range; j <= range; ++j)
				{
					for (int k = -range; k <= range; ++k)
					{
						set.add(new NotifyEntry(event.getState(), pos, pos.getX() + i, pos.getY() + j, pos.getZ() + k));
					}
				}
			}
			markBlockForUpdate(event.getWorld(), set);
			this.notifyFlag = false;
		}
	}
}
