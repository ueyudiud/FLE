package flapi.world;

import java.util.Map;

import flapi.enums.EnumWorldNBT;

public interface IWorldManager
{
	short getData(BlockPos pos, EnumWorldNBT dataType);

	short[] getDatas(BlockPos pos);
	
	short setData(BlockPos pos, EnumWorldNBT dataType, int data);

	void setDatas(BlockPos pos, short[] data);
	
	short removeData(BlockPos pos, EnumWorldNBT dataType);

	short[] removeData(BlockPos pos);
	
	void markData(BlockPos pos);
}