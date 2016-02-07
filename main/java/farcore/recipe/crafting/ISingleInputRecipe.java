package farcore.recipe.crafting;

import farcore.recipe.IRecipe;
import farcore.recipe.stack.IItemStackMatcher;
import net.minecraft.item.ItemStack;

public interface ISingleInputRecipe extends IItemStackMatcher, IRecipe
{
	ItemStack output(ItemStack stack);
	
	ItemStack displayOutput();
}