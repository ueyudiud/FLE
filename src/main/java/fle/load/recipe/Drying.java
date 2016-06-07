package fle.load.recipe;

import farcore.enums.EnumItem;
import farcore.lib.stack.OreStack;
import fle.api.recipe.machine.DryingRecipe;
import fle.api.recipe.machine.DryingRecipe.$Recipe;

public class Drying
{
	public static void init()
	{
		DryingRecipe.addRecipe("fle:leaves", new $Recipe(new OreStack("broadleaves"), 439, 392, 4000, EnumItem.plant_production.instance(1, "leaves_dry")));
	}
}