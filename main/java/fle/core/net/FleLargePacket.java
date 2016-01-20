package fle.core.net;

import io.netty.buffer.Unpooled;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import farcore.net.FlePacketBuffer;
import farcore.net.INetworkHandler;
import farcore.net.IPacket;
import farcore.util.FleLog;

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
	public FlePacketBuffer encode(FlePacketBuffer output) throws IOException
	{
		//ByteArrayDataOutput output = ByteStreams.newDataOutput();
		output.writeByteArray(bs);
		return output;
	}

	@Override
	public void decode(FlePacketBuffer s) throws IOException
	{
		byte[] b = s.readByteArray();
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
	public IPacket process(INetworkHandler nwh)
	{
		if(!flag) return null;
		try
		{
			nwh.onPacket(id, new FlePacketBuffer(Unpooled.buffer().writeBytes(largePacketCache.toByteArray())));
			largePacketCache = null;
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(e);
		}
		return null;
	}
}