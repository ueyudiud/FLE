package farcore.lib.world;

import java.util.HashMap;
import java.util.Map;

import farcore.lib.collection.IntArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;

public abstract class WorldDatasAbstract implements IWorldDatas
{
	static final Map<IntArray, ChunkCacheData> datas = new HashMap();
	static final Map<IntArray, ChunkCacheData> invalidDatas = new HashMap();

	IntArray toArrayCodeIdx(Chunk chunk)
	{
		return new IntArray(new int[]{chunk.worldObj.provider.dimensionId, chunk.xPosition, chunk.zPosition});
	}

	protected IChunkCacheData getChunkDataFromBlockCoord(int dim, int x, int z, boolean create)
	{
		return getChunkData(new IntArray(new int[]{dim, x >> 4, z >> 4}), create);
	}
	protected IChunkCacheData getChunkData(Chunk chunk, boolean create)
	{
		return getChunkData(toArrayCodeIdx(chunk), create);
	}
	protected IChunkCacheData getChunkData(IntArray coord, boolean create)
	{
		if(!datas.containsKey(coord))
		{
			if(!create) 
			{	
				return ChunkCacheDataEmpty.instance;
			}
			ChunkCacheData data = new ChunkCacheData();
			datas.put(coord, data);
			return data;
		}
		return datas.get(coord);
	}
	
	protected IChunkCacheData getInvalidChunkData(Chunk chunk)
	{
		IntArray coord = toArrayCodeIdx(chunk);
		if(!invalidDatas.containsKey(coord))
		{
			return ChunkCacheDataEmpty.instance;
		}
		return invalidDatas.get(coord);
	}

	protected void validAndLoadChunkData(Chunk chunk, NBTTagCompound nbt)
	{
		getChunkData(chunk, true).load("smartData", nbt);
	}
	
	protected void invalidChunkData(Chunk chunk)
	{
		IntArray coord = toArrayCodeIdx(chunk);
		if(!datas.containsKey(coord))
		{
			return;
		}
		ChunkCacheData data = datas.remove(coord);
		invalidDatas.put(coord, data);
	}
	
	protected void saveAndRemoveChunkData(Chunk chunk, NBTTagCompound nbt)
	{
		IntArray coord = toArrayCodeIdx(chunk);
		if(datas.containsKey(coord))
		{
			datas.get(coord).save("smartData", nbt);
		}
		else if(invalidDatas.containsKey(coord))
		{
			invalidDatas.remove(coord).save("smartData", nbt);
		}
	}
	
	protected void clearInvalidChunkData(Chunk chunk)
	{
		invalidDatas.remove(toArrayCodeIdx(chunk));
	}

}
