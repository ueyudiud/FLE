package farcore.lib.world;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import farcore.FarCoreSetup;
import farcore.lib.collection.IntArray;
import farcore.lib.net.world.PacketWorldDataUpdateAll;
import farcore.lib.net.world.PacketWorldDataUpdateSingle;
import farcore.util.U;
import farcore.util.V;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

public class WorldDatasServer extends WorldDatasAbstract
{
	private Map<World, FarCoreWorldAccess> map = new HashMap();
	
	public WorldDatasServer()
	{
		if(!V.disableWM)
		{
			MinecraftForge.EVENT_BUS.register(this);
		}
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		FarCoreWorldAccess access = new FarCoreWorldAccess(event.world);
		event.world.addWorldAccess(access);
		map.put(event.world, access);
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event)
	{
		event.world.removeWorldAccess(map.remove(event.world));
	}
	
	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load event)
	{
		
	}
	
	@SubscribeEvent
	public void onChunkUnload(ChunkEvent.Unload event)
	{
		if(event.world.isRemote) return;
		invalidChunkData(event.getChunk());
	}
	
	@SubscribeEvent
	public void onChunkDataLoad(ChunkDataEvent.Load event)
	{
		if(event.world.isRemote) return;
		validAndLoadChunkData(event.getChunk(), event.getData());
	}
	
	@SubscribeEvent
	public void onChunkDataUnload(ChunkDataEvent.Save event)
	{
		if(event.world.isRemote) return;
		saveAndRemoveChunkData(event.getChunk(), event.getData());
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
		setSmartMetadataWithNotify(U.Worlds.world(dim), x, y, z, meta, flag);
	}
	
	@Override
	public void setSmartMetadataWithNotify(World world, int x, int y, int z, int meta, int flag)
	{
		if(y >= 256 || y < 0) return;
		setSmartMetadata(world, x, y, z, meta);
		if(U.Sides.isSimulating())
		{
			FarCoreSetup.network.sendToDim(new PacketWorldDataUpdateSingle(world, x, y, z, meta, 0), world.provider.dimensionId);
		}
		
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
		return getChunkData(new IntArray(new int[]{dim, x, z}), false).send();
	}
	
	@Override
	public void loadChunkData(int dim, int x, int z, int[] data)
	{
		//Server side do nothing.
	}
}