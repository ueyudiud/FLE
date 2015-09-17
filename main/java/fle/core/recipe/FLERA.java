package fle.core.recipe;

import net.minecraft.item.ItemStack;
import fle.api.cg.RecipesTab;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.RecipeAdder;
import fle.core.recipe.FLEDryingRecipe.DryingRecipe;
import fle.core.recipe.FLEPolishRecipe.PolishRecipe;

public class FLERA implements RecipeAdder
{
	@Override
	public void addPolishRecipe(ItemAbstractStack input, String map,
			ItemStack output)
	{
		FLEPolishRecipe.getInstance().registerRecipe(new PolishRecipe(RecipesTab.tabClassic, input, map, output.copy()));
	}

	@Override
	public void addDryingRecipe(ItemAbstractStack input, int tick,
			ItemStack output)
	{
		FLEDryingRecipe.getInstance().registerRecipe(new DryingRecipe(input, tick, output));
	}
}