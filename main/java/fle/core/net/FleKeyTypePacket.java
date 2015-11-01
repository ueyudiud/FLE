package fle.core.net;

import java.io.IOException;

import com.google.common.io.ByteArrayDataOutput;

import fle.FLE;
import fle.api.net.FleAbstractPacket;
import fle.api.net.FleNetworkHandler;
import fle.api.util.FleDataInputStream;
import fle.api.util.FleDataOutputStream;

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