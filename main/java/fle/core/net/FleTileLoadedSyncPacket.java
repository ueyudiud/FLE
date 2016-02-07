package fle.core.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import farcore.FarCore;
import farcore.net.FleAbstractPacket;
import farcore.net.INetworkHandler;
import farcore.tile.ILoadInitTileEntity;
import farcore.util.FleLog;
import farcore.util.io.FleDataInputStream;
import farcore.util.io.FleDataOutputStream;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import scala.collection.generic.BitOperations.Int;
import sun.misc.OSEnvironment;

public class FleTileLoadedSyncPacket extends FleAbstractPacket
{
	int dim;
	List<int[]> pos;
	FleDataInputStream stream;
	
	public FleTileLoadedSyncPacket()
	{
		
	}
	public FleTileLoadedSyncPacket(int dim, List<int[]> pos)
	{
		this.dim = dim;
		this.pos = pos;
	}

	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		os.writeInt(dim);
		int length = pos.size();
		os.writeInt(length);
		World world = FarCore.getWorldInstance(dim);
		for(int[] is : pos)
		{
			TileEntity tile = world.getTileEntity(is[0], is[1], is[2]);
			if(!(tile instanceof ILoadInitTileEntity))
			{
				continue;
			}
			os.writeInt(is[0]);
			os.writeInt(is[1]);
			os.writeInt(is[2]);
			ILoadInitTileEntity load = (ILoadInitTileEntity) tile;
			try
			{
				load.sendData(os);
			}
			catch(Exception exception)
			{
				continue;
			}
		}
	}

	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		dim = stream.readInt();
		stream = is;
	}

	@Override
	public Object process(INetworkHandler nwh)
	{
		World world = FarCore.getWorldInstance(dim);
		try
		{
			int length = stream.readInt();
			for(int i = 0; i < length; ++i)
			{
				int x = stream.readInt();
				int y = stream.readInt();
				int z = stream.readInt();
				ILoadInitTileEntity tile = 
						(ILoadInitTileEntity) world.getTileEntity(x, y, z);
				try
				{
					tile.receiveData(stream);
				}
				catch(IOException exception)
				{
					FleLog.error(
							String.format("Fail to recieve data of position [%d, %d, %d]", x, y, z), 
							exception);
					throw new IOException();
				}
			}
		}
		catch (IOException e)
		{
			;
		}
		
		return null;
	}
}