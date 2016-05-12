package fle.load;

import fle.load.recipe.Crafting;
import fle.load.recipe.Polishing;
import fle.load.recipe.Smelting;
import fle.load.recipe.Washing;

public class Recipes
{
	public static void init()
	{
		Fuels.init();
		Crafting.init();
		Washing.init();
		Polishing.init();
		Smelting.init();
	}
}