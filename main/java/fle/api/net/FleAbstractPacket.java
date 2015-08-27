package fle.api.net;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fle.api.util.FleDataInputStream;
import fle.api.util.FleDataOutputStream;

public abstract class FleAbstractPacket<T extends FleAbstractPacket> implements IMessage, IMessageHandler<T, IMessage>
{
	protected abstract void write(FleDataOutputStream os) throws IOException;
	
	protected abstract void read(FleDataInputStream is) throws IOException;

	public abstract IMessage onMessage(T message, MessageContext ctx);
	
	private final void init(ByteBuf buf)
	{
		FleDataOutputStream os = new FleDataOutputStream(buf);
		try
		{
			write(os);
			os.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public final void fromBytes(ByteBuf buf)
	{
		try
		{
			read(new FleDataInputStream(buf));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public final void toBytes(ByteBuf buf)
	{
		init(buf);
	}
}