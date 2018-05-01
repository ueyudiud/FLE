/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.inventory;

import farcore.lib.solid.SolidStack;

/**
 * @author ueyudiud
 */
public class SolidContainerArraySimple extends SolidContainerSimple
{
	private final SolidStack[] stacks;
	private final int id;
	
	public SolidContainerArraySimple(SolidStack[] stacks, int id, int limit)
	{
		super(limit);
		this.stacks = stacks;
		this.id = id;
	}
	
	@Override
	protected SolidStack get()
	{
		if (this.stacks[this.id].amount <= 0)
		{
			this.stacks[this.id] = null;
		}
		return this.stacks[this.id];
	}
	
	@Override
	protected void add(int amount)
	{
		this.stacks[this.id].amount += amount;
	}
	
	@Override
	protected void set(SolidStack stack)
	{
		this.stacks[this.id] = SolidStack.copyOf(stack);
	}
}
