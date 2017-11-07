/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addStoneMillRecipe;

import fle.loader.IBFS;
import nebula.common.stack.BaseStack;

/**
 * @author ueyudiud
 */
public class RecipeStoneMill
{
	public static void init()
	{
		addStoneMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("black_pepper")), 100, null, null);
		addStoneMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("turmeric_rhizome")), 100, null, null);
		addStoneMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("prickly_ash")), 100, null, null);
		addStoneMillRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("cinnamon")), 100, null, null);
	}
}
