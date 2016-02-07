package fle.core.net;

import java.io.IOException;

import farcore.net.FleAbstractPacket;
import farcore.net.INetEventEmmiter;
import farcore.net.INetEventListener;
import farcore.net.INetworkHandler;
import farcore.util.io.FleDataInputStream;
import farcore.util.io.FleDataOutputStream;

public class FleGuiPacket extends FleAbstractPacket
{
	byte type;
	Object contain;
	
	public FleGuiPacket()
	{
		
	}
	public FleGuiPacket(byte aType, short contain) 
	{
		this.type = aType;
		this.contain = contain;
	}
	public FleGuiPacket(byte aType, Object contain) 
	{
		this.type = aType;
		this.contain = a(aType, contain);
	}
	
	private static Object a(byte type, Object obj)
	{
		if(obj instanceof INetEventEmmiter)
		{
			return ((INetEventEmmiter) obj).onEmit(type);
		}
		return obj;
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
	
	@Override
	public Object process(INetworkHandler nwh)
	{
		if(getPlayer().openContainer instanceof INetEventListener)
		{
			((INetEventListener) getPlayer().openContainer).onReceive(type, contain);
		}
		return null;
	}
}