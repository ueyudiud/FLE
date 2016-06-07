package farcore.lib.net;

import java.io.IOException;

import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.network.PacketAbstract;
import farcore.util.U;
import farcore.util.io.DataStream;
import net.minecraft.world.World;

public abstract class PacketWorld extends PacketAbstract
{
	protected int dimID;
	
	public PacketWorld()
	{
		
	}
	public PacketWorld(World world)
	{
		this.dimID = world.provider.dimensionId;
	}

	@Override
	protected void encode(DataStream output) throws IOException
	{
		output.writeShort(dimID);
	}

	@Override
	protected void decode(DataStream input) throws IOException
	{
		dimID = input.readShort();
	}
	
	public World world()
	{
		return U.Worlds.world(dimID);
	}
}