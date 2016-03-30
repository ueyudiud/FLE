package farcore.lib.world;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import farcore.util.U;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.util.Constants.NBT;
import scala.collection.generic.BitOperations.Int;

@Deprecated
public class ChunkDatas implements IChunkData
{	
	private static final int chunkSize = 256 * 16 * 16;
	private static volatile int[] dataCache = new int[chunkSize];
	public final short[] values = new short[chunkSize];
	private volatile ChunkCoordinates cache = new ChunkCoordinates();
	
	public void readData(int[] datas)
	{
		for(int i : datas)
		{
			values[(i >> 16) & 0xFFFF] = (short) (i & 0xFFFF);
		}
	}
	
	public void load(NBTTagCompound nbt)
	{
		try
		{
			if(nbt.hasKey("values"))
			{
				readData(nbt.getIntArray("values"));
			}
		}
		catch(Throwable throwable)
		{
			;
		}
	}
	
	public int[] writeData()
	{
		int[] list1 = dataCache;
		int size = 0;
		for(int i = 0; i < values.length; ++i)
		{
			if(values[i] == 0) continue;
			list1[size] = (i << 16) | values[i];
			size++;
		}
		if(size > 0)
		{
			int[] ret = new int[size];
			System.arraycopy(list1, 0, ret, 0, size);
			Arrays.fill(dataCache, 0);
			return ret;
		}
		else
		{
			return new int[0];
		}
	}
	
	public void save(NBTTagCompound nbt)
	{
		try
		{
			int[] datas = writeData();
			if(datas.length > 0)
			{
				nbt.setIntArray("values", datas);
			}
		}
		catch(Throwable throwable)
		{
			;
		}
	}
	
	private void initCache(int x, int y, int z)
	{
		cache.posX = x % 16;
		cache.posY = y % 256;
		cache.posZ = z % 16;
		if(cache.posX < 0) cache.posX += 16;
		if(cache.posZ < 0) cache.posZ += 16;
	}
	
	public short getBlockData(int x, int y, int z)
	{
		initCache(x, y, z);
		return getBlockData(cache);
	}

	public short getBlockData(ChunkCoordinates coord)
	{
		return values[coord.hashCode()];
	}

	public short removeBlockData(int x, int y, int z)
	{
		initCache(x, y, z);
		return removeBlockData(cache);
	}
	
	public short removeBlockData(ChunkCoordinates coord)
	{
		int i = coord.hashCode();
		short ret = values[i];
		values[i] = 0;
		return ret;
	}
		
	public boolean setBlockData(int x, int y, int z, int data)
	{
		initCache(x, y, z);
		return setBlockData(cache, (short) data);
	}
	
	public boolean setBlockData(ChunkCoordinates coord, short data)
	{
		int i = coord.hashCode();
		short value = values[i];
		values[i] = data;
		return value != data;
	}
}