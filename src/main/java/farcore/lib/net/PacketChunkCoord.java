package farcore.lib.net;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;

public abstract class PacketChunkCoord extends PacketWorld
{
	protected int x;
	protected int z;

	public PacketChunkCoord()
	{

	}
	public PacketChunkCoord(World world, int x, int z)
	{
		super(world);
		this.x = x;
		this.z = z;
	}

	@Override
	protected void encode(PacketBuffer output) throws IOException
	{
		super.encode(output);
		output.writeInt(x);
		output.writeInt(z);
	}

	@Override
	protected void decode(PacketBuffer input) throws IOException
	{
		super.decode(input);
		x = input.readInt();
		z = input.readInt();
	}
}