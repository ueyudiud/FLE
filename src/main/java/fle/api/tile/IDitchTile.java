/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.api.tile;

import farcore.lib.capability.IFluidHandlerHelper;
import farcore.lib.material.Mat;
import nebula.common.fluid.FluidTankN;
import nebula.common.util.Direction;
import nebula.common.world.IModifiableCoord;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public interface IDitchTile extends IModifiableCoord, IFluidHandlerHelper
{
	default Fluid getFluidContain()
	{
		return getTank().getFluid() != null ? getTank().getFluid().getFluid() : null;
	}
	
	Mat getMaterial();
	
	FluidTankN getTank();
	
	float getFlowHeight();
	
	boolean isLinked(Direction direction);
	
	void setLink(Direction direction, boolean state);
	
	@Override
	default boolean canFill(Direction direction, FluidStack stack)
	{
		switch (direction)
		{
		case D:
		case A:
		case B:
			return false;
		case Q:
			return getTank().canInsertFluid(direction, stack);
		default:
			return isLinked(direction) && getTank().canInsertFluid(direction, stack);
		}
	}
	
	@Override
	default boolean canDrain(Direction direction, FluidStack stack)
	{
		switch (direction)
		{
		case D:
		case A:
		case B:
			return false;
		case Q:
			return getTank().canInsertFluid(direction, stack);
		default:
			return isLinked(direction) && getTank().canInsertFluid(direction, stack);
		}
	}
	
	@Override
	default SidedFluidIOProperty getProperty(Direction direction)
	{
		return new IFluidHandlerHelper.SidedFluidIOTankNPropertyWrapper(getTank());
	}
	
	@Override
	default int fill(Direction direction, FluidStack resource, boolean process)
	{
		switch (direction)
		{
		case D:
		case A:
		case B:
			return 0;
		case Q:
			return getTank().fill(resource, process);
		default:
			return !isLinked(direction) ? 0 : getTank().fill(resource, process);
		}
	}
	
	@Override
	default FluidStack drain(Direction direction, FluidStack required, boolean process)
	{
		switch (direction)
		{
		case D:
		case A:
		case B:
			return null;
		case Q:
			return getTank().drain(required, process);
		default:
			return !isLinked(direction) ? null : getTank().drain(required, process);
		}
	}
	
	@Override
	default FluidStack drain(Direction direction, int maxAmount, boolean process)
	{
		switch (direction)
		{
		case D:
		case A:
		case B:
			return null;
		case Q:
			return getTank().drain(maxAmount, process);
		default:
			return !isLinked(direction) ? null : getTank().drain(maxAmount, process);
		}
	}
}
