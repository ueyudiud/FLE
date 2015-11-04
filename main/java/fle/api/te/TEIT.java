package fle.api.te;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import fle.api.cover.IFluidIOCover;
import fle.api.cover.IItemIOCover;
import fle.api.inventory.InventoryWithFluidTank;

public abstract class TEIT<T extends InventoryWithFluidTank> extends TEInventory<T> implements IFluidHandler, IFluidTanks
{
	protected TEIT(T inv) 
	{
		super(inv);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) 
	{
		if(from != ForgeDirection.UNKNOWN)
		{
			if(covers[from.ordinal()] instanceof IFluidIOCover)
			{
				return ((IFluidIOCover) covers[from.ordinal()]).fill(getBlockPos(), from, resource, doFill);
			}
		}
		return getTileInventory().fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) 
	{
		if(from != ForgeDirection.UNKNOWN)
		{
			if(covers[from.ordinal()] instanceof IFluidIOCover)
			{
				return ((IFluidIOCover) covers[from.ordinal()]).drain(getBlockPos(), from, resource, doDrain);
			}
		}
		return getTileInventory().drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) 
	{
		if(from != ForgeDirection.UNKNOWN)
		{
			if(covers[from.ordinal()] instanceof IFluidIOCover)
			{
				return ((IFluidIOCover) covers[from.ordinal()]).drain(getBlockPos(), from, maxDrain, doDrain);
			}
		}
		return getTileInventory().drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) 
	{
		if(from != ForgeDirection.UNKNOWN)
		{
			if(covers[from.ordinal()] instanceof IFluidIOCover)
			{
				return ((IFluidIOCover) covers[from.ordinal()]).canFill(getBlockPos(), from, fluid);
			}
		}
		return getTileInventory().getFluid() != null ? getTileInventory().getFluid().getFluid() == fluid : true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) 
	{
		if(from != ForgeDirection.UNKNOWN)
		{
			if(covers[from.ordinal()] instanceof IFluidIOCover)
			{
				return ((IFluidIOCover) covers[from.ordinal()]).canDrain(getBlockPos(), from, fluid);
			}
		}
		return getTileInventory().getFluid() != null ? getTileInventory().getFluid().getFluid() == fluid : false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) 
	{
		return new FluidTankInfo[]{new FluidTankInfo(getTileInventory().getTank(0))};
	}
	
	@Override
	public int getSizeTank()
	{
		return getTileInventory().getSizeTank();
	}

	@Override
	public IFluidTank getTank(int index)
	{
		return getTileInventory().getTank(index);
	}

	@Override
	public FluidStack getFluidStackInTank(int index)
	{
		return getTileInventory().getFluidStackInTank(index);
	}

	@Override
	public void setFluidStackInTank(int index, FluidStack aStack)
	{
		getTileInventory().setFluidStackInTank(index, aStack);
	}

	@Override
	public FluidStack drainTank(int index, int maxDrain, boolean doDrain)
	{
		return getTileInventory().drainTank(index, maxDrain, doDrain);
	}

	@Override
	public int fillTank(int index, FluidStack resource, boolean doFill)
	{
		return getTileInventory().fillTank(index, resource, doFill);
	}
}