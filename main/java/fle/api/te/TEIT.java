package fle.api.te;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import fle.api.inventory.InventoryWithFluidTank;

public abstract class TEIT<T extends InventoryWithFluidTank<?>> extends TEInventory<T> implements IFluidHandler, IFluidTanks
{
	protected TEIT(T inv) 
	{
		super(inv);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) 
	{
		return inv.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) 
	{
		return inv.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) 
	{
		return inv.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) 
	{
		return inv.getFluid() != null ? inv.getFluid().getFluid() == fluid : true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) 
	{
		return inv.getFluid() != null ? inv.getFluid().getFluid() == fluid : false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) 
	{
		return new FluidTankInfo[]{new FluidTankInfo(inv.getTank(0))};
	}

	public abstract void updateEntity();

	@Override
	public int getSizeTank()
	{
		return inv.getSizeTank();
	}

	@Override
	public IFluidTank getTank(int index)
	{
		return inv.getTank(index);
	}

	@Override
	public FluidStack getFluidStackInTank(int index)
	{
		return inv.getFluidStackInTank(index);
	}

	@Override
	public void setFluidStackInTank(int index, FluidStack aStack)
	{
		inv.setFluidStackInTank(index, aStack);
	}

	@Override
	public FluidStack drainTank(int index, int maxDrain, boolean doDrain)
	{
		return inv.drainTank(index, maxDrain, doDrain);
	}

	@Override
	public int fillTank(int index, FluidStack resource, boolean doFill)
	{
		return inv.fillTank(index, resource, doFill);
	}
}