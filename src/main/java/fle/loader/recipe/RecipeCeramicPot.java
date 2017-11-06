/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addBoilingPotRecipe;

import fle.loader.IBFS;
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
		addBoilingPotRecipe(new BaseStack(IBFS.iResources.getSubItem("crushed_bone")), new FluidStack(FluidRegistry.WATER, 1000),
				null, 400, 1000, IBFS.iResources.getSubItem("defatted_crushed_bone"), null);
	}
}