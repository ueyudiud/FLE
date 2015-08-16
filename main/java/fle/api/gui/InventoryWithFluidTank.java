package fle.api.gui;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import fle.core.gui.base.InventoryTileBase;

public abstract class InventoryWithFluidTank<T extends TileEntity> extends InventoryTileBase<T> implements IFluidTank
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
}