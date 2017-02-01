/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.tile;

import javax.annotation.Nullable;

import nebula.common.fluid.FluidStackExt;
import nebula.common.util.Direction;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public interface IFluidHandlerIO
{
	/**
	 * Match side can extract item.
	 * @return
	 */
	boolean canExtractFluid(Direction to);

	/**
	 * Match side can insert item.
	 * @param stack For asked stack, null for ask general behavior.
	 * @return
	 */
	boolean canExtractFluid(Direction from, @Nullable FluidStack stack);

	FluidStackExt extractFluid(int size, Direction to, boolean simulate);
	
	FluidStackExt extractFluid(FluidStack suggested, Direction to, boolean simulate);

	int insertFluid(FluidStack stack, Direction from, boolean simulate);
}