package nebula.common.network.packet;

import java.io.IOException;

import nebula.common.network.IPacket;
import nebula.common.network.Network;
import nebula.common.network.PacketBufferExt;
import nebula.common.tile.ISynchronizableTile;
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
		return this.type != 0;
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		super.encode(output);
		output.writeByte(this.type);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		super.decode(input);
		this.type = input.readByte();
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
				if ((this.type & 0x1) != 0)
				{
					((ISynchronizableTile) tile).markBlockRenderUpdate();
				}
				if ((this.type & 0x2) != 0)
				{
					((ISynchronizableTile) tile).markLightForUpdate(EnumSkyBlock.SKY);
				}
				if ((this.type & 0x4) != 0)
				{
					((ISynchronizableTile) tile).markLightForUpdate(EnumSkyBlock.BLOCK);
				}
			}
		}
		return null;
	}
}
