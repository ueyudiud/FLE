package farcore.lib.net.world;

import farcore.lib.net.PacketChunkCoord;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.util.U;
import net.minecraft.world.World;

public class PacketWorldDataAskUpdate extends PacketChunkCoord
{
	public PacketWorldDataAskUpdate()
	{
		
	}
	public PacketWorldDataAskUpdate(World world, int x, int z)
	{
		super(world, x, z);
	}
	
	@Override
	public IPacket process(NetworkBasic network)
	{
		int[] datas = U.Worlds.datas.saveChunkData(dimID, x, z);
		return datas.length == 65536 ?
				new PacketWorldDataUpdateAll(world(), x, z, datas) : null;
	}
}