package fle.core.net;

import java.io.IOException;

import net.minecraft.world.World;
import fle.api.FleAPI;
import fle.api.net.FleCoordinatesPacket;
import fle.api.net.FleNetworkHandler;
import fle.api.util.FleDataInputStream;
import fle.api.util.FleDataOutputStream;
import fle.api.world.BlockPos;

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
		BlockPos pos = pos();
		FleAPI.mod.getWorldManager().setDatas(pos, data, false);
		World world = world();
		if(world != null)
		{
			world.markBlockRangeForRenderUpdate(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z);
		}
		return null;
	}
}