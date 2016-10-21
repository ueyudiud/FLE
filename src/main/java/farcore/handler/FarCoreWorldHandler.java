package farcore.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;

import farcore.asm.LightFix;
import farcore.lib.util.Log;
import farcore.lib.world.IObjectInWorld;
import farcore.util.U;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

public class FarCoreWorldHandler
{
	private static final Map<Class<? extends IObjectInWorld>, String> OBJECTS_TO_ID = new HashMap();
	private static final Map<String, Class<? extends IObjectInWorld>> ID_TO_OBJECTS = new HashMap();

	private static final String key = "objsinw";
	private static Map<Integer, List<IObjectInWorld>> objects = new HashMap();
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
		int id = world.provider.getDimension();
		double sq = range * range;
		if(objects.containsKey(id))
		{
			List<IObjectInWorld> list = new ArrayList();
			for(IObjectInWorld obj : objects.get(id))
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
		return ImmutableList.of();
	}
	
	public static void putNewObjectInWorld(IObjectInWorld world)
	{
		U.L.put(objects, world.world().provider.getDimension(), world);
	}

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

		if(objects.containsKey(event.world.provider.getDimension()))
		{
			List<IObjectInWorld> list = objects.get(event.world.provider.getDimension());
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
	}

	@SubscribeEvent
	public void onDataLoad(ChunkDataEvent.Load event)
	{
		if(event.getData().hasKey(key))
		{
			NBTTagCompound nbt = event.getData().getCompoundTag(key);
			if(!nbt.hasNoTags())
			{
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
							IObjectInWorld obj = clazz.getConstructor(World.class).newInstance(event.getWorld());
							obj.readFromNBT(nbt1);
							U.L.put(objects, event.getWorld().provider.getDimension(), obj);
						}
						catch(Exception exception)
						{
							throw new RuntimeException("Fail to create object in world.", exception);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onDataSave(ChunkDataEvent.Save event)
	{
		int dim = event.getWorld().provider.getDimension();
		if(objects.containsKey(dim) || unlistedObjects.containsKey(dim))
		{
			List<IObjectInWorld> saves = new ArrayList();
			int x1 = event.getChunk().xPosition << 4;
			int z1 = event.getChunk().zPosition << 4;
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
					U.L.put(map, tag, obj.writeFromNBT());
				}
				NBTTagCompound nbt = new NBTTagCompound();
				for(Entry<String, List<NBTBase>> entry : map.entrySet())
				{
					NBTTagList list = new NBTTagList();
					for(NBTBase nbt2 : entry.getValue())
					{
						list.appendTag(nbt2);
					}
					nbt.setTag(entry.getKey(), list);
				}
				event.getData().setTag(key, nbt);
			}
		}
	}
}