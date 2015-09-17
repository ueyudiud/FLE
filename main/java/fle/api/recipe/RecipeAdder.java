package fle.api.recipe;

import net.minecraft.item.ItemStack;

public interface RecipeAdder
{
	public void addPolishRecipe(ItemAbstractStack input, String map, ItemStack output);
	
	public void addDryingRecipe(ItemAbstractStack input, int tick, ItemStack output);
}