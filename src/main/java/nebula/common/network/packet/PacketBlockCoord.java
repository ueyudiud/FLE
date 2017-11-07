/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.network.packet;

import java.io.IOException;

import nebula.common.network.PacketBufferExt;
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
	protected void encode(PacketBufferExt output) throws IOException
	{
		super.encode(output);
		output.writeBlockPos(this.pos);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		super.decode(input);
		this.pos = input.readBlockPos();
	}
}
