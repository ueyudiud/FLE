package farcore.net;

import java.io.IOException;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import farcore.world.IObjectInWorld;
import flapi.FleAPI;

public abstract class FleCoordinatesPacket extends FleAbstractPacket
{
	int dimID;
	protected BlockPos pos;
	
	public FleCoordinatesPacket()
	{
		
	}
	FleCoordinatesPacket(IObjectInWorld world)
	{
		this(world.getWorld().provider.getDimensionId(), world.getBlockPos());
	}
	public FleCoordinatesPacket(int dim, BlockPos pos)
	{
		dimID = dim;
		this.pos = pos;
	}
	
	@Override
	protected void read(FlePacketBuffer buffer) throws IOException
	{
		dimID = buffer.readInt();
		pos = buffer.readBlockPos();
	}
	
	@Override
	protected void write(FlePacketBuffer buffer) throws IOException
	{
		buffer.writeInt(dimID);
		buffer.writeBlockPos(pos);
	}
	
	public World world()
	{
		return FleAPI.mod.getPlatform().getWorldInstance(dimID);
	}
}