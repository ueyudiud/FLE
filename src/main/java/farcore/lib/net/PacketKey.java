package farcore.lib.net;

import java.io.IOException;

import farcore.handler.FarCoreKeyHandler;
import farcore.lib.io.DataStream;
import farcore.network.IPacket;
import farcore.network.Network;
import farcore.network.PacketAbstract;

public class PacketKey extends PacketAbstract
{
	long values;
	
	public PacketKey(long value)
	{
		this.values = value;
	}

	@Override
	protected void encode(DataStream output) throws IOException
	{
		output.writeLong(values);
	}

	@Override
	protected void decode(DataStream input) throws IOException
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