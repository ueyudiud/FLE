/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addShapelessRecipe;

import javax.annotation.Nonnull;

import fle.api.recipes.SingleInputMatch;
import fle.loader.IBF;
import nebula.common.fluid.container.FluidContainerHandler;
import nebula.common.fluid.container.IItemFluidContainer;
import nebula.common.stack.AbstractStack;
import nebula.common.util.FluidStacks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public class RecipeResource2
{
	public static void init()
	{
		addShapelessRecipe(new ItemStack(Items.CLAY_BALL), fluidInput(new FluidStack(IBF.fLimeMortar, 1000)));
	}
	
	private static SingleInputMatch fluidInput(@Nonnull FluidStack input)
	{
		return new SingleInputMatch(new AbstractStack()
		{
			@Override
			public int size(ItemStack stack)
			{
				return 1;
			}
			
			@Override
			public boolean similar(ItemStack stack)
			{
				if (stack == null || !(stack.getItem() instanceof IItemFluidContainer)) return false;
				return FluidStacks.containFluid(FluidContainerHandler.getContain(stack), input);
			}
		}, i-> FluidContainerHandler.drainContainer(i, input.amount).getKey());
	}
}