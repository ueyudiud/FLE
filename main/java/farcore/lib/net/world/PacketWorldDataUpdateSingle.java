package farcore.lib.net.world;

import java.io.IOException;

import farcore.enums.EnumUpdateType;
import farcore.lib.net.PacketBlockCoord;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.util.U;
import farcore.util.io.DataStream;
import net.minecraft.world.World;

public class PacketWorldDataUpdateSingle extends PacketBlockCoord
{
	short data;
	int type;
	
	public PacketWorldDataUpdateSingle(World world, int x, int y, int z, int data, int type)
	{
		super(world, x, y, z);
		this.data = (short) data;
		this.type = type;
	}
	
	@Override
	protected void encode(DataStream output) throws IOException
	{
		super.encode(output);
		output.writeShort(data);
		output.writeByte((byte) type);
	}
	
	@Override
	protected void decode(DataStream input) throws IOException
	{
		super.decode(input);
		data = input.readShort();
		type = input.readByte();
	}
	
	@Override
	public IPacket process(NetworkBasic network)
	{
		U.Worlds.datas.setSmartMetadataWithNotify(dimID, x, y, z, data, type);
		return null;
	}
}