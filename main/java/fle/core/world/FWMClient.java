package fle.core.world;

import net.minecraftforge.event.world.ChunkDataEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fle.FLE;
import fle.api.world.BlockPos;
import fle.core.net.FlePackets.CoderFWMAskMeta;

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
}