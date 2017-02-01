package nebula.common.network.packet;

import java.io.IOException;

import nebula.common.network.PacketAbstract;
import nebula.common.network.PacketBufferExt;
import nebula.common.util.Worlds;
import net.minecraft.world.World;

public abstract class PacketWorld extends PacketAbstract
{
	protected int dimID;
	
	public PacketWorld()
	{
		
	}
	public PacketWorld(World world)
	{
		dimID = world.provider.getDimension();
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		output.writeShort(dimID);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		dimID = input.readShort();
	}
	
	public World world()
	{
		return Worlds.world(dimID);
	}
}