package fle.core.world;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkDataEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fle.FLE;
import fle.api.net.FlePackets.CoderFWMUpdate;
import fle.api.world.BlockPos;
import fle.core.net.FlePackets.CoderFWMAskMeta;
import fle.core.world.FWM.ChunkData;

public class FWMClient extends FWM
{	
	@SubscribeEvent
	public void onDataLoad(ChunkDataEvent.Load evt)
	{
		;
	}
	
	@SubscribeEvent
	public void onDataSave(ChunkDataEvent.Save evt)
	{
		;
	}
	
	@Override
	public short getData(BlockPos pos, int dataType)
	{
		FLE.fle.getNetworkHandler().sendToServer(new CoderFWMAskMeta(pos));
		short ret = super.getData(pos, dataType);
		return ret == -1 ? 0 : ret;
	}
	
	@Override
	public short[] getDatas(BlockPos aPos)
	{
		FLE.fle.getNetworkHandler().sendToServer(new CoderFWMAskMeta(aPos));
		short[] ret = super.getDatas(aPos);
		for(int i = 0; i < ret.length; ++i)
		{
			if(ret[i] == -1) ret[i] = 0;
		}
		return ret;
	}
	
	@Override
	public short setData(BlockPos pos, int dataType, int data, boolean sync)
	{
		return getChunkData(pos.getDim(), pos).setDataFromCoord(dataType, pos, (short) data);
	}
	
	@Override
	public void setDatas(BlockPos pos, short[] data, boolean sync)
	{
		if(data != null)
		{
			ChunkData tData = getChunkData(pos.getDim(), pos);
			for(int i = 0; i < data.length; ++i)
				tData.setDataFromCoord(i, pos, (short) data[i]);
		}
	}
	
	@Override
	public short[] removeData(BlockPos pos)
	{
		return getDatas(pos);
	}
	
	@Override
	public void sendData(BlockPos pos)
	{
		;
	}
}