package nebula.common.network.packet;

import java.io.IOException;

import nebula.common.NebulaKeyHandler;
import nebula.common.network.IPacket;
import nebula.common.network.Network;
import nebula.common.network.PacketAbstract;
import nebula.common.network.PacketBufferExt;

public class PacketKey extends PacketAbstract
{
	long values;
	
	public PacketKey()
	{
	}
	public PacketKey(long value)
	{
		this.values = value;
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		output.writeLong(this.values);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		this.values = input.readLong();
	}
	
	@Override
	public IPacket process(Network network)
	{
		NebulaKeyHandler.onServerRecieve(getPlayer(), this.values);
		return null;
	}
}