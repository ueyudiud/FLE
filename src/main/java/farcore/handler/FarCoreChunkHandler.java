package farcore.handler;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import farcore.lib.collection.IRegister;
import farcore.lib.collection.IntArray;
import farcore.lib.collection.Register;
import farcore.util.U;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

public class FarCoreChunkHandler
{
	private static final String saveName = "djeo";
	private static final IRegister<ChunkConfigHandler> register = new Register();
	
	private static final Map EMPTY_MAP = new AbstractMap()
	{
		@Override
		public int size() 
		{
			return 0;
		}
		
		@Override
		public Set<Entry> entrySet()
		{
			return ImmutableSet.of();
		}

		@Override
		public Object get(Object key) 
		{
			return null;
		}
		
		@Override
		public boolean containsKey(Object key)
		{
			return false;
		}
		
		@Override
		public Object remove(Object key)
		{
			return null;
		}
	};
	
	private static Map<Integer, Map<IntArray, Cache>> cacheMap = new HashMap();
	private static Map<Integer, Map<IntArray, Cache>> secondLoadMap = new HashMap();
	
	public static void registerConfigHandler(ChunkConfigHandler handler)
	{
		register.register(handler.getHandlerName(), handler);
	}

	public static String getChunkCountInfo(World world)
	{
		int id = world.provider.dimensionId;
		return "ch state : " + getChunkMap(cacheMap, id, false).size() + "/" + getChunkMap(secondLoadMap, id, false).size();
	}
	
	public static <T> T getInfoAtBlockCoord(World world, int x, int z, ChunkConfigHandler<T> handler)
	{
		return getInfoAtChunkCoord(world, x >> 4, z >> 4, handler, false, false);
	}
	
	public static <T> T getInfoAtChunkCoord(World world, int x, int z, ChunkConfigHandler<T> handler, boolean ignoreExit, boolean putIn)
	{
		if(putIn)
		{
			int id = world.provider.dimensionId;
			Map<IntArray, Cache> map = null;
			if(!cacheMap.containsKey(id))
			{
				cacheMap.put(id, map = new HashMap());
			}
			else
			{
				map = cacheMap.get(id);
			}
			IntArray array = new IntArray(new int[]{x, z});
			if(!map.containsKey(array))
			{
				if(ignoreExit || world.blockExists(x << 4, 0, z << 4))
				{
					Chunk chunk = world.getChunkFromChunkCoords(x, z);
					Cache cache;
					map.put(array, cache = new Cache(world, chunk));
					return (T) cache.container.get(handler.getHandlerName());
				}
				return null;
			}
			return (T) map.get(array).container.get(handler.getHandlerName());
		}
		else
		{
			int id = world.provider.dimensionId;
			Map<IntArray, Cache> map = null;
			if(!cacheMap.containsKey(id)) return null;
			map = cacheMap.get(id);
			IntArray array = new IntArray(new int[]{x, z});
			if(!map.containsKey(array))
			{
				if(ignoreExit || world.blockExists(x << 4, 0, z << 4))
				{
					Chunk chunk = world.getChunkFromChunkCoords(x, z);
					return (T) new Cache(world, chunk).container.get(handler.getHandlerName());
				}
				return null;
			}
			return (T) map.get(array).container.get(handler.getHandlerName());
		}
	}
	
	private long lastActived = System.currentTimeMillis();
	
