package fle.api.soild;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidTankInfo;

public class SolidTank
{
	int cap;
	SolidStack stack = null;
	
	public SolidTank(int aCap)
	{
		cap = aCap;
	}
	
	public int getCapcity()
	{
		return cap;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		stack = SolidStack.readFromNBT(nbt);
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		if(stack != null)
			stack.writeToNBT(nbt);
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
			return new SolidStack(stack.getObj(), Math.min(size(), maxDrain), stack.nbt);
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
}