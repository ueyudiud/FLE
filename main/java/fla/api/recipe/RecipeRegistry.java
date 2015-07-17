package fla.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public abstract class RecipeRegistry 
{
	protected RecipeRegistry() 
	{
		
	}
	
	public abstract void addCraftingRecipe(IRecipe recipe);
	
	public abstract void addWashingRecipe(DropInfo output);
	
	public abstract void addPolishRecipe(IItemChecker input, String map, ItemStack output);
}
