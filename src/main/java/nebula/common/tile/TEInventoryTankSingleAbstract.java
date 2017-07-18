/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.tile;

import javax.annotation.Nullable;

import farcore.lib.capability.IFluidHandler;
import nebula.common.fluid.FluidStackExt;
import nebula.common.fluid.FluidTankN;
import nebula.common.util.Direction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public abstract class TEInventoryTankSingleAbstract extends TEInventoryBasic
implements IFluidHandlerIO, IFluidHandler
{
	protected boolean syncTankState = true;
	
	public TEInventoryTankSingleAbstract(int size)
	{
		super(size);
	}
	
	protected abstract FluidTankN tank();
	
	protected boolean canAccessWithTank(Direction direction)
	{
		return true;
	}
	
	@Override
	public boolean canFill(Direction direction, FluidStack stack)
	{
		return canAccessWithTank(direction) && tank().canExtractFluidWithType(direction, stack);
	}
	
	@Override
	public boolean canDrain(Direction direction, @Nullable FluidStack stack)
	{
		return canAccessWithTank(direction) && tank().canInsertFluid(direction, stack);
	}
	
	@Override
	public int fill(Direction direction, FluidStack resource, boolean process)
	{
		if (canFill(direction, resource))
		{
			return tank().fill(resource, process);
		}
		return 0;
	}
	
	@Override
	public FluidStack drain(Direction direction, int maxAmount, boolean process)
	{
		if (canDrain(direction, null))
		{
			return tank().drain(maxAmount, process);
		}
		return null;
	}
	
	@Override
	public SidedFluidIOProperty getProperty(Direction direction)
	{
		return canAccessWithTank(direction) ? new SidedFluidIOTankNPropertyWrapper(tank()) : null;
	}
	
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
	
	protected int sendFluidTo(int maxAmount, Direction direction, boolean process)
	{
		FluidStack stack = tank().drain(maxAmount, false);
		if (stack == null) return 0;
		int amt = sendFluidStackTo(stack, direction, process);
		if (process) tank().drain(amt, true);
		return amt;
	}
}