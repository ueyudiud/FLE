/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.inventory;

import farcore.lib.solid.SolidStack;
import nebula.common.inventory.IContainerSingle;

/**
 * @author ueyudiud
 */
public class SolidContainerSingle extends SolidContainerSimple implements IContainerSingle<SolidStack>
{
	private SolidStack stack;
	
	public SolidContainerSingle(int capacity)
	{
		super(capacity);
	}
	
	@Override
	protected void set(SolidStack stack)
	{
		this.stack = SolidStack.copyOf(stack);
	}
	
	@Override
	protected SolidStack get()
	{
		if (this.stack != null && this.stack.amount <= 0)
		{
			this.stack = null;
		}
		return this.stack;
	}
	
	@Override
	protected void add(int size)
	{
		this.stack.amount += size;
	}
}
