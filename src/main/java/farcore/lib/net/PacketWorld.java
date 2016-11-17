package farcore.lib.net;

import java.io.IOException;

import farcore.network.PacketAbstract;
import farcore.network.PacketBufferExt;
import farcore.util.U;
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
		return U.Worlds.world(dimID);
	}
}