package farcore.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * For fluid block caused.
 * Posted in {@link MinecraftForge#EVENT_BUS}
 * @author ueyudiud
 *
 */
public class FluidBlockEvent extends Event
{
	public final World world;
	public final BlockPos resource;
	public final BlockFluidBase block;
	
	public FluidBlockEvent(World world, BlockPos resource, BlockFluidBase block)
	{
		this.world = world;
		this.resource = resource;
		this.block = block;
	}
	
	@HasResult
	public static class FluidTouchBlockEvent extends FluidBlockEvent
	{
		public final BlockPos target;
		public final IBlockState targetState;
		private IBlockState endingTargetState;
		public final int amountStart;
		public int amount;
		
		public FluidTouchBlockEvent(World world, BlockPos resource, BlockPos target, IBlockState targetState, BlockFluidBase block, int amt)
		{
			super(world, resource, block);
			this.target = target;
			this.endingTargetState = this.targetState = targetState;
			this.amountStart = this.amount = amt;
		}
		
		public void setTargetState(IBlockState state)
		{
			this.endingTargetState = state;
		}
		
		public IBlockState getEndingTargetState()
		{
			return this.endingTargetState;
		}
	}
}