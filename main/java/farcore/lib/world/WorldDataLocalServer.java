package farcore.lib.world;

import java.util.HashMap;
import java.util.Map;

import farcore.FarCoreSetup;
import farcore.enums.UpdateType;
import farcore.lib.collection.IntArray;
import farcore.lib.net.world.PacketWorldDataUpdateSingle;
import farcore.util.FleLog;
import farcore.util.U;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

@Deprecated
public class WorldDataLocalServer extends WorldDataLocal
{
	private volatile long time;
	public Map<IntArray, IChunkData> invalidMap = new HashMap();
	
	public WorldDataLocalServer(World world)
	{
		super(world);
	}
	
	@Override
	protected void update(int x, int y, int z, short data, UpdateType type)
	{
		switch (type)
		{
		case CALL_CLIENT:
			if(U.Sides.isSimulating())
			{
				FarCoreSetup.network.sendToNearBy(new PacketWorldDataUpdateSingle(world, x, y, z, data), world.provider.dimensionId, x, y, z, 256.0F);
			}
		case ONLY:
			world.markBlockRangeForRenderUpdate(x - 2, y - 2, z - 2, x + 2, y + 2, z + 2);
		case SERVER_ONLY:
			world.markBlockForUpdate(x, y, z);
		default:
			break;
		}
	}
	
	@Override
	public void onChunkLoad(Chunk chunk)
	{
		IntArray array = make(chunk.xPosition, chunk.zPosition);
		IChunkData datas;
		if(invalidMap.containsKey(array))
		{
			datas = invalidMap.remove(array);
			if(datas == null)
			{
				datas = new ChunkDatas();
			}
		}
		else
		{
			datas = new ChunkDatas();
		}
		map.put(array.copy(), datas);
		markTime();
	}
	
	@Override
	public void onChunkUnload(Chunk chunk)
	{
		IntArray array = make(chunk.xPosition, chunk.zPosition);
		IChunkData datas = map.remove(array);
		if(datas != null)
		{
			invalidMap.put(array.copy(), datas);
		}
		markTime();
	}
	
	@Override
	public void onChunkDataLoad(Chunk chunk, NBTTagCompound nbt)
	{
		IntArray array = make(chunk.xPosition, chunk.zPosition);
		IChunkData datas = map.get(array);
		if(datas == null)
		{
			invalidMap.put(array.copy(), datas = new ChunkDatas());
		}
		datas.load(nbt);
		markTime();
	}
	
	@Override
	public void onChunkDataSave(Chunk chunk, NBTTagCompound nbt)
	{
		IntArray array = make(chunk.xPosition, chunk.zPosition);
		IChunkData datas = map.get(array);
		if(datas == null)
		{
			if(invalidMap.containsKey(array))
			{
				datas = invalidMap.get(array);
				datas.save(nbt);
				invalidMap.remove(array);
			}
		}
		else
		{
			map.get(array).save(nbt);
		}
		markTime();
	}
	
	@Override
	protected void markTime()
	{
		long time1 = System.currentTimeMillis();
		if(time / 20 != time1 / 20)
		{
			time = time1;
			FleLog.getCoreLogger().info("World local data update. Now load " + map.size() + " of chunk datas.");
		}
	}
}