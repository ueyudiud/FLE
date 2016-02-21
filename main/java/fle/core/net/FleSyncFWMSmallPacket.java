package fle.core.net;

import java.io.IOException;

import net.minecraft.world.World;
import flapi.net.FleCoordinatesPacket;
import flapi.net.FleNetworkHandler;
import flapi.util.io.FleDataInputStream;
import flapi.util.io.FleDataOutputStream;
import flapi.world.BlockPos;
import fle.FLE;

public class FleSyncFWMSmallPacket extends FleCoordinatesPacket
{
	private short[] data;
	
	public FleSyncFWMSmallPacket() 
	{
		super(true);
	}
	public FleSyncFWMSmallPacket(BlockPos aPos, short[] aData) 
	{
		super(true, aPos);
		data = aData;
	}
	
	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		super.write(os);
		os.writeShortArray(data);
	}
	
	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		super.read(is);
		data = is.readShortArray();
	}
	
	@Override
	public Object process(FleNetworkHandler nwh)
	{
		World world = world();
		BlockPos pos = pos();
		FLE.fle.getWorldManager().syncMeta(world, pos, data);
		if(world != null)
		{
			world.markBlockForUpdate(pos.x, pos.y, pos.z);
			world.markBlockRangeForRenderUpdate(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z);
		}
		return null;
	}
}