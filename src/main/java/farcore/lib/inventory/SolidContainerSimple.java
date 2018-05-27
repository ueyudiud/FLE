/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.inventory;

import static nebula.common.inventory.IContainer.fully;
import static nebula.common.inventory.IContainer.process;
import static nebula.common.inventory.IContainer.skipAvailableCheck;

import farcore.lib.solid.SolidStack;
import farcore.lib.solid.Subsolid;
import nebula.common.inventory.ContainerSimple;
import nebula.common.inventory.task.Task;

/**
 * @author ueyudiud
 */
public abstract class SolidContainerSimple extends ContainerSimple<SolidStack> implements ISolidContainerSingle
{
	protected SolidContainerSimple(int capacity)
	{
		super(SolidStackHandler.SOLIDSTACK_HANDLER, capacity);
	}
	
	int modCount()
	{
		return this.modCount;
	}
	
	@Override
	protected abstract void set(SolidStack stack);
	
	@Override
	protected abstract SolidStack get();
	
	@Deprecated
	protected abstract void add(int size);
	
	@Override
	protected int getMaxCapacityFor(SolidStack stack)
	{
		return this.capacity;
	}
	
	@Override
	public SolidContainerSimpleSimulated simulated()
	{
		return new SolidContainerSimpleSimulated(this);
	}
	
	@Override
	public boolean isAvailable(Subsolid solid)
	{
		return true;
	}
	
	@Override
	public int getStackAmountInContainer()
	{
		return get() != null ? get().amount : 0;
	}
	
	@Override
	public int getRemainAmountInContainer()
	{
		return this.capacity - getStackAmountInContainer();
	}
	
	@Override
	public int incrStack(Subsolid solid, int amount, int modifier)
	{
		if (amount <= 0 || (!skipAvailableCheck(modifier) && !isAvailable(solid)))
		{
			return 0;
		}
		else if (get() == null)
		{
			if (amount > this.capacity)
			{
				if (fully(modifier))
				{
					return 0;
				}
				if (process(modifier))
				{
					set(solid.stack(this.capacity));
					onContainerChanged(modifier);
				}
				return this.capacity;
			}
			else
			{
				if (process(modifier))
				{
					set(solid.stack(amount));
					onContainerChanged(modifier);
				}
				return amount;
			}
		}
		else if (solid.match(get()))
		{
			int rem = this.capacity - get().amount;
			if (amount > rem)
			{
				if (fully(modifier))
				{
					return 0;
				}
				if (process(modifier))
				{
					add(rem);
					onContainerChanged(modifier);
				}
				return rem;
			}
			else
			{
				if (process(modifier))
				{
					add(amount);
					onContainerChanged(modifier);
				}
				return amount;
			}
		}
		else
		{
			return 0;
		}
	}
	
	@Override
	public int decrStack(Subsolid solid, int amount, int modifier)
	{
		if (amount <= 0 || get() == null)
		{
			return 0;
		}
		else if (solid.match(get()))
		{
			if (get().amount < amount)
			{
				if (fully(modifier))
				{
					return 0;
				}
				int result = get().amount;
				if (process(modifier))
				{
					set(null);
					onContainerChanged(modifier);
				}
				return result;
			}
			else
			{
				if (process(modifier))
				{
					add(-amount);
					onContainerChanged(modifier);
				}
				return amount;
			}
		}
		else
		{
			return 0;
		}
	}
	
	@Override
	public Task.TaskBTB taskIncr(Subsolid solid, int amount, int modifier)
	{
		return amount <= 0 ? Task.pass() : access -> {
			if (!skipAvailableCheck(modifier) && !isAvailable(solid))
			{
				return false;
			}
			else if (get() == null)
			{
				if (amount > this.capacity)
				{
					return false;
				}
				else
				{
					if (process(modifier))
					{
						set(solid.stack(amount));
						onContainerChanged(modifier);
					}
					return true;
				}
			}
			else if (solid.match(get()))
			{
				int rem = this.capacity - get().amount;
				if (amount > rem)
				{
					return false;
				}
				else
				{
					if (process(modifier))
					{
						add(amount);
						onContainerChanged(modifier);
					}
					return true;
				}
			}
			else
			{
				return false;
			}
		};
	}
	
	@Override
	public Task.TaskBTB taskDecr(Subsolid solid, int amount, int modifier)
	{
		return amount <= 0 ? Task.pass() : access -> {
			if (get() == null)
			{
				return false;
			}
			else if (solid.match(get()))
			{
				if (get().amount < amount || !access.test())
				{
					return false;
				}
				if (process(modifier))
				{
					if (get().amount > amount)
					{
						add(-amount);
						onContainerChanged(modifier);
					}
					else
					{
						set(null);
					}
				}
				return true;
			}
			else
			{
				return false;
			}
		};
	}
}
