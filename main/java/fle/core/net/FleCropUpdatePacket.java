package fle.core.net;

import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import fle.FLE;
import fle.api.net.FleCoordinatesPacket;
import fle.api.net.FleNetworkHandler;
import fle.api.util.FleDataInputStream;
import fle.api.util.FleDataOutputStream;
import fle.api.world.BlockPos;
import fle.core.te.TileEntityCrop;

public class FleCropUpdatePacket extends FleCoordinatesPacket
{
	private int a;
	private double b;
	private int c;
	private String crop;
	
	public FleCropUpdatePacket()
	{
		super(true);
	}

	public FleCropUpdatePacket(TileEntityCrop t)
	{
		super(true, t.getWorldObj().provider.dimensionId, t.xCoord, t.yCoord, t.zCoord);
		a = t.getCropAge();
		b = t.getCropBuf();
		c = t.getCropCushion();
		crop = t.getCrop().getCropName();
	}
	
	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		super.write(os);
		os.writeInt(a);
		os.writeDouble(b);
		os.writeInt(c);
		os.writeString(crop);
	}
	
	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		super.read(is);
		a = is.readInt();
		b = is.readDouble();
		c = is.readInt();
		crop = is.readString();
	}

	@Override
	public Object process(FleNetworkHandler nwh)
	{
		BlockPos pos = pos();
		if(pos.getBlockTile() instanceof TileEntityCrop)
		{
			TileEntityCrop tile = (TileEntityCrop) pos.getBlockTile();
			tile.setupCrop(FLE.fle.getCropRegister().getCropFromName(crop));
			tile.setCropAge(a);
			tile.setCropBuf(b);
			tile.setCropCushion(c);
			if(tile.getWorldObj() != null)
				tile.markRenderForUpdate();
		}
		return null;
	}
}