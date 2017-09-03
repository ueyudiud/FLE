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
		addBoilingPotRecipe(new BaseStack(IBF.miscResources.getSubItem("crushed_bone")), new FluidStack(FluidRegistry.WATER, 1000),
				null, 400, 1000, IBF.miscResources.getSubItem("defatted_crushed_bone"), null);
	}
}