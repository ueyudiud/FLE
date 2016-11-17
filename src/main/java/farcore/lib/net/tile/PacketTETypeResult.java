package farcore.lib.net.tile;

import java.io.IOException;

import farcore.lib.net.PacketBlockCoord;
import farcore.lib.tile.instance.TELossTile;
import farcore.network.IPacket;
import farcore.network.Network;
import farcore.network.PacketBufferExt;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketTETypeResult extends PacketBlockCoord
{
	NBTTagCompound nbt;

	public PacketTETypeResult()
	{
	}
	public PacketTETypeResult(World world, BlockPos pos, TileEntity tile)
	{
		super(world, pos);
		nbt = tile.writeToNBT(new NBTTagCompound());
	}

	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		super.encode(output);
		output.writeNBTTagCompoundToBuffer(nbt);
	}

	@Override
	protected void decode(PacketBufferExt input) throws IOException
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
			if(tile instanceof TELossTile)
			{
				((TELossTile) tile).refreshTile(nbt);
			}
		}
		return null;
	}
}