package farcore.lib.world;

import net.minecraft.nbt.NBTTagCompound;

@Deprecated
public class ChunkDataEmpty implements IChunkData
{
	public static final ChunkDataEmpty DATA = new ChunkDataEmpty();
	
	private ChunkDataEmpty() {}
	
	@Override
	public void readData(int[] datas){}

	@Override
	public void load(NBTTagCompound nbt){}

	@Override
	public int[] writeData() 
	{
		return new int[0];
	}

	@Override
	public void save(NBTTagCompound nbt) {}

	@Override
	public short getBlockData(int x, int y, int z)
	{
		return 0;
	}

	@Override
	public short removeBlockData(int x, int y, int z)
	{
		return 0;
	}

	@Override
	public boolean setBlockData(int x, int y, int z, int data)
	{
		return false;//Always needn't update.
	}
}