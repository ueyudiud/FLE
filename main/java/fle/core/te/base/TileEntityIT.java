package fle.core.te.base;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import fle.core.gui.base.IInventoryITBase;

public abstract class TileEntityIT<T extends IInventoryITBase<?>> extends TileEntityInventory<T> implements IFluidHandler
{
	protected TileEntityIT(T inv) 
	{
		super(inv);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) 
	{
		return inv.fill(from, resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) 
	{
		return inv.drain(from, resource, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) 
	{
		return inv.drain(from, maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) 
	{
		return inv.canFill(from, fluid);
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) 
	{
		return inv.canDrain(from, fluid);
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) 
	{
		return inv.getTankInfo(from);
	}

	public abstract void updateEntity();
}