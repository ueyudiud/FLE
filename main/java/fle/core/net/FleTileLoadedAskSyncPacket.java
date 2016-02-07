package fle.core.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import farcore.net.FleAbstractPacket;
import farcore.net.INetworkHandler;
import farcore.net.IPacket;
import farcore.util.io.FleDataInputStream;
import farcore.util.io.FleDataOutputStream;
import farcore.world.BlockPos;
import fle.core.FLE;
import net.minecraft.tileentity.TileEntity;

public class FleTileLoadedAskSyncPacket extends FleAbstractPacket
{
	private int dim;
	private List<int[]> pos;
	
	public FleTileLoadedAskSyncPacket()
	{
		pos = new ArrayList();
	}
	public FleTileLoadedAskSyncPacket(List<TileEntity> list)
	{
		this();
		dim = list.get(0).getWorldObj().provider.dimensionId;
		for(TileEntity tile : list)
		{
			pos.add(new int[]{tile.xCoord, tile.yCoord, tile.zCoord});
		}
	}
	
	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		os.writeInt(dim);
		int length = pos.size();
		os.writeInt(length);
		for(int[] p : pos)
		{
			os.writeInt(p[0]);
			os.writeInt(p[1]);
			os.writeInt(p[2]);
		}
	}
	
	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		dim = is.readInt();
		int length = is.readInt();
		for(int i = 0; i < length; ++i)
		{
			int x = is.readInt();
			int y = is.readInt();
			int z = is.readInt();
			pos.add(new int[]{x, y, z});
		}
	}
	
	@Override
	public Object process(INetworkHandler nwh)
	{
		FLE.fle.getNetworkHandler()
		.sendLargePacket(new FleTileLoadedSyncPacket(dim, pos), getPlayer());
		return null;
	}
}