package fle.core.world;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import flapi.enums.EnumWorldNBT;
import flapi.net.IPacket;
import flapi.world.BlockPos;
import flapi.world.BlockPos.ChunkPos;
import fle.FLE;
import fle.core.net.FleSyncAskFWMPacket;
import fle.core.net.FleSyncAskTileMetaPacket;

public class FWMClient extends FWM
{
	@Override
	public short setData(BlockPos pos, int dataType, int data, byte syncType)
	{
		return getData(pos, dataType);
	}
	
	@Override
	public void setDatas(BlockPos pos, short[] data, byte syncType)
	{
		;
	}
	
	@Override
	public void markData(BlockPos pos)
	{
		;
	}
	
	@Override
	public void setPollute(BlockPos pos, int pollute)
	{
		;
	}
	
	@Override
	public void syncMeta(World world, BlockPos pos, short[] data)
	{
		super.setDatas(pos, data, (byte) 2);
	}
	
	@Override
	public void syncMetas(int dim, ChunkPos pos, int[][] datas)
	{
		if(!nbts.containsKey(dim))
		{
			nbts.put(dim, new FWMLocal(dim));
		}
		FWMLocal local = nbts.get(dim);
		local.set(pos, datas);
	}
}