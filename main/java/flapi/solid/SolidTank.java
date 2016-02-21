package flapi.solid;

import net.minecraft.nbt.NBTTagCompound;

public class SolidTank
{
	int cap;
	SolidStack stack = null;

	public SolidTank(int aCap, SolidStack aStack)
	{
		cap = aCap;
		stack = aStack != null ? aStack.copy() : null;
	}
	public SolidTank(int aCap)
	{
		cap = aCap;
	}
	
	public int getCapcity()
	{
		return cap;
	}
	
	public NBTTagCompound readFromNBT(NBTTagCompound nbt)
	{
		stack = SolidStack.readFromNBT(nbt);
		return nbt;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(stack != null)
			stack.writeToNBT(nbt);
		return nbt;
	}
	
	public int fill(SolidStack aStack, boolean doFill)
	{
		if(aStack == null) return 0;
		if(!doFill)
		{
			if(get() == null) return Math.min(aStack.size(), cap);
			else if(stack.isStackEqul(aStack) && stack.isStackTagEqul(aStack))
				return Math.min(cap - stack.size(), aStack.size());
			return 0;
		}
		else
		{
			if(get() == null)
			{
				stack = aStack.copy();
				return aStack.size();
			}
			else if(stack.isStackEqul(aStack) && stack.isStackTagEqul(aStack))
			{
				int size = Math.min(cap - stack.size(), aStack.size());
				stack.size += size;
				return size; 
			}
			return 0;
		}
	}
	
	public SolidStack drain(int maxDrain, boolean doDrain)
	{
		if(maxDrain <= 0 && size() <= 0) return null;
		if(!doDrain)
		{
			return stack == null ? null : new SolidStack(stack.get(), Math.min(size(), maxDrain), stack.nbt);
		}
		else
		{
			int ret = Math.min(size(), maxDrain);
			SolidStack ss = new SolidStack(stack.get(), Math.min(size(), maxDrain), stack.nbt);
			stack.size -= ret;
			if(stack.size() <= 0) stack = null;
			return ss;
		}
	}
	
	public Solid get()
	{
		return stack == null ? null : stack.get();
	}
	
	public int size()
	{
		return stack == null ? 0 : stack.size();
	}
	
	public SolidStack getStack()
	{
		return stack;
	}

	public SolidTankInfo getInfo()
	{
		return new SolidTankInfo(getStack(), getCapcity());
	}
	
	public void setStack(SolidStack aStack)
	{
		stack = aStack;
	}
	
	public boolean has(SolidStack aResource)
	{
		return aResource == null ? stack == null : has(aResource.get());
	}
	
	public boolean has(Solid solid)
	{
		return get() == null || solid == null ? get() == null && solid == null : get().getSolidName().equals(solid.getSolidName());
	}
}