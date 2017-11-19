/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.solid.container;

import farcore.lib.solid.SolidStack;
import farcore.lib.solid.container.ISolidHandler.ISoildHandlerProperty;

/**
 * @author ueyudiud
 */
public class SimpleSoildHandlerProperty implements ISoildHandlerProperty
{
	private int capacity;
	private SolidStack stack;
	private boolean canFill;
	private boolean canDrain;
	
	public SimpleSoildHandlerProperty(int capacity, SolidStack stack, boolean canFill, boolean canDrain)
	{
		this.capacity = capacity;
		this.stack = SolidStack.copyOf(stack);
		this.canFill = canFill;
		this.canDrain = canDrain;
	}
	
	@Override
	public SolidStack[] getContents()
	{
		return new SolidStack[] { this.stack };
	}
	
	@Override
	public int getCapacity()
	{
		return this.capacity;
	}
	
	@Override
	public boolean canFill()
	{
		return this.canFill;
	}
	
	@Override
	public boolean canDrain()
	{
		return this.canDrain;
	}
	
	@Override
	public boolean canFill(SolidStack stack)
	{
		return canFill() && (this.stack == null || this.stack.isSoildEqual(stack));
	}
	
	@Override
	public boolean canDrain(SolidStack stack)
	{
		return canDrain() && (this.stack != null && this.stack.isSoildEqual(stack));
	}
	
}
