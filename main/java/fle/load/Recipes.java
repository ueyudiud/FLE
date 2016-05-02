package fle.load;

import fle.load.recipe.Crafting;
import fle.load.recipe.Washing;

public class Recipes
{
	public static void init()
	{
		Crafting.init();
		Washing.init();
	}
}