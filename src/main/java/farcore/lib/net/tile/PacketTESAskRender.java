package farcore.lib.net.tile;

import farcore.lib.net.PacketBlockCoord;
import farcore.lib.tile.ISynchronizableTile;
import farcore.network.IPacket;
import farcore.network.Network;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketTESAskRender extends PacketBlockCoord
{
	public PacketTESAskRender()
	{

	}
	public PacketTESAskRender(World world, BlockPos pos)
	{
		super(world, pos);
	}

	@Override
	public IPacket process(Network network)
	{
		World world = world();
		if(world != null)
		{
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof ISynchronizableTile)
				((ISynchronizableTile) tile).markBlockRenderUpdate();
		}
		return null;
	}
}