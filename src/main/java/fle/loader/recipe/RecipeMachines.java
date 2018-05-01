/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addRecipe;
import static fle.api.recipes.instance.RecipeAdder.addShapedRecipe;

import farcore.data.M;
import farcore.data.MC;
import farcore.data.SubTags;
import fle.api.recipes.ShapedFleDataRecipe;
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
		addRecipe(ShapedFleDataRecipe.builder(new ItemStack(IBFS.bWoodenMachine, 1, 1),
				" sr",
				"ppp")
				.v_nbt("F", SubTags.WOOD, "frame")
				.v_nbt("S", SubTags.ROCK, "mill")
				.t_m('s', MC.stone.orePrefix, "S", MC.stone::extractMaterial)
				.t('r', IBFS.iResources.getSubItem("ramie_rope"))
				.t_m('p', MC.plank.orePrefix, "F", MC.plank::extractMaterial)
				.b());
	}
}
