package fle.core.world;

import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.world.World;
import fle.core.energy.electrical.ElectricalNetLocal;

public class WorldData
{
	public static WorldData get(World world)
	{
		if (world == null)
		{
			throw new IllegalArgumentException("FLE : world is null");
	    }
	    WorldData ret = (WorldData) mapping.get(world);
	    if (ret == null)
	    {
	    	ret = new WorldData(world);
	    	mapping.put(world, ret);
	    }
	    return ret;
	}
	
	public static void remove(World world)
	{
		mapping.remove(world);
	}
	  
	private static Map<World, WorldData> mapping = new WeakHashMap();
	  
	public final World world;
	public ElectricalNetLocal eleNet;
	
	public WorldData(World world)
	{
		this.world = world;
		eleNet = new ElectricalNetLocal(world);
	}
}