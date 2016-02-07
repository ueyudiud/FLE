package farcore.net;

import java.io.IOException;

import farcore.util.io.FleDataInputStream;
import farcore.util.io.FleDataOutputStream;
import farcore.world.BlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class FleNBTPacket extends FleCoordinatesPacket
{
	public NBTTagCompound nbt;
	
	public FleNBTPacket()
	{
		super();
	}
	public FleNBTPacket(INetEventHandler te)
	{
		super(te.pos());
		te.pos().tile().writeToNBT(nbt = new NBTTagCompound());
	}

	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		super.write(os);
		os.writeNBT(nbt);
	}

	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		super.read(is);
		nbt = is.readNBT();
	}
	
	@Override
	public Object process(INetworkHandler nwh)
	{
		if(pos.tile() == null)
		{
			pos.world().setTileEntity(pos.x, pos.y, pos.z, TileEntity.createAndLoadEntity(nbt));
		}
		else
		{
			pos.tile().readFromNBT(nbt);
//			if(pos.tile() instanceof TEBase)
//				((TEBase) pos.getBlockTile()).markRenderForUpdate();
		}
		return null;
	}
}