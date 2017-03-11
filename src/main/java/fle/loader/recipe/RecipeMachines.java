/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addShapedRecipe;

import fle.loader.BlocksItems;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipeMachines
{
	public static void init()
	{
		addShapedRecipe(new ItemStack(BlocksItems.simple_wooden_workbench, 1, 0), "l", "g", 'l', "logWood", 'g', "pileGravel");
		addShapedRecipe(new ItemStack(BlocksItems.misc_wooden_machine, 1, 0), "ss", "ss", 's', "stickWood");
	}
}