/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader.recipe;

import farcore.data.M;
import farcore.data.MC;
import fle.api.recipes.instance.RecipeAdder;
import fle.loader.IBF;
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
				IBF.iResources.getSubItem("flint_fragment"),
				IBF.iResources.getSubItem("flint_sharp"),
				IBF.iResources.getSubItem("flint_small"),
				new ItemStack(Items.FLINT),
				IBF.iResources.getSubItem("quartz_chip")},
				new int[][]{
			{6000, 2000, 1000},
			{2000, 500},
			{10000, 3000, 1000},
			{3000, 1000},
			{200, 50}});
	}
}