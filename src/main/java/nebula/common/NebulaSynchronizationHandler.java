/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;

import nebula.Nebula;
import nebula.common.network.packet.PacketChunkNetData;
import nebula.common.tile.INetworkedSyncTile;
import nebula.common.util.L;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

/**
 * @author ueyudiud
 */
public class NebulaSynchronizationHandler
{
	private static final Map<Integer, Map<Integer, Map<ChunkPos, List<BlockPos>>>> SYNC_NETWORK_MAP = new HashMap();
	
	public static void markTileEntityForUpdate(INetworkedSyncTile tile, int type)
	{
		L.put(L.getOrPut(L.getOrPut(SYNC_NETWORK_MAP, tile.getDimension()), type), new ChunkPos(tile.pos()), tile.pos());
	}
	
	@SubscribeEvent
	public void onServerTickingEnd(TickEvent.ServerTickEvent event)
	{
		if(event.phase == Phase.END)
		{
			for(Entry<Integer, Map<Integer, Map<ChunkPos, List<BlockPos>>>> entry : ImmutableList.copyOf(SYNC_NETWORK_MAP.entrySet()))
			{
				int dim = entry.getKey();
				World world = DimensionManager.getWorld(dim);
				if(world != null)
				{
					for(Entry<Integer, Map<ChunkPos, List<BlockPos>>> entry2 : entry.getValue().entrySet())
					{
						int mark = entry2.getKey();
						for(Entry<ChunkPos, List<BlockPos>> entry3 : entry2.getValue().entrySet())
						{
							int centerX = entry3.getKey().getXCenter();
							int centerZ = entry3.getKey().getZCenter();
							Chunk chunk = world.getChunkFromChunkCoords(entry3.getKey().chunkXPos, entry3.getKey().chunkZPos);
							Nebula.network.sendLargeToNearby(new PacketChunkNetData(mark, chunk, entry3.getValue()), dim, centerX, 128, centerZ, 128.0F);
						}
					}
					entry.getValue().clear();
				}
				else
				{
					SYNC_NETWORK_MAP.remove(dim);
				}
			}
		}
	}
}