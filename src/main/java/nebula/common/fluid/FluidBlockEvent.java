package nebula.common.fluid;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
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
	public final Block block;
	
	public FluidBlockEvent(World world, BlockPos resource, Block block)
	{
		this.world = world;
		this.resource = resource;
		this.block = block;
	}
	
	/**
	 * Called when fluid drain from container to world.<br>
	 * The drain action will be prevent if the event is canceled.
	 * @author ueyudiud
	 *
	 */
	@Cancelable
	public static class FluidDrainToWorldEvent extends FluidBlockEvent
	{
		public final FluidStack resource;
		private int used = -1;
		
		public FluidDrainToWorldEvent(World world, BlockPos resource, Block block, FluidStack stack)
		{
			super(world, resource, block);
			this.resource = stack;
		}
		
		public void setUsed(int used)
		{
			this.used = used;
		}
		
		public int getUsed()
		{
			return this.used;
		}
	}
	
	/**
	 * Called when fluid drain from world to container.<br>
	 * The fill action will be prevent if the event is canceled.
	 * @author ueyudiud
	 *
	 */
	@Cancelable
	public static class FluidFillFromWorldEvent extends FluidBlockEvent
	{
		public final int maxFill;
		public final @Nullable Fluid requiredFluid;
		private FluidStack ret = null;
		
		public FluidFillFromWorldEvent(World world, BlockPos resource, Fluid requiredFluid, int maxFill)
		{
			super(world, resource, null);
			this.requiredFluid = requiredFluid;
			this.maxFill = maxFill;
		}
		
		public void setResult(FluidStack result)
		{
			this.ret = result;
		}
		
		public FluidStack getRet()
		{
			return this.ret;
		}
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