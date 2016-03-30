package farcore.lib.world;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import farcore.enums.UpdateType;
import farcore.util.FleLog;
import farcore.util.SideGateway;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;

@Deprecated
public class WorldDatas
{
	public static final SideGateway<WorldDatas> instance = 
			new SideGateway("farcore.lib.world.WorldDatasServer", "farcore.lib.world.WorldDatas");
	
	protected Map<Integer, WorldDataLocal> map = new HashMap();
	
	public void $1(WorldEvent.Save evt)
	{
		
	}
	
	@SubscribeEvent
	public void $2(WorldEvent.Load evt)
	{
		WorldDataLocal local = newLocalData(evt.world);
		map.put(evt.world.provider.dimensionId, local);
		local.onWorldLoad();
	}
	
	@SubscribeEvent
	public void $3(WorldEvent.Unload evt)
	{
		map.remove(evt.world.provider.dimensionId);
	}

	@SubscribeEvent
	public void $4(ChunkEvent.Load evt)
	{
		try
		{
			WorldDataLocal local = map.get(evt.world.provider.dimensionId);
			if(local == null)
			{
				local = newLocalData(evt.world);
				map.put(evt.world.provider.dimensionId, local);
			}
			local.onChunkLoad(evt.getChunk());
		}
		catch(Throwable throwable)
		{
			FleLog.getCoreLogger().catching(throwable);
		}
	}

	@SubscribeEvent
	public void $5(ChunkEvent.Unload evt)
	{
		try
		{
			WorldDataLocal local = map.get(evt.world.provider.dimensionId);
			if(local != null)
			{
				local.onChunkUnload(evt.getChunk());
			}
		}
		catch(Throwable throwable)
		{
			FleLog.getCoreLogger().catching(throwable);
		}
	}

	public void $6(ChunkDataEvent.Load evt)
	{
		
	}
	
	public void $7(ChunkDataEvent.Save evt)
	{
		
	}
	
	protected WorldDataLocal newLocalData(World world)
	{
		return new WorldDataLocal(world);
	}

	public static short getBlockData(World world, int x, int y, int z) 
	{
		return getBlockData(world.provider.dimensionId, x, y, z);
	}

	public static void setBlockData(World world, int x, int y, int z, int value)
	{
		setBlockData(world.provider.dimensionId, x, y, z, value);
	}

	public static void setBlockData(World world, int x, int y, int z, int value, UpdateType type)
	{
		setBlockData(world.provider.dimensionId, x, y, z, value, type);
	}

	public static short getBlockData(int dim, int x, int y, int z) 
	{
		return instance.get().map.containsKey(dim) ? 
				instance.get().map.get(dim).getBlockData(x, y, z) :
					0;
	}

	public static void setBlockData(int dim, int x, int y, int z, int value)
	{
		if(instance.get().map.containsKey(dim))
		{
			WorldDataLocal local = instance.get().map.get(dim);
			local.setBlockData(x, y, z, value, UpdateType.NO_UPDATE);
		}
	}

	public static void setBlockData(int dim, int x, int y, int z, int value, UpdateType type)
	{
		if(instance.get().map.containsKey(dim))
		{
			WorldDataLocal local = instance.get().map.get(dim);
			local.setBlockData(x, y, z, value, type);
		}
	}

	public static void setChunkData(int dim, int x, int z, int[] datas)
	{
		if(instance.get().map.containsKey(dim))
		{
			WorldDataLocal local = instance.get().map.get(dim);
			IChunkData data = local.data(x, z);
			if(data != null)
			{
				data.readData(datas);
			}
		}
	}
}