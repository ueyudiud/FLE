package fle.api.soild;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidTankInfo;

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
			if(get() == null) return Math.min(aStack.getSize(), cap);
			else if(stack.isStackEqul(aStack) && stack.isStackTagEqul(aStack))
				return Math.min(cap - stack.getSize(), aStack.getSize());
			return 0;
		}
		else
		{
			if(get() == null)
			{
				stack = aStack.copy();
				return aStack.getSize();
			}
			else if(stack.isStackEqul(aStack) && stack.isStackTagEqul(aStack))
			{
				int size = Math.min(cap - stack.getSize(), aStack.getSize());
				stack.addStackIn(size);
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
			return stack == null ? null : new SolidStack(stack.getObj(), Math.min(size(), maxDrain), stack.nbt);
		}
		else
		{
			int ret = Math.min(size(), maxDrain);
			SolidStack ss = new SolidStack(stack.getObj(), Math.min(size(), maxDrain), stack.nbt);
			stack.minusStackOut(ret);
			if(stack.getSize() <= 0) stack = null;
			return ss;
		}
	}
	
	public Solid get()
	{
		return stack == null ? null : stack.getObj();
	}
	
	public int size()
	{
		return stack == null ? 0 : stack.getSize();
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
		return aResource == null ? stack == null : has(aResource.getObj());
	}
	
	public boolean has(Solid aSolid)
	{
		return get() == null || aSolid == null ? get() == null && aSolid == null : aSolid.getSolidName().equals(get().getSolidName());
	}
}