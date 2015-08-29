package fle.api.recipe;

import net.minecraft.item.ItemStack;

public interface SingleInputRecipe 
{
	public boolean match(ItemStack aInput);
	
	public ItemStack getResult(ItemStack aInput);
	
	public ItemAbstractStack getShowStack();
}
