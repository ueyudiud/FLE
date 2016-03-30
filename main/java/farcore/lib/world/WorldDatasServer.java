package farcore.lib.world;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import farcore.util.FleLog;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

@Deprecated
public class WorldDatasServer extends WorldDatas
{
	@SubscribeEvent
	public void $1(WorldEvent.Save evt)
	{
		WorldDataLocal local = map.get(evt.world.provider.dimensionId);
		if(local != null)
		{
			local.onWorldSave();
		}
	}
	
	@SubscribeEvent
	public void $2(WorldEvent.Load evt)
	{
		super.$2(evt);
	}
	
	@SubscribeEvent
	public void $3(WorldEvent.Unload evt)
	{
		super.$3(evt);
	}

	@SubscribeEvent
	public void $4(ChunkEvent.Load evt)
	{
		super.$4(evt);
	}

	@SubscribeEvent
	public void $5(ChunkEvent.Unload evt)
	{
		super.$5(evt);
	}
	
	@SubscribeEvent
	public void $6(ChunkDataEvent.Load evt)
	{
		try
		{
			map.get(evt.world.provider.dimensionId).onChunkDataLoad(evt.getChunk(), evt.getData());
		}
		catch(Throwable throwable)
		{
			FleLog.getCoreLogger().catching(throwable);
		}
	}
	
	@SubscribeEvent
	public void $7(ChunkDataEvent.Save evt)
	{
		try
		{
			map.get(evt.world.provider.dimensionId).onChunkDataSave(evt.getChunk(), evt.getData());
		}
		catch(Throwable throwable)
		{
			FleLog.getCoreLogger().catching(throwable);
		}
	}
	
	@Override
	protected WorldDataLocal newLocalData(World world)
	{
		return new WorldDataLocalServer(world);
	}
}