package flapi.recipe;

import net.minecraft.item.ItemStack;
import flapi.recipe.stack.ItemAbstractStack;

public interface SingleInputRecipe 
{
	public boolean match(ItemStack aInput);
	
	public ItemStack getResult(ItemStack aInput);
	
	public ItemAbstractStack getShowStack();
}
