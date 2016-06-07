package farcore.lib.world;

import farcore.util.FleLog;
import net.minecraft.nbt.NBTTagCompound;

public class ChunkCacheData implements IChunkCacheData
{
	private int[] values = new int[256 * 16 * 16];
	
	public int get(int x, int y, int z)
	{
		return values[idxcode(x, y, z)];
	}
	
	public void set(int x, int y, int z, int value)
	{
		values[idxcode(x, y, z)] = value;
	}
	
	@Override
	public void save(String tag, NBTTagCompound nbt)
	{
		nbt.setIntArray(tag, values);
	}
	
	@Override
	public void load(String tag, NBTTagCompound nbt)
	{
		if(!nbt.hasKey(tag)) return;
		int[] array = nbt.getIntArray(tag);
		if(array.length != 65536) FleLog.getCoreLogger().warn("Fail to load chunk data from chunk nbt, use default instead.");
		values = array;
	}
	
	@Override
	public int[] send()
	{
		return values;
	}
	
	@Override
	public void sync(int[] data)
	{
		if(data.length != 65536) throw new IllegalArgumentException("Fail to sync data, beacause this data has wrong length.");
		values = data;
	}
	
	int idxcode(int x, int y, int z)
	{
		return y << 8 | x << 4 | z;
	}
}