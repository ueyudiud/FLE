package flapi.recipe;

import net.minecraft.item.crafting.IRecipe;
import flapi.cg.RecipesTab;

public interface IFleRecipe extends IRecipe
{
	RecipesTab getRecipeTab();
}
