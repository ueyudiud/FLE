package farcore.lib.world;

import net.minecraft.nbt.NBTTagCompound;

public interface IChunkCacheData
{
	int get(int x, int y, int z);
	
	void set(int x, int y, int z, int value);
	
	void save(String tag, NBTTagCompound nbt);
	
	void load(String tag, NBTTagCompound nbt);

	int[] send();

	void sync(int[] data);
}