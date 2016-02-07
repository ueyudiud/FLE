package farcore.net;

import java.io.IOException;

import farcore.util.io.FleDataInputStream;
import farcore.util.io.FleDataOutputStream;
import farcore.world.BlockPos;
import farcore.world.IObjectInWorld;
import net.minecraft.world.World;

public abstract class FleCoordinatesPacket extends FleAbstractPacket
{
	protected BlockPos pos;
	
	public FleCoordinatesPacket()
	{
		
	}
	public FleCoordinatesPacket(int t, int x, int y, int z)
	{
		this(new BlockPos(t, x, y, z));
	}
	public FleCoordinatesPacket(IObjectInWorld object)
	{
		this(object.pos());
	}
	public FleCoordinatesPacket(BlockPos pos)
	{
		this.pos = pos;
	}
	
	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		pos = is.readBlockPos();
	}
	
	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		os.writeBlockPos(pos);
	}
}