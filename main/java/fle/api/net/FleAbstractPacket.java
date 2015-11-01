package fle.api.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.util.internal.PlatformDependent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fle.api.util.FleDataInputStream;
import fle.api.util.FleDataOutputStream;

public abstract class FleAbstractPacket extends IPacket
{
	protected abstract void write(FleDataOutputStream os) throws IOException;
	
	protected abstract void read(FleDataInputStream is) throws IOException;
	
	public final void init(ByteArrayDataOutput output, boolean flag)
	{
		FleDataOutputStream os = new FleDataOutputStream(output);
		try
		{
			write(os);
			if(flag)
				os.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public ByteArrayDataOutput encode() throws IOException
	{
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		init(output, true);
		return output;
	}
	
	@Override
	public void decode(ByteArrayDataInput input)
			throws IOException
	{
		read(new FleDataInputStream(input));
	}
}