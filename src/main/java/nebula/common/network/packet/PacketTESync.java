package nebula.common.network.packet;

import java.io.IOException;

import nebula.common.network.IPacket;
import nebula.common.network.Network;
import nebula.common.network.PacketBufferExt;
import nebula.common.tile.ISynchronizableTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketTESync extends PacketBlockCoord
{
	private NBTTagCompound nbt;
	
	public PacketTESync()
	{
		
	}
	
	public PacketTESync(World world, BlockPos pos, NBTTagCompound nbt)
	{
		super(world, pos);
		this.nbt = nbt;
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		super.encode(output);
		output.writeCompoundTag(this.nbt);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		super.decode(input);
		this.nbt = input.readCompoundTag();
	}
	
	@Override
	public IPacket process(Network network)
	{
		World world = world();
		if (world != null)
		{
			TileEntity tile = world.getTileEntity(this.pos);
			if (tile instanceof ISynchronizableTile)
			{
				((ISynchronizableTile) tile).readFromDescription(this.nbt);
			}
		}
		return null;
	}
}
