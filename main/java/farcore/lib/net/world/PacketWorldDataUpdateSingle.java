package farcore.lib.net.world;

import java.io.IOException;

import farcore.enums.UpdateType;
import farcore.lib.net.PacketBlockCoord;
import farcore.lib.world.WorldDatas;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.util.io.DataStream;
import net.minecraft.world.World;

@Deprecated
public class PacketWorldDataUpdateSingle extends PacketBlockCoord
{
	short data;
	
	public PacketWorldDataUpdateSingle(World world, int x, int y, int z, int data)
	{
		super(world, x, y, z);
		this.data = (short) data;
	}
	
	@Override
	protected void encode(DataStream output) throws IOException
	{
		super.encode(output);
		output.writeShort(data);
	}
	
	@Override
	protected void decode(DataStream input) throws IOException
	{
		super.decode(input);
		data = input.readShort();
	}
	
	@Override
	public IPacket process(NetworkBasic network)
	{
		WorldDatas.setBlockData(dimID, x, y, z, data, UpdateType.ONLY);
		return null;
	}
}