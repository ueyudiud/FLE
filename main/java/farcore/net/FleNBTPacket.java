package farcore.net;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import farcore.tileentity.TEBase;

public class FleNBTPacket extends FleCoordinatesPacket
{
	public NBTTagCompound nbt;
	
	public FleNBTPacket()
	{
		
	}
	public FleNBTPacket(INetEventHandler handler)
	{
		super(handler);
		handler.getWorld().getTileEntity(handler.getBlockPos()).writeToNBT(nbt = new NBTTagCompound());
	}

	@Override
	protected void write(FlePacketBuffer buffer) throws IOException
	{
		super.write(buffer);
		buffer.writeNBTTagCompoundToBuffer(nbt);
	}

	@Override
	protected void read(FlePacketBuffer buffer) throws IOException
	{
		super.read(buffer);
		nbt = buffer.readNBTTagCompoundFromBuffer();
	}
	
	@Override
	public IPacket process(INetworkHandler handler)
	{
		World world = world();
		if(world.getTileEntity(pos) == null)
		{
			world.setTileEntity(pos, TileEntity.createAndLoadEntity(nbt));
		}
		else
		{
			world.getTileEntity(pos).readFromNBT(nbt);
			if(world.getTileEntity(pos) instanceof TEBase)
				((TEBase) world.getTileEntity(pos)).markRenderForUpdate();
		}
		return null;
	}
}