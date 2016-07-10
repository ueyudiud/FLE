package farcore.lib.net;

import java.io.IOException;

import farcore.lib.io.DataStream;
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
	protected void encode(DataStream output) throws IOException
	{
		super.encode(output);
		output.writeInt(x);
		output.writeInt(z);
	}
	
	@Override
	protected void decode(DataStream input) throws IOException
	{
		super.decode(input);
		x = input.readInt();
		z = input.readInt();
	}
}