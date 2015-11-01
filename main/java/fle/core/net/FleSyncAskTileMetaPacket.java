package fle.core.net;

import java.io.IOException;

import fle.api.net.FleCoordinatesPacket;
import fle.api.net.FleNetworkHandler;
import fle.api.net.INetEventEmmiter;
import fle.api.util.FleDataInputStream;
import fle.api.util.FleDataOutputStream;
import fle.api.world.BlockPos;

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
		return pos.getBlockTile() instanceof INetEventEmmiter ? new FleTEPacket(type, ((INetEventEmmiter) pos.getBlockTile()).onEmmit(type)) : null;
	}
}