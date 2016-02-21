package fle.core.net;

import java.io.IOException;

import flapi.enums.EnumWorldNBT;
import flapi.net.FleAbstractPacket;
import flapi.net.FleNetworkHandler;
import flapi.util.io.FleDataInputStream;
import flapi.util.io.FleDataOutputStream;
import flapi.world.BlockPos.ChunkPos;
import fle.FLE;

public class FleWorldMetaSyncPacket extends FleAbstractPacket
{
	int dim;
	ChunkPos pos;
	int[][] datas;
	
	public FleWorldMetaSyncPacket(int aDim, ChunkPos aPos, int[][] aDatas)
	{
		dim = aDim;
		pos = aPos;
		datas = aDatas.clone();
	}
	public FleWorldMetaSyncPacket()
	{
		
	}

	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		os.writeInt(dim);
		os.writeLong(pos.x);
		os.writeLong(pos.z);
		for(int[] array : datas)
		{
			os.writeIntArray(array);
		}
	}

	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		dim = is.readInt();
		long x = is.readLong();
		long z = is.readLong();
		pos = new ChunkPos(x, z);
		datas = new int[EnumWorldNBT.values().length][];
		for(int i = 0; i < datas.length; ++i)
		{
			datas[i] = is.readIntArray();
		}
	}

	@Override
	public Object process(FleNetworkHandler nwh)
	{
		FLE.fle.getWorldManager().syncMetas(dim, pos, datas);
		return null;
	}
}