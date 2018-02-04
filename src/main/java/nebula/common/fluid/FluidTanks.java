/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.fluid;

import java.util.function.Supplier;

import nebula.common.tile.IFluidHandlerIO;
import nebula.common.util.Direction;
import net.minecraftforge.fluids.FluidStack;

/**
 * TODO
 * @author ueyudiud
 */
public class FluidTanks implements IFluidHandlerIO, IBasicTanks
{
	@Override
	public boolean canExtractFluid(Direction to)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean canInsertFluid(Direction from, FluidStack stack)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public FluidStackExt extractFluid(int amount, Direction to, boolean simulate)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public FluidStackExt extractFluid(FluidStack suggested, Direction to, boolean simulate)
	{
		return null;
	}
	
	@Override
	public int insertFluid(FluidStack stack, Direction from, boolean simulate)
	{
		return 0;
	}
	
	@Override
	public FluidStack[] toFluidArray()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void fromFluidArray(FluidStack[] stacks)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getFluidSlots()
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getMaxFluidAmount(int id)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public FluidStack getStackInFluidTank(int id)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setFluid(int id, FluidStack stack)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public FluidStack descFluid(int id, int maxAmount, boolean process)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int incrFluid(int id, FluidStack stack, boolean process)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public <T> T insertAllFluid(int from, int to, FluidStack[] stacks, Supplier<T> supplier)
	{
		// TODO Auto-generated method stub
		return null;
	}
}