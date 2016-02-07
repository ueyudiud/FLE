package farcore.recipe.stack;

import net.minecraft.item.ItemStack;

public interface IIStackCheckerMatcher extends IItemStackMatcher
{
	@Override
	default boolean match(ItemStack stack)
	{
		return stack == null ? false : 
			checker().match(stack) && stack.stackSize >= size();
	}
	
	@Override
	default int sizeRequire(ItemStack stack)
	{
		return size();
	}
	
	IItemChecker checker();
	
	int size();
}
