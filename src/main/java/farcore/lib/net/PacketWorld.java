package farcore.lib.net;

import java.io.IOException;

import farcore.network.PacketAbstract;
import farcore.util.U;
import net.minecraft.network.PacketBuffer;
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
	protected void encode(PacketBuffer output) throws IOException
	{
		output.writeShort(dimID);
	}

	@Override
	protected void decode(PacketBuffer input) throws IOException
	{
		dimID = input.readShort();
	}

	public World world()
	{
		return U.Worlds.world(dimID);
	}
}