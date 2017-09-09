/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addDirtMixtureInputRecipe;
import static fle.api.recipes.instance.RecipeAdder.addDirtMixtureOutputRangedRecipe;

import farcore.data.M;
import farcore.data.MC;
import farcore.data.SubTags;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import nebula.common.stack.OreStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipeDirtMixture
{
	public static void init()
	{
		for (Mat material : Mat.filt(SubTags.DIRT))
		{
			addDirtMixtureInputRecipe(new OreStack(MC.pile.getOreName(material)),
					material, MC.pile.size);
			addDirtMixtureInputRecipe(new OreStack(MC.soil.getOreName(material)),
					material, MC.soil.size);
			addDirtMixtureOutputRangedRecipe(ItemMulti.createStack(material, MC.pile), MC.pile.size,
					material, 0.99F, 1.0F);
		}
		addDirtMixtureOutputRangedRecipe(new ItemStack(Items.CLAY_BALL, 2), MC.pile.size,
				M.latrosol, 0.45F, 0.55F, M.latroaluminosol, 0.45F, 0.55F);
	}
}