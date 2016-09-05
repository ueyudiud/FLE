package farcore.lib.net.tile;

import farcore.lib.net.PacketBlockCoord;
import farcore.lib.tile.ISynchronizableTile;
import farcore.lib.world.ICoord;
import farcore.network.IPacket;
import farcore.network.Network;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketTEAskType extends PacketBlockCoord
{
	public PacketTEAskType()
	{
	}
	public PacketTEAskType(ICoord coord)
	{
		super(coord.world(), coord.pos());
	}
	
	@Override
	public IPacket process(Network network)
	{
		World world = world();
		TileEntity tile = world.getTileEntity(pos);
		return new PacketTETypeResult(world, pos, tile);
	}
}