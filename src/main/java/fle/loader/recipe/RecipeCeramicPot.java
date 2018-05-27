/*
 * copyright 2016-2018 ueyudiud
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
		addBoilingPotRecipe(new BaseStack(IBFS.iResources.getSubItem("crushed_bone")), new FluidStack(FluidRegistry.WATER, 250), null, 400, 600, IBFS.iResources.getSubItem("defatted_crushed_bone"), null);
	}
}
