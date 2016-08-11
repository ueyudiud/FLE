package farcore.lib.net.tile;

import java.io.IOException;

import farcore.lib.net.PacketBlockCoord;
import farcore.lib.tile.ISynchronizableTile;
import farcore.network.IPacket;
import farcore.network.Network;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class PacketTESAsk extends PacketBlockCoord
{
	byte type;
	
	public PacketTESAsk()
	{

	}
	public PacketTESAsk(World world, BlockPos pos, int type)
	{
		super(world, pos);
		this.type = (byte) type;
	}
	
	@Override
	public boolean needToSend()
	{
		return type != 0;
	}
	
	@Override
	protected void encode(PacketBuffer output) throws IOException
	{
		super.encode(output);
		output.writeByte(type);
	}
	
	@Override
	protected void decode(PacketBuffer input) throws IOException
	{
		super.decode(input);
		type = input.readByte();
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
				if((type & 0x1) != 0)
				{
					((ISynchronizableTile) tile).markBlockRenderUpdate();
				}
				if((type & 0x2) != 0)
				{
					((ISynchronizableTile) tile).markLightForUpdate(EnumSkyBlock.SKY);
				}
				if((type & 0x4) != 0)
				{
					((ISynchronizableTile) tile).markLightForUpdate(EnumSkyBlock.BLOCK);
				}
			}
		}
		return null;
	}
}