package farcore.lib.net.tile;

import java.io.IOException;

import farcore.lib.net.PacketBlockCoord;
import farcore.lib.tile.ISynchronizableTile;
import farcore.network.IPacket;
import farcore.network.Network;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
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
	protected void encode(PacketBuffer output) throws IOException
	{
		super.encode(output);
		output.writeNBTTagCompoundToBuffer(nbt);
	}
	
	@Override
	protected void decode(PacketBuffer input) throws IOException
	{
		super.decode(input);
		nbt = input.readNBTTagCompoundFromBuffer();
	}
	
	@Override
	public IPacket process(Network network)
	{
		World world = world();
		if(world != null)
		{
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof ISynchronizableTile)
			{
				((ISynchronizableTile) tile).readFromDescription(nbt);
			}
		}
		return null;
	}
}