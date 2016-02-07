package farcore.net;

import java.io.IOException;

import farcore.util.io.FleDataInputStream;
import farcore.util.io.FleDataOutputStream;
import farcore.world.IObjectInWorld;
import net.minecraft.world.World;

public class FleTEPacket extends FleCoordinatesPacket
{
	private byte type;
	private Object contain;
	
	public FleTEPacket()
	{
		super();
	}
	FleTEPacket(byte aType, Object aContain)
	{
		super();
		type = aType;
		contain = aContain;
	}
	public FleTEPacket(IObjectInWorld obj, byte aType, INetEventEmmiter emmiter) 
	{
		super(obj);
		type = aType;
		contain = emmiter.onEmit(type);
	}
	public FleTEPacket(INetEventHandler handler, byte aType) 
	{
		super(handler);
		type = aType;
		contain = handler.onEmit(type);
	}
	
	@Override
	protected void write(FleDataOutputStream buffer) throws IOException
	{
		super.write(buffer);
		buffer.writeByte(type);
		buffer.write(contain);
	}

	@Override
	protected void read(FleDataInputStream buffer) throws IOException
	{
		super.read(buffer);
		type = buffer.readByte();
		contain = buffer.read();
	}

	@Override
	public IPacket process(INetworkHandler nwh)
	{
		if(pos.tile() instanceof INetEventListener)
		{
			((INetEventListener) pos.tile()).onReceive(type, contain);
		}
		pos.world().markBlockForUpdate(pos.x, pos.y, pos.z);
		return null;
	}
}