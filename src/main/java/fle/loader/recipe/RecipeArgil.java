/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.loader.recipe;

import fle.api.recipes.instance.SimplyKilnRecipe;
import fle.loader.IBFS;
import nebula.common.stack.BaseStack;

/**
 * @author ueyudiud
 */
public class RecipeArgil
{
	public static void init()
	{
		SimplyKilnRecipe.addRecipe(new BaseStack(IBFS.iResources.getSubItem("argil_plate_unsmelted")), new BaseStack(IBFS.iResources.getSubItem("argil_plate")), 8, 540, 2000);
	}
}
