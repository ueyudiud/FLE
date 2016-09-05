package farcore.lib.net.world;

import farcore.lib.net.PacketBlockCoord;
import farcore.network.IPacket;
import farcore.network.Network;
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
			IBlockState state = world.getBlockState(pos);
			state.getBlock().breakBlock(world, pos, state);
			world.setBlockToAir(pos);
			if(world.isAreaLoaded(pos, 64))
			{
				Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(pos, state);
			}
		}
		return null;
	}
}