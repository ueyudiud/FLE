package farcore.recipe.stack;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IItemStackMatcher
{
	boolean match(ItemStack stack);
	
	int sizeRequire(ItemStack stack);
	
	List<ItemStack> list();
}