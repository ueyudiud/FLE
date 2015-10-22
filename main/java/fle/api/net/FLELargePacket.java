package fle.api.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ReadOnlyByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.util.internal.PlatformDependent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.InflaterInputStream;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fle.api.FleAPI;
import fle.api.util.FleDataInputStream;
import fle.api.util.FleDataOutputStream;
import fle.api.util.FleLog;
import fle.core.net.NetWorkHandler;
import fle.core.world.layer.FLELayer;

public class FLELargePacket extends FleAbstractPacket<FLELargePacket>
{
	private byte[] bs;
	private static ByteArrayOutputStream largePacketCache;
	
	private int id = 0;
	private boolean flag = false;
	
	public FLELargePacket()
	{
		
	}
	public FLELargePacket(byte[] arrays) 
	{
		bs = arrays;
	}

	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		os.writeBytes(bs);
	}

	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		DataInputStream stream = new DataInputStream(new ByteArrayInputStream(is.getBuf().array()));
		stream.readByte();//Skip a id.
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
	public IMessage onMessage(FLELargePacket message, MessageContext ctx)
	{
		if(!message.flag) return null;
		try
		{
			id = message.id;
			ByteArrayInputStream inflateInput = new ByteArrayInputStream(largePacketCache.toByteArray());
			InflaterInputStream inflate = new InflaterInputStream(inflateInput);
			ByteArrayOutputStream inflateBuffer = new ByteArrayOutputStream(16384);
	        int len;
			byte[] buffer = new byte[4096];
			while ((len = inflateInput.read(buffer)) != -1)
			{
				inflateBuffer.write(buffer, 0, len);
			}
			inflate.close();			
			ByteBuf subData = new UnpooledByteBufAllocator(PlatformDependent.directBufferPreferred()).buffer();
			subData.writeBytes(inflateBuffer.toByteArray());
			FleAPI.mod.getNetworkHandler().onPacket(id, subData, ctx);
			largePacketCache = null;
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(e);
		}
		return null;
	}
}