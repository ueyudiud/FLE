package farcore.lib.net.tile;

import java.io.IOException;

import farcore.lib.io.DataStream;
import farcore.lib.net.PacketBlockCoord;
import farcore.lib.tile.ISynchronizableTile;
import farcore.network.IPacket;
import farcore.network.Network;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketTESync extends PacketBlockCoord
{
	private NBTTagCompound nbt;
	public PacketTESync()
	{
		
	}
	public PacketTESync(World world, int x, int y, int z, NBTTagCompound nbt)
	{
		super(world, x, y, z);
		this.nbt = nbt;
	}
	
	@Override
	protected void encode(DataStream output) throws IOException
	{
		super.encode(output);
		output.writeNBT(nbt);
	}
	
	@Override
	protected void decode(DataStream input) throws IOException
	{
		super.decode(input);
		nbt = input.readNBT();
	}
	
	@Override
	public IPacket process(Network network)
	{
		World world = world();
		if(world != null)
		{
			TileEntity tile = world.getTileEntity(x, y, z);
			if(tile instanceof ISynchronizableTile)
			{
				((ISynchronizableTile) tile).readFromDescription(nbt);
			}
		}
		return null;
	}	
}