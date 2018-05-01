/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.inventory;

import farcore.lib.solid.SolidStack;
import nebula.common.inventory.IStackHandler;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class SolidStackHandler implements IStackHandler<SolidStack>
{
	public static final SolidStackHandler SOLIDSTACK_HANDLER = new SolidStackHandler();
	
	@Override
	public SolidStack readFrom(NBTTagCompound nbt)
	{
		return SolidStack.loadFromNBT(nbt);
	}
	
	@Override
	public SolidStack readFrom(NBTTagCompound nbt, String key)
	{
		return SolidStack.loadFromNBT(nbt.getCompoundTag(key));
	}
	
	@Override
	public void writeTo(SolidStack stack, NBTTagCompound nbt)
	{
		stack.writeToNBT(nbt);
	}
	
	@Override
	public NBTTagCompound writeTo(NBTTagCompound nbt, String key, SolidStack stack)
	{
		if (stack != null)
		{
			nbt.setTag(key, stack.writeToNBT(new NBTTagCompound()));
		}
		return nbt;
	}
	
	@Override
	public SolidStack copy(SolidStack stack)
	{
		return SolidStack.copyOf(stack);
	}
	
	@Override
	public SolidStack copy(SolidStack stack, int newSize)
	{
		return SolidStack.sizeOf(stack, newSize);
	}
	
	@Override
	public int size(SolidStack stack)
	{
		return stack.amount;
	}
	
	@Override
	public int size(SolidStack stack, int newSize)
	{
		return stack.amount = newSize;
	}
	
	@Override
	public int add(SolidStack stack, int incrSize)
	{
		return stack.amount += incrSize;
	}
	
	@Override
	public int sub(SolidStack stack, int decrSize)
	{
		return stack.amount -= decrSize;
	}
	
	@Override
	public boolean isSimilar(SolidStack s1, SolidStack s2)
	{
		return s1.isSoildEqual(s2);
	}
	
	@Override
	public boolean isEqual(SolidStack s1, SolidStack s2)
	{
		return s1.isSoildEqual(s2);
	}
	
	@Override
	public boolean contains(SolidStack source, SolidStack target)
	{
		return source.containsStack(target);
	}
	
	@Override
	public SolidStack validate(SolidStack stack)
	{
		return stack == null || stack.amount <= 0 ? null : stack;
	}
}
