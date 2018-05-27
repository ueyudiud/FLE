/*
 * copyright 2016-2018 ueyudiud
 */
package fle.loader.recipe;

import static farcore.data.M.azurite;
import static farcore.data.M.copper;
import static farcore.data.M.cuprite;
import static farcore.data.M.malachite;
import static fle.api.recipes.instance.RecipeAdder.addSimpleReducingRecipe;

/**
 * @author ueyudiud
 */
public class RecipeSimpleReducing
{
	public static void init()
	{
		addSimpleReducingRecipe(malachite, copper);
		addSimpleReducingRecipe(azurite, copper);
		addSimpleReducingRecipe(cuprite, copper);
	}
}
