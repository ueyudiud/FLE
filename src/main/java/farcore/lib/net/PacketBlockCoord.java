package farcore.lib.net;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class PacketBlockCoord extends PacketWorld
{
	protected BlockPos pos;

	public PacketBlockCoord()
	{

	}
	public PacketBlockCoord(World world, BlockPos pos)
	{
		super(world);
		this.pos = pos;
	}

	@Override
	protected void encode(PacketBuffer output) throws IOException
	{
		super.encode(output);
		output.writeBlockPos(pos);
	}

	@Override
	protected void decode(PacketBuffer input) throws IOException
	{
		super.decode(input);
		pos = input.readBlockPos();
	}
}