package fle.core.net;

import java.io.IOException;

import fle.api.FleAPI;
import fle.api.net.FleCoordinatesPacket;
import fle.api.net.FleNetworkHandler;
import fle.api.net.FleSortInfomationPacket;
import fle.api.net.INetEventEmmiter;
import fle.api.net.INetEventHandler;
import fle.api.net.INetEventListener;
import fle.api.te.IObjectInWorld;
import fle.api.util.FleDataInputStream;
import fle.api.util.FleDataOutputStream;
import fle.api.world.BlockPos;

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
		contain = emmiter.onEmmit(type);
	}
	public FleTEPacket(INetEventHandler handler, byte aType) 
	{
		super(true, handler.getBlockPos());
		type = aType;
		contain = handler.onEmmit(type);
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
			((INetEventListener) pos.getBlockTile()).onReseave(type, contain);
		}
		world().markBlockForUpdate(pos.x, pos.y, pos.z);
		return null;
	}
}