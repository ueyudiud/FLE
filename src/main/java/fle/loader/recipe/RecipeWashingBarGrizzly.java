/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader.recipe;

import farcore.data.M;
import farcore.data.MC;
import fle.api.recipes.instance.RecipeAdder;
import fle.loader.BlocksItems;
import nebula.common.stack.OreStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipeWashingBarGrizzly
{
	public static void init()
	{
		RecipeAdder.addWashingBarGrizzlyRecipe(new OreStack(MC.pile.getOreName(M.gravel)), 100, new ItemStack[]{
				BlocksItems.miscResources.getSubItem("flint_fragment"),
				BlocksItems.miscResources.getSubItem("flint_sharp"),
				BlocksItems.miscResources.getSubItem("flint_small"),
				new ItemStack(Items.FLINT),
				BlocksItems.miscResources.getSubItem("quartz_chip")},
				new int[][]{
			{6000, 2000, 1000},
			{2000, 500},
			{10000, 3000, 1000},
			{3000, 1000},
			{200, 50}});
	}
}