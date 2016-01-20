package farcore.net;

import java.io.IOException;

import net.minecraft.world.World;
import farcore.world.IObjectInWorld;

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
	protected void write(FlePacketBuffer buffer) throws IOException
	{
		super.write(buffer);
		buffer.writeByte(type);
		buffer.write(contain);
	}

	@Override
	protected void read(FlePacketBuffer buffer) throws IOException
	{
		super.read(buffer);
		type = buffer.readByte();
		contain = buffer.read();
	}

	@Override
	public IPacket process(INetworkHandler nwh)
	{
		World world = world();
		if(world.getTileEntity(pos) instanceof INetEventListener)
		{
			((INetEventListener) world.getTileEntity(pos)).onReceive(type, contain);
		}
		world().markBlockForUpdate(pos);
		return null;
	}
}