package fle.core.handler;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import farcore.lib.collection.IntArray;
import fle.core.util.ClimateManager;
import fle.core.world.climate.Climate;
import fle.core.world.climate.EnumClimate;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;

public class WorldHandler
{
	private static volatile IntArray cache = new IntArray(2);
	private static Map<World, Map<IntArray, ClimateInfo>> map = new HashMap();
	
	@SubscribeEvent
	public void onChunkSave(ChunkDataEvent.Save event)
	{
		ClimateInfo info = getClimateInfo(event.world, event.getChunk().xPosition, event.getChunk().zPosition, false);
		if(info != null)
		{
			event.getData().setIntArray("climates", info.getAsIntArray());
		}
	}

	@SubscribeEvent
	public void onChunkLoad(ChunkDataEvent.Load event)
	{
		if(event.getData().hasKey("climates"))
		{
			putClimateInfo(event.world, event.getChunk().xPosition, event.getChunk().zPosition, event.getData().getIntArray("climates"));
		}
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event)
	{
		map.remove(event.world);
	}
	
	private static Map<IntArray, ClimateInfo> getClimates(World world, boolean forceGen)
	{
		if(!map.containsKey(world))
		{
			if(forceGen)
			{
				Map<IntArray, ClimateInfo> map = new HashMap();
				WorldHandler.map.put(world, map);
				return map;
			}
			return null;
		}
		return map.get(world);
	}
	
	public static Climate getClimateAt(World world, int x, int z)
	{
		ClimateInfo info = getClimateInfo(world, x >> 4, z >> 4, false);
		return info != null ? info.getClimateAt(x, z) : 
			EnumClimate.ocean.climate();
	}
	
	public static ClimateInfo getClimateInfo(World world, int chunkX, int chunkZ, boolean forceGen)
	{
		cache.array[0] = chunkX;
		cache.array[1] = chunkZ;
		Map<IntArray, ClimateInfo> map = getClimates(world, forceGen);
		if(map == null) return null;
		if(!map.containsKey(cache))
		{
			if(forceGen)
			{
				ClimateInfo climateInfo = new ClimateInfo(world, chunkX, chunkZ);
				map.put(cache.copy(), climateInfo);
				return climateInfo;
			}
			return null;
		}
		return map.get(cache);
	}
	
	private static void putClimateInfo(World world, int chunkX, int chunkZ, int[] value)
	{
		cache.array[0] = chunkX;
		cache.array[1] = chunkZ;
		Map<IntArray, ClimateInfo> map = getClimates(world, true);
		map.put(cache, new ClimateInfo(chunkX, chunkZ, value));
	}
	
	public static boolean hasClimateInfo(World world, int chunkX, int chunkZ)
	{
		Map<IntArray, ClimateInfo> map;
		return (map = getClimates(world, false)) == null ? false :
			map.containsKey(cache.set(0, chunkX).set(1, chunkZ));
	}
	
	private static class ClimateInfo
	{
		private int x;
		private int z;
		private Climate[] climates = new Climate[256];
		
		public ClimateInfo(int x, int z, int[] array)
		{
			this.x = x;
			this.z = z;
			for(int i = 0; i < 256; ++i)
			{
				climates[i] = Climate.climates.get(i);
			}
		}
		public ClimateInfo(World world, int x, int z)
		{
			this.x = x;
			this.z = z;
			ClimateManager.getGenClimatesAt(climates, world, x << 4, z << 4, 16, 16);
		}
		
		public Climate getClimateAt(int x, int z)
		{
			return climates[x & 0xF | (z & 0xF << 4)];
		}
		
		public int[] getAsIntArray()
		{
			int[] array = new int[256];
			for(int i = 0; i < 256; ++i)
			{
				array[i] = climates[i] == null ? 0:
					climates[i].climateID;
			}
			return array;
		}
	}
}