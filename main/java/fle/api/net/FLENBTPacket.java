package fle.api.net;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fle.FLE;
import fle.api.te.ITEInWorld;
import fle.api.te.TEBase;
import fle.api.util.FleDataInputStream;
import fle.api.util.FleDataOutputStream;
import fle.api.world.BlockPos;

public class FLENBTPacket extends FleAbstractPacket<FLENBTPacket>
{
	public BlockPos pos;
	public NBTTagCompound nbt;
	
	public FLENBTPacket()
	{
	}
	public FLENBTPacket(ITEInWorld te)
	{
		pos = te.getBlockPos();
		te.getTileEntity().writeToNBT(nbt = new NBTTagCompound());
	}

	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		os.writeBlockPos(pos);
		os.writeNBT(nbt);
	}

	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		pos = is.readBlockPos();
		nbt = is.readNBT();
	}

	@Override
	public IMessage onMessage(FLENBTPacket message, MessageContext ctx)
	{
		World world = FLE.fle.getPlatform().getWorldInstance(message.pos.getDim());
		if(message.pos.getBlockTile() == null)
		{
			world.setTileEntity(message.pos.x, message.pos.y, message.pos.z, TileEntity.createAndLoadEntity(message.nbt));
		}
		else
		{
			message.pos.getBlockTile().readFromNBT(message.nbt);
			if(message.pos.getBlockTile() instanceof TEBase)
				((TEBase) message.pos.getBlockTile()).markRenderForUpdate();
		}
		return null;
	}
}