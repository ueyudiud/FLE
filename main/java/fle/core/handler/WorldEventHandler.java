package fle.core.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import farcore.FarCore;
import farcore.tile.ILoadInitTileEntity;
import fle.core.FLE;
import fle.core.net.FleTileLoadedAskSyncPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.ChunkEvent;

public class WorldEventHandler
{
	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load evt)
	{
//		if(evt.world.isRemote)
//		{
//			List<TileEntity> list = new ArrayList();
//			for(Entry entry : 
//				(Iterable<Entry>) evt.getChunk().chunkTileEntityMap.entrySet())
//			{
//				TileEntity tile = (TileEntity) entry.getValue();
//				if(tile instanceof ILoadInitTileEntity)
//				{
//					list.add(tile);
//				}
//			}
//			if(list.isEmpty()) return;
//			FLE.fle.getNetworkHandler().sendToServer(new FleTileLoadedAskSyncPacket(list));
//		}
	}
}