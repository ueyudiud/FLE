package fle.core.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import farcore.net.INetworkHandler;
import farcore.net.IPacket;
import farcore.util.FleLog;
import farcore.util.io.FleDataInputStream;

public class FleLargePacket extends IPacket
{
	private byte[] bs;
	private static ByteArrayOutputStream largePacketCache;
	
	private int id = 0;
	private boolean flag = false;
	
	public FleLargePacket()
	{
		
	}
	public FleLargePacket(byte[] arrays)
	{
		bs = arrays;
	}

	@Override
	public ByteArrayDataOutput encode() throws IOException
	{
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		output.writeShort(bs.length);
		output.write(bs);
		return output;
	}

	@Override
	public void decode(ByteArrayDataInput s) throws IOException
	{
		byte[] b = new byte[s.readShort()];
		s.readFully(b);
		DataInputStream stream = new DataInputStream(new ByteArrayInputStream(b));
		int type = stream.readInt();
		id = type >> 2;
		if((type & 0x1) != 0)
		{
			largePacketCache = new ByteArrayOutputStream();
		}
		byte[] buffer = new byte[4096];
        int len;
        while ((len = stream.read(buffer)) != -1)
        {
        	largePacketCache.write(buffer, 0, len);
        }
		if((type & 0x2) != 0)
		{
			flag = true;
		}
	}

	@Override
	public Object process(INetworkHandler nwh)
	{
		if(!flag) return null;
		try
		{
			
			nwh.onPacket(id, 
					ByteStreams.newDataInput(largePacketCache.toByteArray()));
			largePacketCache = null;
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(e);
		}
		return null;
	}
}