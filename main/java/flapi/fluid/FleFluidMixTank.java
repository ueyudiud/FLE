package flapi.fluid;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

public class FleFluidMixTank implements IFluidTank
{
	long capacity;
	List<FluidStack> fluidList = new ArrayList();

	public FleFluidMixTank()
	{
		capacity = 1000;
	}
	public FleFluidMixTank(int aCapacity)
	{
		capacity = aCapacity;
	}
	
	public void setCapacity(long capacity)
	{
		this.capacity = capacity;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setLong("Capacity", capacity);
		NBTTagList list = new NBTTagList();
		for(FluidStack tStack : fluidList)
		{
			list.appendTag(tStack.writeToNBT(new NBTTagCompound()));
		}
		nbt.setTag("Fluids", list);
		return nbt;
	}
	
	public NBTTagCompound readFromNBT(NBTTagCompound nbt)
	{
		capacity = nbt.getLong("Capacity");
		fluidList.clear();
		NBTTagList list = nbt.getTagList("Fluids", 11);
		for(int i = 0; i < list.tagCount(); ++i)
		{
			fluidList.add(FluidStack.loadFluidStackFromNBT(list.getCompoundTagAt(i)));
		}
		return nbt;
	}
	
	private void checkNotNull()
	{
		FluidStack[] itr = fluidList.toArray(new FluidStack[fluidList.size()]);
		for(FluidStack stack : itr)
		{
			if(stack.amount == 0 || stack.getFluid() == null) 
				fluidList.remove(stack);
		}
	}
	private boolean checkAccept(FluidStack fluid)
	{
		for(FluidStack stack : fluidList)
		{
			if(stack.isFluidEqual(fluid))
			{
				stack.amount += fluid.amount;
				return true;
			}
		}
		return false;
	}
	public FluidStack checkContain(FluidStack target)
	{
		FluidStack ret = null;
		boolean flag = false;
		for(FluidStack stack : fluidList)
		{
			if(stack.isFluidEqual(target))
			{
				int out = Math.min(stack.amount, target.amount);
				stack.amount -= out;
				if(stack.amount <= 0)
					flag = true;
				ret = new FluidStack(target.getFluid(), out, stack.tag);
				break;
			}
		}
		if(flag) checkNotNull();
		return ret;
	}
	
	@Override
	public FluidStack getFluid()
	{
		checkNotNull();
		return fluidList.isEmpty() ? null : fluidList.get(0);
	}

	@Override
	public int getFluidAmount()
	{
		int ret = 0;
		FluidStack[] itr = fluidList.toArray(new FluidStack[fluidList.size()]);
		for(FluidStack stack : itr)
		{
			ret += stack.amount;
		}
		return ret;
	}

	@Override
	public int getCapacity()
	{
		return (int) capacity;
	}

	@Override
	public FluidTankInfo getInfo()
	{
		return new FluidTankInfo(this);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		int i = getCapacity() - getFluidAmount();
		if(resource == null) return 0;
		int ret = Math.min(i, resource.amount);
		if(!doFill)
		{
			return ret;
		}
		else
		{
			FluidStack fluid = resource.copy();
			fluid.amount = ret;
			if(!checkAccept(fluid))
			{
				fluidList.add(fluid);
			}
			return ret;
		}
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		if(getFluidAmount() == 0) return null;
		FluidStack stack = getFluid();
		if(!doDrain)
		{
			return new FluidStack(stack.getFluid(), Math.min(stack.amount, maxDrain), stack.tag);
		}
		else
		{
			FluidStack ret = new FluidStack(stack.getFluid(), Math.min(stack.amount, maxDrain), stack.tag);
			fluidList.get(0).amount -= Math.min(stack.amount, maxDrain);
			checkNotNull();
			return ret;
		}
	}
}