package fle.api.recipe;

import net.minecraft.item.crafting.IRecipe;
import fle.cg.RecipesTab;

public interface IFleRecipe extends IRecipe
{
	RecipesTab getRecipeTab();
}
