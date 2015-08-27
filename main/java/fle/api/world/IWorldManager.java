package fle.api.world;

import net.minecraft.nbt.NBTTagCompound;

public interface IWorldManager
{
	public int getData(BlockPos pos, int dataType);
	
	public void setData(BlockPos pos, int dataType, int data);

	public int[] removeData(BlockPos pos);
	
	public int removeData(BlockPos pos, int type);

	public void setDatas(BlockPos pos, int[] data, boolean b);
}
