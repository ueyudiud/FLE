package fle.core.net;

import flapi.net.FleNetworkHandler;
import flapi.net.FleSortInfomationPacket;
import flapi.net.INetEventEmmiter;
import flapi.net.INetEventListener;

public class FleGuiPacket extends FleSortInfomationPacket
{
	public FleGuiPacket()
	{
		
	}
	public FleGuiPacket(byte aType, short contain) 
	{
		super(aType, contain);
	}
	public FleGuiPacket(byte aType, Object contain) 
	{
		super(aType, a(aType, contain));
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
	public Object process(FleNetworkHandler nwh)
	{
		if(getPlayer().openContainer instanceof INetEventListener)
		{
			((INetEventListener) getPlayer().openContainer).onReceive(type, contain);
		}
		return null;
	}
}