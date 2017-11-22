/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.fluid;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraftforge.fluids.FluidStack;

/**
 * The multi-tank (maybe only single tank) interface.
 * 
 * @author ueyudiud
 */
public interface IBasicTanks
{
	FluidStack[] toFluidArray();
	
	void fromFluidArray(FluidStack[] stacks);
	
	int getFluidSlots();
	
	int getMaxFluidAmount(int id);
	
	@Nullable FluidStack getStackInFluidTank(int id);
	
	default boolean hasStackInFluidTank(int id)
	{
		return getStackInFluidTank(id) != null;
	}
	
	void setFluid(int id, @Nullable FluidStack stack);
	
	default void removeFluid(int id)
	{
		setFluid(id, null);
	}
	
	default void removeAllFluid()
	{
		for (int i = 0; i < getFluidSlots(); ++i)
		{
			removeFluid(i);
		}
	}
	
	FluidStack descFluid(int id, int maxAmount, boolean process);
	
	default int descFluid(int id, FluidStack source, boolean process)
	{
		FluidStack stack = getStackInFluidTank(id);
		return stack != null && stack.isFluidEqual(source) ? descFluid(id, source.amount, process).amount : 0;
	}
	
	default boolean extractFluid(int id, FluidStack stack, boolean process)
	{
		if (stack == null) return true;
		FluidStack s = getStackInFluidTank(id);
		if (s != null && s.containsFluid(stack))
		{
			if (process)
			{
				descFluid(id, s.amount, true);
			}
			return true;
		}
		return false;
	}
	
	int incrFluid(int id, FluidStack stack, boolean process);
	
	default boolean insertFluid(int id, FluidStack stack, boolean process)
	{
		if (stack == null) return true;
		if (incrFluid(id, stack, false) == stack.amount)
		{
			if (process) incrFluid(id, stack, true);
			return true;
		}
		return false;
	}
	
	default <T> T insertAllFluid(FluidStack[] stacks, Supplier<T> supplier)
	{
		return insertAllFluid(0, getFluidSlots(), stacks, supplier);
	}
	
	<T> T insertAllFluid(int from, int to, FluidStack[] stacks, Supplier<T> supplier);
}
