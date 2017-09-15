/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addBoilingPotRecipe;

import fle.loader.IBF;
import nebula.common.stack.BaseStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public class RecipeCeramicPot
{
	public static void init()
	{
		addBoilingPotRecipe(new BaseStack(IBF.iResources.getSubItem("crushed_bone")), new FluidStack(FluidRegistry.WATER, 1000),
				null, 400, 1000, IBF.iResources.getSubItem("defatted_crushed_bone"), null);
	}
}