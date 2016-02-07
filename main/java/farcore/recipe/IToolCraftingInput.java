package farcore.recipe;

import farcore.recipe.stack.IItemStackMatcher;
import net.minecraft.item.ItemStack;

/**
 * Called when crafting item in crafting table.
 * @author ueyudiud
 *
 */
public interface IToolCraftingInput extends IItemStackMatcher, IRecipe
{
	ItemStack onToolUsing(ItemStack stack);
}