package flapi.net;

import java.io.IOException;

import net.minecraft.world.World;
import flapi.FleAPI;
import flapi.util.io.FleDataInputStream;
import flapi.util.io.FleDataOutputStream;
import flapi.world.BlockPos;

public abstract class FleCoordinatesPacket extends FleAbstractPacket
{
	int dimID;
	int x;
	short y;
	int z;
	final boolean flag;
	
	public FleCoordinatesPacket(boolean aFlag)
	{
		flag = aFlag;
	}
	public FleCoordinatesPacket(boolean aFlag, int aDim, int aX, int aY, int aZ)
	{
		flag = aFlag;
		dimID = aDim;
		x = aX;
		y = (short) aY;
		z = aZ;
	}
	public FleCoordinatesPacket(boolean aFlag, BlockPos pos)
	{
		if(pos == null)
		{
			flag = aFlag;
			y = -1;
		}
		else
		{
			if(flag = aFlag)
				dimID = pos.getDim();
			x = pos.x;
			y = pos.y;
			z = pos.z;
		}
	}
	
	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		if(flag)
		{
			dimID = is.readInt();
		}
		x = is.readInt();
		y = is.readShort();
		z = is.readInt();
	}
	
	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		if(flag)
		{
			os.writeInt(dimID);
		}
		os.writeInt(x);
		os.writeShort(y);
		os.writeInt(z);
	}
	
	public World world()
	{
		return FleAPI.mod.getPlatform().getWorldInstance(dimID);
	}
	
	public BlockPos pos()
	{
		return new BlockPos(world(), x, y, z);
	}
}