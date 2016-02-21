package flapi.net;

import java.io.IOException;

import flapi.util.io.FleDataInputStream;
import flapi.util.io.FleDataOutputStream;

public abstract class FleSortInfomationPacket extends FleAbstractPacket
{
	protected byte type;
	protected Object contain;
	
	public FleSortInfomationPacket()
	{
		
	}
	public FleSortInfomationPacket(byte aType, short b)
	{
		this(aType, new Short(b));
	}
	public FleSortInfomationPacket(byte aType, Object aContain)
	{
		type = aType;
		contain = aContain;
	}
	
	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		os.writeByte(type);
		os.write(contain);
	}
	
	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		type = is.readByte();
		contain = is.read();
	}
}