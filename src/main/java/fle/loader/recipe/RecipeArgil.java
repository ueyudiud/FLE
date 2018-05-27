/*
 * copyright 2016-2018 ueyudiud
 */
package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addCeramicRecipe;

import farcore.data.M;
import farcore.data.MC;
import farcore.lib.item.ItemMulti;
import fle.api.recipes.instance.SimplyKilnRecipe;
import fle.loader.IBFS;
import nebula.base.A;
import nebula.common.stack.BaseStack;
import nebula.common.stack.OreStack;

/**
 * @author ueyudiud
 */
public class RecipeArgil
{
	public static void init()
	{
		addCeramicRecipe(A.createArray(10, new byte[] {8, 16}), IBFS.iResources.getSubItem("argil_plate_unsmelted"));
		addCeramicRecipe(A.createArray(10, new byte[] {28, 36}), IBFS.iResources.getSubItem("argil_brick_unsmelted"));
		
		SimplyKilnRecipe.addRecipe(new BaseStack(IBFS.iResources.getSubItem("argil_plate_unsmelted")), new BaseStack(IBFS.iResources.getSubItem("argil_plate")), 8, 540, 2000);
		SimplyKilnRecipe.addRecipe(new BaseStack(IBFS.iResources.getSubItem("argil_brick_unsmelted")), new BaseStack(ItemMulti.createStack(M.argil, MC.brick)), 8, 540, 2000);
		SimplyKilnRecipe.addRecipe(new OreStack(MC.pile.getOreName(M.limestone)), new BaseStack(IBFS.iResources.getSubItem("quick_lime_dust")), 8, 560, 3000);
		SimplyKilnRecipe.addRecipe(new OreStack(MC.pile.getOreName(M.marble)), new BaseStack(IBFS.iResources.getSubItem("quick_lime_dust")), 8, 560, 3000);
	}
}
