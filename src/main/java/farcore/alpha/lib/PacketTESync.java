package farcore.alpha.lib;

import java.io.IOException;

import farcore.alpha.interfaces.tile.ISynchronizableTile;
import farcore.lib.net.PacketBlockCoord;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.util.io.DataStream;
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
	}
	
	@Override
	protected void decode(DataStream input) throws IOException
	{
		super.decode(input);
	}
	
	@Override
	public IPacket process(NetworkBasic network)
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