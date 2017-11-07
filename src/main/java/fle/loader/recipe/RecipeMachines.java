/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addShapedRecipe;

import farcore.data.M;
import farcore.data.MC;
import fle.loader.IBFS;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipeMachines
{
	public static void init()
	{
		addShapedRecipe(new ItemStack(IBFS.bWoodenWorkbench, 1, 0), "l", "g", 'l', MC.log.getOreName(M.wood), 'g', MC.pile.getOreName(M.gravel));
		addShapedRecipe(new ItemStack(IBFS.bWoodenMachine, 1, 0), "ss", "ss", 's', "stickWood");
	}
}
