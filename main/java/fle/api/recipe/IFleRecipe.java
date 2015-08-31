package fle.api.recipe;

import net.minecraft.item.crafting.IRecipe;
import fle.api.cg.RecipesTab;

public interface IFleRecipe extends IRecipe
{
	RecipesTab getRecipeTab();
}
