package fle.core.net;

import java.io.IOException;

import flapi.net.FleCoordinatesPacket;
import flapi.net.FleNetworkHandler;
import flapi.net.INetEventEmmiter;
import flapi.net.INetEventHandler;
import flapi.net.INetEventListener;
import flapi.te.interfaces.IObjectInWorld;
import flapi.util.io.FleDataInputStream;
import flapi.util.io.FleDataOutputStream;
import flapi.world.BlockPos;

public class FleTEPacket extends FleCoordinatesPacket
{
	private byte type;
	private Object contain;
	
	public FleTEPacket()
	{
		super(true);
	}
	FleTEPacket(byte aType, Object aContain)
	{
		super(true);
		type = aType;
		contain = aContain;
	}
	public FleTEPacket(IObjectInWorld obj, byte aType, INetEventEmmiter emmiter) 
	{
		super(true, obj.getBlockPos());
		type = aType;
		contain = emmiter.onEmit(type);
	}
	public FleTEPacket(INetEventHandler handler, byte aType) 
	{
		super(true, handler.getBlockPos());
		type = aType;
		contain = handler.onEmit(type);
	}
	
	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		super.write(os);
		os.writeByte(type);
		os.write(contain);
	}

	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		super.read(is);
		type = is.readByte();
		contain = is.read();
	}

	@Override
	public Object process(FleNetworkHandler nwh)
	{
		BlockPos pos = pos();
		if(pos.getBlockTile() instanceof INetEventListener)
		{
			((INetEventListener) pos.getBlockTile()).onReceive(type, contain);
		}
		world().markBlockForUpdate(pos.x, pos.y, pos.z);
		return null;
	}
}