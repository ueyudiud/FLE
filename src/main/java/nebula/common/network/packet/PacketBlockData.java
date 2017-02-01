/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.network.packet;

import java.io.IOException;

import nebula.common.network.IPacket;
import nebula.common.network.Network;
import nebula.common.network.PacketAbstract;
import nebula.common.network.PacketBufferExt;
import nebula.common.world.chunk.ExtendedBlockStateRegister;

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