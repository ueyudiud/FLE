package fle.core.net;

import java.io.IOException;

import flapi.net.FleAbstractPacket;
import flapi.net.FleNetworkHandler;
import flapi.util.io.FleDataInputStream;
import flapi.util.io.FleDataOutputStream;
import fle.FLE;

public class FleKeyTypePacket extends FleAbstractPacket
{
	int keys;
	
	public FleKeyTypePacket() 
	{
		
	}
	public FleKeyTypePacket(int key) 
	{
		keys = key;
	}
	
	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		os.writeInt(keys);
	}
	
	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		keys = is.readInt();	
	}
	
	@Override
	public Object process(FleNetworkHandler nwh)
	{
		FLE.fle.getKeyboard().processKeyUpdate(getPlayer(), keys);
		return null;
	}
}