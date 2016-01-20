package fle.core.net;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;
import farcore.net.FlePacketBuffer;
import farcore.net.INetworkHandler;
import farcore.net.IPacket;
import fle.FLE;

public class FleKeyTypePacket extends IPacket
{
	long state;
	
	public FleKeyTypePacket()
	{
		
	}
	public FleKeyTypePacket(long state)
	{
		this.state = state;
	}

	@Override
	public PacketBuffer encode(FlePacketBuffer buffer) throws IOException
	{
		buffer.writeLong(state);
		return buffer;
	}

	@Override
	public void decode(FlePacketBuffer buffer) throws IOException
	{
		state = buffer.readLong();
	}

	@Override
	public IPacket process(INetworkHandler handler)
	{
		FLE.fle.getKeyboard().processKeyUpdate(getPlayer(), state);
		return null;
	}
}