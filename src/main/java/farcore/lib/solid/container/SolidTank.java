/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.solid.container;

import javax.annotation.Nullable;

import farcore.lib.inventory.ISolidHandler;
import farcore.lib.solid.SolidStack;
import nebula.common.nbt.INBTSelfCompoundReaderAndWriter;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class SolidTank implements INBTSelfCompoundReaderAndWriter, ISolidHandler
{
	protected int			capacity;
	protected SolidStack	stack;
	
	public SolidTank(int capacity)
	{
		this.capacity = capacity;
	}
	
	public int getCapacity()
	{
		return this.capacity;
	}
	
	public SolidStack getStack()
	{
		return this.stack;
	}
	
	public void setStack(SolidStack stack)
	{
		this.stack = stack;
	}
	
	public int getSolidAmount()
	{
		return this.stack == null ? 0 : this.stack.amount;
	}
	
	/**
	 * Match stack can fill into tank, use for child type implementation.
	 * 
	 * @param stack
	 * @return
	 */
	public boolean canFill(@Nullable SolidStack stack)
	{
		return true;
	}
	
	/**
	 * Match can stack drain from tank, use for child type implementation.
	 * 
	 * @return
	 */
	public boolean canDrain()
	{
		return true;
	}
	
	/**
	 * Take <tt>insert</tt> action, means stack will be filled only source stack
	 * can fully insert into tank.
	 * 
	 * @param stack the source stack.
	 * @param simulate do stack in tank <i>not</i> changed in <tt>insert</tt>
	 *            action, if argument is <tt>true</tt>, the tank will only give
	 *            simulate result.
	 * @return return <tt>true</tt> if stack fully insert into tank.
	 * @see #fill(SolidStack, boolean)
	 */
	public boolean insertSolid(@Nullable SolidStack stack, boolean simulate)
	{
		if (stack == null) return true;
		if (fill(stack, false) == stack.amount)
		{
			if (!simulate) fill(stack, true);
			return true;
		}
		return false;
	}
	
	/**
	 * Take <tt>fill</tt> action, means stack will be filled if source stack can
	 * fill into tank if tank is empty or solid of stack in tank equals to solid
	 * of stack in fill source.
	 * 
	 * @param stack the source stack, will not changed in this method.
	 * @param doFill do stack in tank changed in <tt>fill</tt> action, if
	 *            argument is <tt>false</tt>, the tank will only give simulate
	 *            result.
	 * @return the amount fill into tank.
	 * @see #insertSolid(SolidStack, boolean)
	 * @see farcore.lib.solid.SolidStack#isSoildEqual(SolidStack)
	 */
	public int fill(SolidStack stack, boolean doFill)
	{
		if (stack == null || stack.amount == 0 || !canFill(stack)) return 0;
		if (this.stack == null)
		{
			int i = Math.min(this.capacity, stack.amount);
			if (doFill)
			{
				this.stack = SolidStack.sizeOf(stack, i);
			}
			return i;
		}
		else if (this.stack.isSoildEqual(stack))
		{
			int i = Math.min(this.capacity - this.stack.amount, stack.amount);
			if (doFill)
			{
				this.stack.amount += i;
			}
			return i;
		}
		else
			return 0;
	}
	
	public SolidStack drain(int maxAmount, boolean doDrain)
	{
		if (this.stack == null || maxAmount <= 0 || !canDrain()) return null;
		int i = Math.min(this.stack.amount, maxAmount);
		if (doDrain)
		{
			if (i == this.stack.amount)
			{
				SolidStack result = this.stack;
				this.stack = null;
				return result;
			}
			else
				return this.stack.splitStack(i);
		}
		return SolidStack.sizeOf(this.stack, i);
	}
	
	@Override
	public SolidStack drain(SolidStack source, boolean doDrain)
	{
		if (source == null || !source.isSoildEqual(this.stack))
			return null;
		return drain(source.amount, doDrain);
	}
	
	@Override
	public void readFrom(NBTTagCompound nbt)
	{
		this.stack = SolidStack.loadFromNBT(nbt);
	}
	
	@Override
	public void writeTo(NBTTagCompound nbt)
	{
		if (this.stack != null)
		{
			this.stack.writeToNBT(nbt);
		}
	}
	
	@Override
	public ISoildHandlerProperty getProperty()
	{
		return new SimpleSoildHandlerProperty(this.capacity, this.stack, canFill(null), canDrain());
	}
}
