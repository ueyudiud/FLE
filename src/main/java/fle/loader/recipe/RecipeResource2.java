/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addShapelessRecipe;

import javax.annotation.Nonnull;

import fle.api.recipes.SingleInputMatch;
import fle.loader.IBFS;
import nebula.common.fluid.container.FluidContainerHandler;
import nebula.common.fluid.container.IItemFluidContainer;
import nebula.common.item.ItemFluidDisplay;
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
		addShapelessRecipe(new ItemStack(Items.CLAY_BALL), fluidInput(new FluidStack(IBFS.fLimeMortar, 1000)));
	}
	
	static SingleInputMatch fluidInput(@Nonnull FluidStack input)
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
			
			@Override
			public ItemStack instance()
			{
				return ItemFluidDisplay.createFluidDisplay(input, true);
			}
		}, i -> FluidContainerHandler.drainContainer(i, input.amount).getKey());
	}
	
	static SingleInputMatch fluidOutput(@Nonnull FluidStack output)
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
				FluidStack stack1;
				return ((IItemFluidContainer) stack.getItem()).canFill(stack, output) && ((stack1 = FluidContainerHandler.getContain(stack)) == null || stack1.isFluidEqual(output));
			}
			
			@Override
			public ItemStack instance()
			{
				return IBFS.iFluidContainer.getSubItem("barrel");
			}
		}, i -> FluidContainerHandler.fillContainer(i, output).getKey());
	}
}
