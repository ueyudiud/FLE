package farcore.alpha.lib;

import farcore.alpha.interfaces.tile.ISynchronizableTile;
import farcore.lib.net.PacketBlockCoord;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketTEAsk extends PacketBlockCoord
{
	public PacketTEAsk()
	{
		
	}
	public PacketTEAsk(World world, int x, int y, int z)
	{
		super(world, x, y, z);
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
				((ISynchronizableTile) tile).syncToPlayer(getPlayer());
			}
		}
		return null;
	}
}