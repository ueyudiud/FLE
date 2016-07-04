package farcore.alpha.lib;

import farcore.alpha.interfaces.tile.ISynchronizableTile;
import farcore.lib.net.PacketBlockCoord;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketTESAskRender extends PacketBlockCoord
{
	public PacketTESAskRender()
	{
		
	}
	public PacketTESAskRender(World world, int x, int y, int z)
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
				((ISynchronizableTile) tile).markBlockRenderUpdate();
			}
		}
		return null;
	}
}