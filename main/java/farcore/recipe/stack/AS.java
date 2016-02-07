package farcore.recipe.stack;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.util.Util;
import net.minecraft.item.ItemStack;

public abstract class AS implements IIStackCheckerMatcher
{
	protected int size;
	protected IItemChecker checker;
	protected ImmutableList<ItemStack> list;

	public AS(IItemChecker checker)
	{
		this(checker, 1);
	}
	public AS(IItemChecker checker, int size)
	{
		this.checker = checker;
		this.size = size;
	}
	
	@Override
	public int hashCode()
	{
		return checker.hashCode() * 31 + size;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj == this ? true : 
			obj == null ? false : obj.getClass().equals(getClass()) ?
					checker.equals(((AS) obj).checker) && ((AS) obj).size == size : false;
	}
	
	@Override
	public IItemChecker checker()
	{
		return checker;
	}

	@Override
	public List<ItemStack> list()
	{
		if(list == null)
		{
			List<ItemStack> list = checker().list();
			ItemStack[] stacks = list.toArray(new ItemStack[list.size()]);
			Util.setStacksSize(stacks, size);
			this.list = ImmutableList.copyOf(stacks);
		}
		return list;
	}
	
	@Override
	public int size()
	{
		return size;
	}
}