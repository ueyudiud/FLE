/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.tile;

import nebula.common.fluid.FluidStackExt;
import nebula.common.fluid.FluidTankN;
import nebula.common.util.Direction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public abstract class TEInventoryTankSingleAbstract extends TEInventoryBasic implements IFluidHandlerIO
{
	protected boolean syncTankState = true;
	
	public TEInventoryTankSingleAbstract(int size)
	{
		super(size);
	}
	
	protected abstract FluidTankN tank();
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		tank().writeToNBT(compound, "tank");
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		tank().readFromNBT(compound, "tank");
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		if (this.syncTankState)
		{
			tank().writeToNBT(nbt, "t");
		}
	}
	
	protected void onTankChanged()
	{
		if (this.syncTankState)
		{
			tank().readFromNBT1(this.nbt, "t");
		}
	}
	
	@Override
	public boolean canInsertFluid(Direction from, FluidStack stack)
	{
		return tank().canInsertFluid(from, stack);
	}
	
	@Override
	public boolean canExtractFluid(Direction to)
	{
		return tank().canExtractFluid(to);
	}
	
	@Override
	public FluidStackExt extractFluid(FluidStack suggested, Direction to, boolean simulate)
	{
		FluidStackExt stack = tank().extractFluid(suggested, to, simulate);
		if (!simulate && stack != null)
		{
			onTankChanged();
		}
		return stack;
	}
	
	@Override
	public FluidStackExt extractFluid(int size, Direction to, boolean simulate)
	{
		FluidStackExt stack = tank().extractFluid(size, to, simulate);
		if (!simulate && stack != null)
		{
			onTankChanged();
		}
		return stack;
	}
	
	@Override
	public int insertFluid(FluidStack stack, Direction from, boolean simulate)
	{
		int amt = tank().insertFluid(stack, from, simulate);
		if (!simulate && amt > 0)
		{
			onTankChanged();
		}
		return amt;
	}
}