/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addShapelessRecipe;

import fle.api.recipes.instance.RecipeMaps;
import fle.core.recipe.RecipePortableWoodwork1;
import fle.core.recipe.RecipePortableWoodwork2;
import fle.loader.BlocksItems;

/**
 * @author ueyudiud
 */
public class RecipeResource1
{
	public static void init()
	{
		addShapelessRecipe(BlocksItems.miscResources.getSubItem("ramie_rope"), BlocksItems.miscResources.getSubItem("dry_ramie_fiber"), 4);
		addShapelessRecipe(BlocksItems.miscResources.getSubItem("ramie_rope_bundle"), BlocksItems.miscResources.getSubItem("ramie_rope"), 4);
		addShapelessRecipe(BlocksItems.miscResources.getSubItem("ramie_rope", 4), BlocksItems.miscResources.getSubItem("ramie_rope_bundle"));
		
		RecipeMaps.PORTABLE_WOODWORK.addRecipe(new RecipePortableWoodwork1());
		RecipeMaps.PORTABLE_WOODWORK.addRecipe(new RecipePortableWoodwork2());
	}
}