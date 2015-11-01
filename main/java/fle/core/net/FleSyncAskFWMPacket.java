package fle.core.net;

import java.io.IOException;

import fle.FLE;
import fle.api.net.FleCoordinatesPacket;
import fle.api.net.FleNetworkHandler;
import fle.api.util.FleDataInputStream;
import fle.api.util.FleDataOutputStream;
import fle.api.world.BlockPos;

public class FleSyncAskFWMPacket extends FleCoordinatesPacket
{	
	byte type;
	
	public FleSyncAskFWMPacket()
	{
		super(true);
	}
	public FleSyncAskFWMPacket(byte aType, BlockPos pos)
	{
		super(true, pos);
		type = aType;
		
	}
	
	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		super.write(os);
		os.writeByte(type);
	}
	
	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		super.read(is);
		type = is.readByte();
	}
	
	@Override
	public Object process(FleNetworkHandler nwh)
	{
		switch(type)
		{
		case 0 : FLE.fle.getWorldManager().markPosForUpdate(pos());
		break;
		case 1 : return FLE.fle.getWorldManager().createPacket(pos().getDim(), pos());
		}
		return null;
	}
}