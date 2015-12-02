package fle.core.net;

import fle.api.net.FleNetworkHandler;
import fle.api.net.FleSortInfomationPacket;
import fle.api.net.INetEventEmmiter;
import fle.api.net.INetEventListener;

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