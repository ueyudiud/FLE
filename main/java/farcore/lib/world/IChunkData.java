package farcore.lib.world;

import net.minecraft.nbt.NBTTagCompound;

@Deprecated
public interface IChunkData
{
	/**
	 * Read data from int array.
	 * Used by network.
	 * @param datas
	 */
	void readData(int[] datas);
	
	/**
	 * Load from NBT.
	 * @param nbt
	 */
	void load(NBTTagCompound nbt);
	
	/**
	 * Write data to array.
	 * Used by network.
	 * @return
	 */
	int[] writeData();
	
	/**
	 * Save fromt NBT.
	 * @param nbt
	 */
	void save(NBTTagCompound nbt);
	
	/**
	 * Get block data with coordinate.
	 * @param x
	 * @param y
	 * @param z
	 * @return The data.
	 */
	short getBlockData(int x, int y, int z);
	
	/**
	 * Remove block data with coordinate.
	 * @param x
	 * @param y
	 * @param z
	 * @return The old data current.
	 */
	short removeBlockData(int x, int y, int z);
	
	/**
	 * Set block data with coordinate.
	 * @param x
	 * @param y
	 * @param z
	 * @param data
	 * @return The new data is not same with old data (To resolve
	 * whether need to mark update to client).
	 */
	boolean setBlockData(int x, int y, int z, int data);
}