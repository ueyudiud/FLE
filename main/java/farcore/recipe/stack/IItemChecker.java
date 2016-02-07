package farcore.recipe.stack;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IItemChecker
{
	boolean match(ItemStack stack);
	
	List<ItemStack> list();
}