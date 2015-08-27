package fle.api.gui;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import fle.api.FleAPI;
import fle.api.net.FlePackets.CoderTankUpdate;
import fle.api.te.IFluidTanks;
import fle.api.te.TEIT;

public abstract class InventoryWithFluidTank<T extends TEIT> extends InventoryTileBase<T> implements IFluidTank, IFluidTanks
{
	protected FluidTank tank;
	protected int maxHeat;

	public InventoryWithFluidTank(int size, int capacity) 
	{
		super(size);
		tank = new FluidTank(capacity);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		tank.readFromNBT(nbt.getCompoundTag("Fluid"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		NBTTagCompound nbt1 = new NBTTagCompound();
		tank.writeToNBT(nbt1);
		nbt.setTag("Fluid", nbt1);
	}
	
	public void syncTank(T tile)
	{
		if(!tile.getWorldObj().isRemote)
		{
			FleAPI.mod.getNetworkHandler().sendTo(new CoderTankUpdate(tile.getBlockPos()));
		}
	}

	@Override
	public void updateEntity(T tile) 
	{
		if(tank.getFluid() != null)
		{
			if(tank.getFluid().getFluid().getTemperature(tank.getFluid()) > maxHeat)
			{
				onMelted(tile);
			}
		}
	}
	
	protected abstract void onMelted(T tile);

	@Override
	public FluidStack getFluid() 
	{
		return tank.getFluid();
	}

	@Override
	public int getFluidAmount() 
	{
		return tank.getFluidAmount();
	}

	@Override
	public int getCapacity() 
	{
		return tank.getCapacity();
	}

	@Override
	public FluidTankInfo getInfo() 
	{
		return tank.getInfo();
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) 
	{
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) 
	{
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public int getSizeTank()
	{
		return 1;
	}

	@Override
	public IFluidTank getTank(int index)
	{
		return tank;
	}

	@Override
	public FluidStack getFluidStackInTank(int index)
	{
		return tank.getFluid();
	}

	@Override
	public void setFluidStackInTank(int index, FluidStack aStack)
	{
		tank.setFluid(aStack);
	}

	@Override
	public FluidStack drainTank(int index, int maxDrain, boolean doDrain)
	{
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public int fillTank(int index, FluidStack resource, boolean doFill)
	{
		return tank.fill(resource, doFill);
	}
}