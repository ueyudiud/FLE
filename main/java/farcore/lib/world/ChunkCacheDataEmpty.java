package farcore.lib.world;

import net.minecraft.nbt.NBTTagCompound;

public class ChunkCacheDataEmpty implements IChunkCacheData
{
	public static final ChunkCacheDataEmpty instance = new ChunkCacheDataEmpty();
	
	private ChunkCacheDataEmpty() {	}
	
	@Override
	public int get(int x, int y, int z)
	{
		return 0;
	}

	@Override
	public void set(int x, int y, int z, int value)
	{
		
	}

	@Override
	public void save(String tag, NBTTagCompound nbt)
	{
		
	}

	@Override
	public void load(String tag, NBTTagCompound nbt)
	{
		
	}
	
	@Override
	public int[] send()
	{
		return new int[0];
	}
	
	@Override
	public void sync(int[] data)
	{
		
	}
}