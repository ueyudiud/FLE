package flapi.recipe;

import net.minecraft.item.ItemStack;
import flapi.recipe.stack.AbstractStack;

public interface SingleInputRecipe 
{
	public boolean match(ItemStack aInput);
	
	public ItemStack getResult(ItemStack aInput);
	
	public AbstractStack getShowStack();
}
