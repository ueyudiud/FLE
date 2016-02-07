package farcore.recipe.stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.item.ItemStack;

public class LM extends AM
{
	private ImmutableSet<IItemChecker> set;
	
	public LM(IItemChecker...lms)
	{
		set = ImmutableSet.copyOf(lms);
	}
	
	@Override
	public int hashCode()
	{
		return set.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this) return true;
		return obj instanceof LM ? ((LM) obj).set.equals(set) : false;
	}

	@Override
	public boolean match(ItemStack stack)
	{
		for(IItemChecker checker : set)
		{
			if(checker.match(stack)) return true;
		}
		return false;
	}

	@Override
	protected Object createList()
	{
		List<ItemStack> list = new ArrayList();
		for(IItemChecker checker : set)
		{
			list.addAll(checker.list());
		}
		return list;
	}
}