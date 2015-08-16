package fle.core.gui.base;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import fle.api.util.FleFluidTank;

public abstract class InventoryITBase<T extends TileEntity> extends InventoryTileBase<T> implements IFluidHandler
{
	private final FleFluidTank[] tanks;

	public InventoryITBase(int aSize, int aCapacity)
	{
		super(aSize);
		tanks = new FleFluidTank[]{new FleFluidTank(aCapacity)};
	}
	public InventoryITBase(int aSize, Fluid targetFluid, int aCapacity)
	{
		super(aSize);
		tanks = new FleFluidTank[]{new FleFluidTank(targetFluid, aCapacity)};
	}
	public InventoryITBase(int aSize, FleFluidTank...aTanks)
	{
		super(aSize);
		tanks = aTanks;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		NBTTagList list = nbt.getTagList("Tanks", 10);
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound nbt1 = list.getCompoundTagAt(i);
			int slotId = nbt1.getByte("SlotId");
			tanks[slotId].readFromNBT(nbt1);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < tanks.length; ++i)
		{
			if(tanks[i] == null) continue;
			NBTTagCompound nbt1 = tanks[i].writeToNBT(new NBTTagCompound());
			nbt1.setByte("SlotId", (byte) i);
			list.appendTag(nbt1);
		}
		nbt.setTag("Tanks", list);
	}
	
	protected IFluidTank getTankFromDirection(ForgeDirection dir)
	{
		return tanks == null ? null : tanks[0];
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) 
	{
		return new FluidTankInfo[]{getTankFromDirection(from).getInfo()};
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) 
	{
		return getTankFromDirection(from).fill(resource, doFill);
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) 
	{
		return resource != null ? getTankFromDirection(from).drain(resource.amount, doDrain) : null;
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return getTankFromDirection(from).drain(maxDrain, doDrain);
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) 
	{
		return getTankFromDirection(from) != null;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) 
	{
		return getTankFromDirection(from) != null;
	}
}