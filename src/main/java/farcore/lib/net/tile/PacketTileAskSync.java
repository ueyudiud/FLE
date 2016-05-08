package farcore.lib.net.tile;

import farcore.interfaces.tile.IDescribableTile;
import farcore.lib.net.PacketBlockCoord;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketTileAskSync extends PacketBlockCoord
{
	public PacketTileAskSync()
	{
		
	}
	public PacketTileAskSync(World world, int x, int y, int z)
	{
		super(world, x, y, z);
	}

	@Override
	public IPacket process(NetworkBasic network)
	{
		TileEntity tile = world().getTileEntity(x, y, z);
		if(tile instanceof IDescribableTile)
		{
			((IDescribableTile) tile).markNBTSync(getPlayer());
		}
		return null;
	}
}