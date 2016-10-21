package fle.api.recipes;

import farcore.lib.collection.ArrayIterator;
import fle.api.recipes.ShapedRecipeFluidInput.RecipeFluidInputConfig;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.fluids.FluidStack;

public class ShapelessRecipeFluidInput extends ShapelessRecipeInput<FluidStack, RecipeFluidInputConfig>
{
	@Override
	protected RecipeFluidInputConfig decode(ArrayIterator<Object> itr)
	{
		return ShapedRecipeFluidInput.decode$(itr);
	}

	@Override
	protected EnumActionResult matchInput(RecipeFluidInputConfig arg, FluidStack target)
	{
		return arg.input.isFluidEqual(target) ? arg.input.amount >= target.amount ? EnumActionResult.SUCCESS : EnumActionResult.FAIL : EnumActionResult.PASS;
	}

	@Override
	protected void onInput(int id, RecipeFluidInputConfig arg, ICraftingMatrix<FluidStack> matrix)
	{
		FluidStack stack = matrix.get(id);
		if(arg.useFluid)
		{
			if(stack.amount == arg.input.amount)
			{
				matrix.set(id, stack = null);
			}
			else
			{
				stack.amount -= arg.input.amount;
			}
		}
		if(arg.output != null && stack == null)
		{
			matrix.set(id, arg.output.copy());
		}
	}

	@Override
	protected boolean isValid(RecipeFluidInputConfig source)
	{
		return true;
	}
}