package fle.core.world;

import java.util.HashMap;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import fle.api.enums.EnumWorldNBT;
import fle.api.world.BlockPos;
import fle.api.world.BlockPos.ChunkPos;

public class FWMClient extends FWM
{	
	@Override
	public short setData(BlockPos pos, int dataType, int data, boolean sync)
	{
		return getChunkData(pos.getDim(), pos).setDataFromCoord(dataType, pos, (short) data);
	}
	
	@Override
	public void setDatas(BlockPos pos, short[] data, boolean sync)
	{
		int dim = pos.getDim();
		if(data != null)
		{
			ChunkData tData = getChunkData(dim, pos);
			for(int i = 0; i < data.length; ++i)
				tData.setDataFromCoord(i, pos, (short) data[i]);
		}
	}
	
	@Override
	public void sendData(BlockPos pos)
	{
		;
	}
	
	@Override
	public void sendAllData(int dim, ChunkPos pos)
	{
		;
	}
	
	@Override
	public void markPosForUpdate(BlockPos pos)
	{
		;
	}
	
	@Override
	public void syncData(int dim, ChunkPos pos, int[][] datas)
	{
		if(!nbts.containsKey(dim)) nbts.put(dim, new HashMap());
		if(!nbts.get(dim).containsKey(pos)) nbts.get(dim).put(pos, new ChunkData(EnumWorldNBT.values().length));
		ChunkData data = nbts.get(dim).get(pos);
		for(int i = 0; i < datas.length; ++i)
			data.loadFromIntArray(i, datas[i]);
	}
	
	@Override
	public IMessage createPacket(int dim, BlockPos pos)
	{
		return null;
	}
}