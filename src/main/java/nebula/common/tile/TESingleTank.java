/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.tile;

import farcore.data.Capabilities;
import farcore.lib.capability.IFluidHandlerHelper;
import nebula.common.fluid.FluidStackExt;
import nebula.common.fluid.FluidTankN;
import nebula.common.util.Direction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public abstract class TESingleTank extends TESynchronization implements IFluidHandlerHelper, IFluidHandlerIO
{
	public abstract FluidTankN getTank();
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		getTank().writeToNBT(nbt, "tank");
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		getTank().readFromNBT(nbt, "tank");
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		getTank().writeToNBT(nbt, "t");
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if (nbt.hasKey("t"))
		{
			getTank().readFromNBT(nbt, "t");
			markBlockRenderUpdate();
		}
	}
	
	@Override
	public boolean canExtractFluid(Direction to)
	{
		return getTank().canExtractFluid(to);
	}
	
	@Override
	public boolean canInsertFluid(Direction from, FluidStack stack)
	{
		return getTank().canInsertFluid(from, stack);
	}
	
	@Override
	public FluidStackExt extractFluid(int amount, Direction to, boolean simulate)
	{
		if (canExtractFluid(to))
		{
			FluidStackExt fluid = getTank().extractFluid(amount, to, simulate);
			if (fluid != null && !simulate)
			{
				syncToNearby();
			}
			return fluid;
		}
		return null;
	}
	
	@Override
	public FluidStackExt extractFluid(FluidStack suggested, Direction to, boolean simulate)
	{
		if (canExtractFluid(to))
		{
			FluidStackExt fluid = getTank().extractFluid(suggested, to, simulate);
			if (fluid != null && !simulate)
			{
				syncToNearby();
			}
			return fluid;
		}
		return null;
	}
	
	@Override
	public int insertFluid(FluidStack stack, Direction from, boolean simulate)
	{
		if (canInsertFluid(from, stack))
		{
			int amount = getTank().insertFluid(stack, from, simulate);
			if (amount > 0 && !simulate)
			{
				syncToNearby();
			}
			return amount;
		}
		return 0;
	}
	
	@Override
	public boolean canFill(Direction direction, FluidStack stack)
	{
		return canInsertFluid(direction, stack);
	}
	
	@Override
	public boolean canDrain(Direction direction, FluidStack stack)
	{
		return canExtractFluid(direction);
	}
	
	@Override
	public int fill(Direction direction, FluidStack resource, boolean process)
	{
		if (canFill(direction, resource))
		{
			int amount = getTank().fill(resource, process);
			if (amount > 0 && process)
			{
				syncToNearby();
			}
			return amount;
		}
		return 0;
	}
	
	@Override
	public FluidStack drain(Direction direction, int maxAmount, boolean process)
	{
		if (canDrain(direction, getTank().getFluid()))
		{
			FluidStackExt fluid = getTank().drain(maxAmount, process);
			if (fluid != null && process)
			{
				syncToNearby();
			}
			return fluid;
		}
		return null;
	}
	
	@Override
	public SidedFluidIOProperty getProperty(Direction direction)
	{
		return new SidedFluidIOTankNPropertyWrapper(getTank(), direction);
	}
	
	protected abstract boolean canAccessFluidHandlerFrom(EnumFacing facing);
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return (capability == Capabilities.CAPABILITY_FLUID && canAccessFluidHandlerFrom(facing)) || super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return (capability == Capabilities.CAPABILITY_FLUID && canAccessFluidHandlerFrom(facing)) ? Capabilities.CAPABILITY_FLUID.cast(new FluidHandlerWrapper(this, facing)) : super.getCapability(capability, facing);
	}
}
