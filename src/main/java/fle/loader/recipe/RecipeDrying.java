/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addDringRecipe;

import farcore.data.MC;
import fle.loader.BlocksItems;
import fle.loader.Crops;
import nebula.common.stack.BaseStack;
import nebula.common.stack.OreStack;

/**
 * @author ueyudiud
 */
public class RecipeDrying
{
	public static void init()
	{
		addDringRecipe(new OreStack(MC.crop.getOreName(Crops.ramie)), 250000, 10.0F, BlocksItems.miscResources.getSubItem("dry_ramie_fiber"));
		addDringRecipe(new BaseStack(BlocksItems.crop.getSubItem("grass")), 150000, 10.0F, BlocksItems.miscResources.getSubItem("hay"));
		addDringRecipe(new BaseStack(BlocksItems.crop.getSubItem("broadleaf")), 250000, 20.0F, BlocksItems.miscResources.getSubItem("dry_broadleaf"));
		addDringRecipe(new BaseStack(BlocksItems.crop.getSubItem("coniferous")), 250000, 20.0F, BlocksItems.miscResources.getSubItem("dry_coniferous"));
	}
}