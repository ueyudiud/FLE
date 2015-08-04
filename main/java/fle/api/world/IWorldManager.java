package fle.api.world;

import net.minecraft.nbt.NBTTagCompound;

public interface IWorldManager
{
	public int getData(BlockPos pos, int dataType);
	
	public void setData(BlockPos pos, int dataType, int data);

	public void removeData(BlockPos pos);
	public void removeData(BlockPos pos, int type);
}
