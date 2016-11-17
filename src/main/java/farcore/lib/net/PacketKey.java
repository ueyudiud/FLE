package farcore.lib.net;

import java.io.IOException;

import farcore.handler.FarCoreKeyHandler;
import farcore.network.IPacket;
import farcore.network.Network;
import farcore.network.PacketAbstract;
import farcore.network.PacketBufferExt;

public class PacketKey extends PacketAbstract
{
	long values;
	
	public PacketKey(long value)
	{
		values = value;
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		output.writeLong(values);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		values = input.readLong();
	}
	
	@Override
	public IPacket process(Network network)
	{
		FarCoreKeyHandler.onServerRecieve(getPlayer(), values);
		return null;
	}
}