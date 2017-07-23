/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addStoneMillRecipe;

import nebula.common.stack.BaseStack;
import net.minecraft.init.Items;

/**
 * @author ueyudiud
 */
public class RecipeStoneMill
{
	public static void init()
	{
		addStoneMillRecipe(new BaseStack(Items.WHEAT), 100, null, null);
	}
}