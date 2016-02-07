package fle.core.net;

import java.io.IOException;

import farcore.net.FleAbstractPacket;
import farcore.net.INetworkHandler;
import farcore.util.io.FleDataInputStream;
import farcore.util.io.FleDataOutputStream;
import fle.core.FLE;

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
	public Object process(INetworkHandler nwh)
	{
		FLE.fle.getKeyboard().processKeyUpdate(getPlayer(), keys);
		return null;
	}
}