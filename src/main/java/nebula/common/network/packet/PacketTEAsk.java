package nebula.common.network.packet;

import nebula.common.network.IPacket;
import nebula.common.network.Network;
import nebula.common.tile.ISynchronizableTile;
import nebula.common.world.ICoord;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketTEAsk extends PacketBlockCoord
{
	public PacketTEAsk()
	{
		
	}
	
	public PacketTEAsk(World world, BlockPos pos)
	{
		super(world, pos);
	}
	
	public PacketTEAsk(ICoord coord)
	{
		this(coord.world(), coord.pos());
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
				((ISynchronizableTile) tile).syncToPlayer(getPlayer());
			}
		}
		return null;
	}
}
