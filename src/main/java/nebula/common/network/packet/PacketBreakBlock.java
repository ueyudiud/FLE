package nebula.common.network.packet;

import nebula.common.network.IPacket;
import nebula.common.network.Network;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketBreakBlock extends PacketBlockCoord
{
	public PacketBreakBlock()
	{
	}
	public PacketBreakBlock(World world, BlockPos pos)
	{
		super(world, pos);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IPacket process(Network network)
	{
		World world = world();
		if(world != null)
		{
			IBlockState state = world.getBlockState(this.pos);
			state.getBlock().breakBlock(world, this.pos, state);
			world.setBlockToAir(this.pos);
			if(world.isAreaLoaded(this.pos, 64))
			{
				Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(this.pos, state);
			}
		}
		return null;
	}
}