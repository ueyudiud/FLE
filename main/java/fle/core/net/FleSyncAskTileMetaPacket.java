package fle.core.net;

import java.io.IOException;

import flapi.net.FleCoordinatesPacket;
import flapi.net.FleNetworkHandler;
import flapi.net.INetEventEmmiter;
import flapi.util.io.FleDataInputStream;
import flapi.util.io.FleDataOutputStream;
import flapi.world.BlockPos;

public class FleSyncAskTileMetaPacket extends FleCoordinatesPacket
{
	byte type;
	
	public FleSyncAskTileMetaPacket()
	{
		super(true);
	}
	public FleSyncAskTileMetaPacket(byte aType, BlockPos pos)
	{
		super(true, pos);
		type = aType;
	}
	
	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		super.write(os);
		os.writeByte(type);
	}
	
	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		super.read(is);
		type = is.readByte();
	}
	
	@Override
	public Object process(FleNetworkHandler nwh)
	{
		BlockPos pos = pos();
		return pos.getBlockTile() instanceof INetEventEmmiter ? new FleTEPacket(pos, type, (INetEventEmmiter) pos.getBlockTile()) : null;
	}
}