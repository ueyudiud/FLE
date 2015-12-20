package flapi.world;

import java.util.Map;

import flapi.enums.EnumWorldNBT;

public interface IWorldManager
{
	short getData(BlockPos pos, EnumWorldNBT dataType);

	short[] getDatas(BlockPos pos);
	
	short setData(BlockPos pos, EnumWorldNBT dataType, int data);

	void setDatas(BlockPos pos, Map<EnumWorldNBT, Integer> map, boolean sync);

	void setDatas(BlockPos pos, short[] data, boolean sync);
	
	short removeData(BlockPos pos, EnumWorldNBT dataType);

	short[] removeData(BlockPos pos);
}