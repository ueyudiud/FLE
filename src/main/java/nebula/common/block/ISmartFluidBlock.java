package nebula.common.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface ISmartFluidBlock
{
	/**
	 * Attempt to drain the block. This method should be called by devices such
	 * as pumps.
	 *
	 * NOTE: The block is intended to handle its own state changes.
	 *
	 * @param doDrain If false, the drain will only be simulated.
	 * @param maxDrain The max drain amount.
	 * @return
	 */
	FluidStack drain(World world, BlockPos pos, int maxDrain, boolean doDrain);
	
	/**
	 * Fill fluid to the block.
	 * 
	 * @param resource The resource of this action.
	 * @param doFill If false, the fill will only be simulated.
	 * @return
	 */
	int fill(World world, BlockPos pos, FluidStack resource, boolean doFill);
	
	Fluid getFluid();
}
