package farcore.lib.net;

import java.io.IOException;

import farcore.util.io.DataStream;
import net.minecraft.world.World;

public abstract class PacketBlockCoord extends PacketWorld
{
	protected int x;
	protected int y;
	protected int z;
	
	public PacketBlockCoord()
	{
		
	}
	public PacketBlockCoord(World world, int x, int y, int z)
	{
		super(world);
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	protected void encode(DataStream output) throws IOException
	{
		super.encode(output);
		output.writeInt(x);
		output.writeShort(y);
		output.writeInt(z);
	}
	
	@Override
	protected void decode(DataStream input) throws IOException
	{
		super.decode(input);
		x = input.readInt();
		y = input.readShort();
		z = input.readInt();
	}
}