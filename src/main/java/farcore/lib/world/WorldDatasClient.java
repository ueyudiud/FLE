package farcore.lib.world;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import farcore.FarCoreSetup;
import farcore.lib.collection.IntArray;
import farcore.lib.net.world.PacketWorldDataAskUpdate;
import farcore.lib.net.world.PacketWorldDataUpdateSingle;
import farcore.util.U;
import farcore.util.V;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;

public class WorldDatasClient extends WorldDatasAbstract
{
	public WorldDatasClient()
	{
		if(!V.disableWM)
		{
			MinecraftForge.EVENT_BUS.register(this);
		}
	}
	
	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load event)
	{
		if(!event.world.isRemote) return;
		if(!datas.containsKey(toArrayCodeIdx(event.getChunk())))
		{
			FarCoreSetup.network.sendToServer(new PacketWorldDataAskUpdate(event.world, event.getChunk().xPosition, event.getChunk().zPosition));
		}
	}
	
	@Override
	public int getSmartMetadata(World world, int x, int y, int z)
	{
		return getSmartMetadata(world.provider.dimensionId, x, y, z);
	}
	
	@Override
	public int getSmartMetadata(int dim, int x, int y, int z)
	{
		if(y >= 256 || y < 0) return 0;
		return getChunkDataFromBlockCoord(dim, x, z, false).get(x % 16, y, z % 16);
	}

	@Override
	public void setSmartMetadata(World world, int x, int y, int z, int meta)
	{
		if(y >= 256 || y < 0) return;
		getChunkDataFromBlockCoord(world.provider.dimensionId, x, z, false).set(x % 16, y, z % 16, meta);
	}
	
	@Override
	public void setSmartMetadataWithNotify(int dim, int x, int y, int z, short meta, int flag)
	{
		World world = U.Worlds.world(dim);
		if(world == null)
		{
			setSmartMetadata(world, x, y, z, meta);
		}
		else
		{
			setSmartMetadataWithNotify(world, x, y, z, meta, flag);
		}
	}

	@Override
	public void setSmartMetadataWithNotify(World world, int x, int y, int z, int meta, int flag)
	{
		if(y >= 256 || y < 0) return;
		setSmartMetadata(world, x, y, z, meta);
		
		//Notify start
		Block block = world.getBlock(x, y, z);
		Chunk chunk = world.getChunkFromBlockCoords(x, z);
        if ((flag & 2) != 0 && (!world.isRemote || (flag & 4) == 0) && chunk.func_150802_k())
        {
            world.markBlockForUpdate(x, y, z);
        }

        if (!world.isRemote && (flag & 1) != 0)
        {
            world.notifyBlockChange(x, y, z, block);

            if (block.hasComparatorInputOverride())
            {
                world.func_147453_f(x, y, z, block);
            }
        }
	}
	
	@Override
	public int[] saveChunkData(int dim, int x, int z)
	{
		return new int[0];
	}
	
	@Override
	public void loadChunkData(int dim, int x, int z, int[] data)
	{
		getChunkData(new IntArray(new int[]{dim, x, z}), true).sync(data);
	}
}