	private IntArray cache = new IntArray(2);
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event)
	{
		/**
		 * The cache current long time in second load map might is already removed by game, this
		 * method is to clear these cache which current long time in game 
		 * (Might cause lost NBT, I need spend more time to debug this method.)
		 * @author ueyudiud
		 */
		Map<IntArray, Cache> map = getChunkMap(secondLoadMap, event.world.provider.dimensionId, false);
		if(map != null)
		{
			long time = System.currentTimeMillis();
			for(Entry<IntArray, Cache> entry : ImmutableSet.copyOf(map.entrySet()))
			{
				Cache cache = entry.getValue();
				int x = entry.getKey().array[0];
				int z = entry.getKey().array[1];
				if(!event.world.blockExists(x << 4, 0, z << 4))
				{
					/**
					 * Check cached time, if long time for active remove this, and if
					 * handler not act for a long time might because it's saving map busily.
					 */
					if(time - cache.lastActiveTime > 10000L + (100L * (time - lastActived)))
					{
						map.remove(entry.getKey());
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event)
	{
		if(event.world.isRemote && !U.Sides.isSimulating()) return;
		if(cacheMap.containsKey(event.world))
		{
			secondLoadMap.put(event.world.provider.dimensionId, cacheMap.remove(event.world));
		}
		lastActived = System.currentTimeMillis();
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		if(event.world.isRemote && !U.Sides.isSimulating()) return;
		if(secondLoadMap.containsKey(event.world))
		{
			cacheMap.put(event.world.provider.dimensionId, secondLoadMap.get(event.world));
		}
		lastActived = System.currentTimeMillis();
	}
	
	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load event)
	{
		if(event.world.isRemote && !U.Sides.isSimulating()) return;
		Map<IntArray, Cache> map = getChunkMap(secondLoadMap, event.world.provider.dimensionId, false);
		cache.array[0] = event.getChunk().xPosition;
		cache.array[1] = event.getChunk().zPosition;
		Cache cache = null;
		if(map != null)
		{
			cache = map.remove(this.cache);
			map = getChunkMap(cacheMap, event.world.provider.dimensionId, true);
		}
		map.put(this.cache.copy(), cache != null ? cache : new Cache(event.world, event.getChunk()));
		lastActived = System.currentTimeMillis();
	}
	
	@SubscribeEvent
	public void onChunkUnload(ChunkEvent.Unload event)
	{
		if(event.world.isRemote && !U.Sides.isSimulating()) return;
		Map<IntArray, Cache> map = getChunkMap(cacheMap, event.world.provider.dimensionId, false);
		if(map != null)
		{
			cache.array[0] = event.getChunk().xPosition;
			cache.array[1] = event.getChunk().zPosition;
			if(map.containsKey(cache))
			{
				Cache cache = map.remove(this.cache);
				cache.lastActiveTime = System.currentTimeMillis();
				map = getChunkMap(secondLoadMap, event.world.provider.dimensionId, true);
				map.put(this.cache.copy(), cache);
			}
		}
		lastActived = System.currentTimeMillis();
	}
	
	@SubscribeEvent
	public void onChunkLoad(ChunkDataEvent.Load event)
	{
		if(event.world.isRemote && !U.Sides.isSimulating()) return;
		if(event.getData().hasKey(saveName))
		{
			NBTTagCompound nbt = event.getData().getCompoundTag(saveName);
			Map<IntArray, Cache> map = getChunkMap(secondLoadMap, event.world.provider.dimensionId, true);
			cache.array[0] = event.getChunk().xPosition;
			cache.array[1] = event.getChunk().zPosition;
			if(map.containsKey(cache))
			{
				map.get(cache).readFromNBT(nbt);
			}
			else
			{
				Cache cache;
				map.put(this.cache.copy(), (cache = new Cache(event.world, event.getChunk())));
				cache.readFromNBT(nbt);
			}
		}
		lastActived = System.currentTimeMillis();
	}
	
	@SubscribeEvent
	public void onChunkSave(ChunkDataEvent.Save event)
	{
		if(event.world.isRemote && !U.Sides.isSimulating()) return;
		NBTTagCompound nbt = new NBTTagCompound();
		Map<IntArray, Cache> map = getChunkMap(cacheMap, event.world.provider.dimensionId, true);
		if(map != null)
		{
			cache.array[0] = event.getChunk().xPosition;
			cache.array[1] = event.getChunk().zPosition;
			if(map.containsKey(cache))
			{
				map.get(cache).writeToNBT(nbt);
			}
		}
		else
		{
			map = getChunkMap(secondLoadMap, event.world.provider.dimensionId, true);
			if(map != null)
			{
				cache.array[0] = event.getChunk().xPosition;
				cache.array[1] = event.getChunk().zPosition;
				if(map.containsKey(cache))
				{
					map.remove(cache).writeToNBT(nbt);
				}
			}
		}
		event.getData().setTag(saveName, nbt);
		lastActived = System.currentTimeMillis();
	}
	
	private static <K, E> Map<IntArray, E> getChunkMap(Map<K, Map<IntArray, E>> map, K key, boolean forceGen)
	{
		if(!map.containsKey(key))
		{
			if(forceGen)
			{
				Map<IntArray, E> map1 = new HashMap();
				map.put(key, map1);
				return map1;
			}
			return EMPTY_MAP;
		}
		return map.get(key);
	}
	
	public static class Cache
	{
		long lastActiveTime;
		private Map<String, Object> container = new HashMap();
		
		public Cache(World world, Chunk chunk)
		{
			for(ChunkConfigHandler handler : register)
			{
				container.put(handler.getHandlerName(), handler.provideDefaultTarget(world, chunk));
			}
			lastActiveTime = System.currentTimeMillis();
		}
		
		public void readFromNBT(NBTTagCompound nbt)
		{
			for(ChunkConfigHandler handler : register)
			{
				if(nbt.hasKey(handler.getHandlerName()))
				{
					handler.readFromNBT(nbt.getCompoundTag(handler.getHandlerName()), container.get(handler.getHandlerName()));
				}
			}
			lastActiveTime = System.currentTimeMillis();
		}
		
		public void writeToNBT(NBTTagCompound nbt)
		{
			for(ChunkConfigHandler handler : register)
			{
				NBTTagCompound nbt1;
				if(container.containsKey(handler.getHandlerName()))
				{
					handler.writeToNBT(nbt1 = new NBTTagCompound(), container.get(handler.getHandlerName()));
					nbt.setTag(handler.getHandlerName(), nbt1);
				}
			}
			lastActiveTime = System.currentTimeMillis();
		}
	}
	
	public static interface ChunkConfigHandler<T>
	{
		String getHandlerName();
		
		T provideDefaultTarget(World world, Chunk chunk);
		
		void writeToNBT(NBTTagCompound nbt, T target);
		
		T readFromNBT(NBTTagCompound nbt, T target);
	}
}