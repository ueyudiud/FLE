/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.net;

import java.io.IOException;

import farcore.lib.world.chunk.ExtendedBlockStateRegister;
import farcore.network.IPacket;
import farcore.network.Network;
import farcore.network.PacketAbstract;
import farcore.network.PacketBufferExt;

/**
 * @author ueyudiud
 */
public class PacketBlockData extends PacketAbstract
{
	public PacketBlockData()
	{
	}
	
	@Override
	public IPacket process(Network network) throws IOException
	{
		return null;
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		ExtendedBlockStateRegister.encode(output);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		ExtendedBlockStateRegister.decode(input);
	}
}