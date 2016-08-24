package farcore.lib.net.tile;

import farcore.lib.net.PacketBlockCoord;
import farcore.lib.tile.ISynchronizableTile;
import farcore.lib.world.ICoord;
import farcore.network.IPacket;
import farcore.network.Network;
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
		if(world != null)
		{
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof ISynchronizableTile)
			{
				((ISynchronizableTile) tile).syncToPlayer(getPlayer());
			}
		}
		return null;
	}
}