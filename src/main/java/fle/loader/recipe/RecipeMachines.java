/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addShapedRecipe;

import fle.loader.IBF;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipeMachines
{
	public static void init()
	{
		addShapedRecipe(new ItemStack(IBF.simple_wooden_workbench, 1, 0), "l", "g", 'l', "logWood", 'g', "pileGravel");
		addShapedRecipe(new ItemStack(IBF.misc_wooden_machine, 1, 0), "ss", "ss", 's', "stickWood");
	}
}