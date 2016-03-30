package farcore.lib.world;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import farcore.enums.UpdateType;
import farcore.lib.collection.IntArray;
import farcore.util.FleLog;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants.NBT;
import scala.collection.generic.BitOperations.Int;

@Deprecated
public class WorldDataLocal
{
	public final World world;
	private NBTTagCompound currentData = new NBTTagCompound();
	public Map<IntArray, IChunkData> map = new HashMap();
	
	public WorldDataLocal(World world)
	{
		this.world = world;
	}
		
	public int getDim()
	{
		return world.provider.dimensionId;
	}
	
	public NBTTagCompound getWorldData()
	{
		return currentData;
	}
	
	public void onWorldLoad()
	{
		if(world.getWorldInfo().getNBTTagCompound().hasKey("fle"))
		{
			currentData = world.getWorldInfo().getNBTTagCompound().getCompoundTag("fle");
		}
	}
	
	public void onWorldSave()
	{
		if(!currentData.hasNoTags())
		{
			world.getWorldInfo().getNBTTagCompound().setTag("fle", currentData);
		}
		else
		{
			world.getWorldInfo().getNBTTagCompound().removeTag("fle");
		}
	}

	public void onChunkLoad(Chunk chunk)
	{
		map.put(make(chunk).copy(), new ChunkDatas());
		markTime();
	}
	
	public void onChunkUnload(Chunk chunk)
	{
		map.remove(make(chunk));
	}
	
	public void onChunkDataLoad(Chunk chunk, NBTTagCompound nbt){}
	public void onChunkDataSave(Chunk chunk, NBTTagCompound nbt){}
	
	public IChunkData data(int x, int z)
	{
		IntArray array = make(x, z);
		return map.containsKey(array) ? map.get(array) : null;
	}
	
	public short getBlockData(int x, int y, int z)
	{
		IChunkData datas = data(x / 16, z / 16);
		if(datas == null) return 0;
		return datas.getBlockData(x, y, z);
	}
	
	public short removeBlockData(int x, int y, int z)
	{
		IChunkData datas = data(x / 16, z / 16);
		if(datas == null) return 0;
		return datas.removeBlockData(x, y, z);
	}
	
	public boolean setBlockData(int x, int y, int z, int value, UpdateType type)
	{
		IChunkData datas = data(x / 16, z / 16);
		if(datas == null)
		{
			map.put(make(x / 16, z / 16), datas = new ChunkDatas());
		}
		update(x, y, z, (short) value, type);
		return datas.setBlockData(x, y, z, value);
	}
	
	protected void update(int x, int y, int z, short data, UpdateType type)
	{
		switch (type)
		{
		case ONLY:
		case CALL_CLIENT:
			world.markBlockForUpdate(x, y, z);
			world.markBlockRangeForRenderUpdate(x - 2, y - 2, z - 2, x + 2, y + 2, z + 2);
			break;
		default:
			break;
		}
	}

	private volatile IntArray array = new IntArray(2);

	protected final IntArray make(Chunk chunk)
	{		
		return make(chunk.xPosition, chunk.zPosition);
	}
	protected final IntArray make(int x, int z)
	{		
		array.array[0] = x;
		array.array[1] = z;
		return array;
	}
	
	protected void markTime()
	{
		
	}
}