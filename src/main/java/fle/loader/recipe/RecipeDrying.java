/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader.recipe;

import static fle.api.recipes.instance.RecipeAdder.addDringRecipe;

import farcore.data.MC;
import fle.api.recipes.TemplateRecipeMap.TemplateRecipe;
import fle.api.recipes.instance.RecipeMaps;
import fle.core.items.ItemSimpleFluidContainer;
import fle.loader.Crops;
import fle.loader.IBFS;
import nebula.common.data.Misc;
import nebula.common.fluid.container.IItemFluidContainer;
import nebula.common.fluid.container.IItemFluidContainerV1;
import nebula.common.fluid.container.IItemFluidContainerV2;
import nebula.common.stack.BaseStack;
import nebula.common.stack.OreStack;
import nebula.common.util.FluidStacks;
import nebula.common.util.Maths;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public class RecipeDrying
{
	public static void init()
	{
		addDringRecipe(new OreStack(MC.crop.getOreName(Crops.ramie)), 250000, 10.0F, IBFS.iResources.getSubItem("dry_ramie_fiber"));
		addDringRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("grass")), 150000, 10.0F, IBFS.iResources.getSubItem("hay"));
		addDringRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("broadleaf")), 250000, 20.0F, IBFS.iResources.getSubItem("dry_broadleaf"));
		addDringRecipe(new BaseStack(IBFS.iCropRelated.getSubItem("coniferous")), 250000, 20.0F, IBFS.iResources.getSubItem("dry_coniferous"));
		
		addFluidContainerDryingRecipe(new FluidStack(IBFS.fLacquer, 15), 5000, 10.0F, new FluidStack(IBFS.fLacquerDried, 10));
	}
	
	private static void addFluidContainerDryingRecipe(FluidStack input, int durationMul, float rainfall, FluidStack output)
	{
		input = input.copy(); output = output.copy();
		int gcd = Maths.gcd(input.amount, output.amount, durationMul);
		input.amount /= gcd;
		output.amount /= gcd;
		durationMul /= gcd;
		
		final FluidStack a = input;
		final FluidStack b = output;
		final int c = durationMul;
		RecipeMaps.DRYING.addRecipe(new TemplateRecipe<ItemStack>(stack-> {
			if (stack.getItem() instanceof IItemFluidContainer)
			{
				IItemFluidContainer rawContainer = ((IItemFluidContainer) stack.getItem());
				FluidStack result;
				if (rawContainer.isV1())
				{
					IItemFluidContainerV1 container = rawContainer.castV1();
					return (result = container.drain(stack,
							FluidStacks.sizeOf(a, Integer.MAX_VALUE), false)) != null && result.amount >= a.amount;
				}
				else//if (rawContainer.isV2())
				{
					IItemFluidContainerV2 container = rawContainer.castV2();
					return (result = container.getContain(stack)) != null && result.amount >= a.amount;
				}
			}
			return false;
		}, stack-> {
			IItemFluidContainer container = ((IItemFluidContainer) stack.getItem());
			return container.isV1() ?
					container.castV1().drain(stack, FluidStacks.sizeOf(a, Integer.MAX_VALUE), false).amount / a.amount * c :
						container.castV2().getContain(stack).amount / a.amount * c;
		}, Misc.anyTo(rainfall), stack-> {
			ItemStack stack1 = stack.copy();
			IItemFluidContainer container = ((IItemFluidContainer) stack.getItem());
			if (container.isV1())
			{
				FluidStack stack2 = FluidStacks.sizeOf(b, container.castV1().drain(stack1, FluidStacks.sizeOf(a, Integer.MAX_VALUE), true).amount * b.amount / a.amount);
				
				container.castV1().removeFluidFromContainer(stack1);
				container.castV1().setFluidInContainer(stack1, stack2);
			}
			else
			{
				FluidStack stack2 = FluidStacks.sizeOf(b, container.castV2().capacity(stack1) * b.amount / a.amount);
				
				container.castV2().drain(stack1, true);
				container.castV2().fill(stack1, stack2, true);
			}
			return stack1;
		}).setData(ItemSimpleFluidContainer.createItemStack("barrel", a), durationMul, rainfall, b));
	}
}