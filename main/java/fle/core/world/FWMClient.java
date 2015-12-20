package fle.core.world;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import flapi.net.IPacket;
import flapi.world.BlockPos;
import flapi.world.BlockPos.ChunkPos;
import fle.FLE;
import fle.core.net.FleSyncAskFWMPacket;

public class FWMClient extends FWM
{	
	@Override
	public short setData(BlockPos pos, int dataType, int data, boolean sync)
	{
		return getData(pos, dataType);
	}
	
	@Override
	public void setDatas(BlockPos pos, short[] data, boolean sync)
	{
		;
	}
	
	@Override
	public void sendChunkData(EntityPlayerMP player, ChunkPos pos)
	{
		;
	}
	
	@Override
	public void sendChunkData(int dim, ChunkPos pos)
	{
		;
	}
	
	@Override
	public void askChunkData(int dim, ChunkPos pos)
	{
		FLE.fle.getNetworkHandler().sendToServer(new FleSyncAskFWMPacket(dim, pos));
	}
	
	@Override
	public IPacket createPacket(int dim, BlockPos pos)
	{
		return null;
	}
	
	@Override
	public void syncMeta(World world, BlockPos pos, short[] data)
	{
		super.setDatas(pos, data, false);
	}
	
	@Override
	public void syncMetas(int dim, ChunkPos pos, int[][] datas)
	{
		if(!nbts.containsKey(dim)) nbts.put(dim, new HashMap());
		if(!nbts.get(dim).containsKey(pos)) nbts.get(dim).put(pos, new ChunkData());
		ChunkData cd = nbts.get(dim).get(pos);
		for(int i = 0; i < datas.length; ++i)
			cd.loadFromIntArray(i, datas[i]);
	}
}