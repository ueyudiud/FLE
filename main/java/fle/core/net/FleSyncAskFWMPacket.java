package fle.core.net;

import java.io.IOException;

import flapi.net.FleAbstractPacket;
import flapi.net.FleNetworkHandler;
import flapi.util.io.FleDataInputStream;
import flapi.util.io.FleDataOutputStream;
import flapi.world.BlockPos.ChunkPos;
import fle.FLE;

public class FleSyncAskFWMPacket extends FleAbstractPacket
{	
	ChunkPos pos;
	int dim;
	
	public FleSyncAskFWMPacket()
	{
		super();
	}
	public FleSyncAskFWMPacket(int dim, ChunkPos pos)
	{
		super();
		this.dim = dim;
		this.pos = pos;
	}
	
	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		os.writeInt(dim);
		os.writeInt((int) pos.x);
		os.writeInt((int) pos.z);
	}
	
	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		dim = is.readInt();
		int x = is.readInt();
		int z = is.readInt();
		pos = new ChunkPos(x, z);
	}
	
	@Override
	public Object process(FleNetworkHandler nwh)
	{
		FLE.fle.getWorldManager().markPosForUpdate(getPlayer(), pos);
		return null;
	}
}