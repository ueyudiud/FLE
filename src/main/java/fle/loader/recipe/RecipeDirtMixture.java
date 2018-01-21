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
import fle.loader.IBFS;
import nebula.common.stack.OreStack;

/**
 * @author ueyudiud
 */
public class RecipeDirtMixture
{
	public static void init()
	{
		for (Mat material : Mat.filt(MC.pile_purified, true))
		{
			addDirtMixtureInputRecipe(new OreStack(MC.pile.getOreName(material)), material, MC.pile_purified.size);
		}
		for (Mat material : Mat.filt(MC.clayball, true))
		{
			addDirtMixtureInputRecipe(new OreStack(MC.clayball.getOreName(material)), material, MC.pile.size);
		}
		for (Mat material : Mat.filt(SubTags.DIRT))
		{
			addDirtMixtureInputRecipe(new OreStack(MC.soil.getOreName(material)), material, MC.soil.size);
			addDirtMixtureOutputRangedRecipe(ItemMulti.createStack(material, MC.pile), MC.pile.size, material, 0.99F, 1.0F);
		}
		for (Mat material : Mat.filt(SubTags.CLAY))
		{
			addDirtMixtureOutputRangedRecipe(IBFS.iResources.getSubItem("argil_ball"), MC.clayball.size, material, 0.6F, 0.67F, M.sand, 0.16F, 0.2F, M.limestone, 0.16F, 0.2F);
		}
	}
}
