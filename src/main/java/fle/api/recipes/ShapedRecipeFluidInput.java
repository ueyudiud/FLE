package fle.api.recipes;

import fle.api.recipes.ShapedRecipeFluidInput.RecipeFluidInputConfig;
import nebula.common.base.ArrayIterator;
import net.minecraftforge.fluids.FluidStack;

public class ShapedRecipeFluidInput extends ShapedRecipeInput<FluidStack, RecipeFluidInputConfig>
{
	public static class RecipeFluidInputConfig
	{
		FluidStack input;
		boolean useFluid;
		FluidStack output;
		
		public RecipeFluidInputConfig(FluidStack input)
		{
			this(input, true, null);
		}
		public RecipeFluidInputConfig(FluidStack input, boolean useFluid, FluidStack output)
		{
			this.input = input;
			this.useFluid = useFluid;
			this.output = output;
		}
	}
	
	static RecipeFluidInputConfig decode$(ArrayIterator<Object> itr)
	{
		Object object = itr.next();
		if(object instanceof FluidStack)
			return new RecipeFluidInputConfig((FluidStack) object);
		if(object instanceof RecipeFluidInputConfig)
			return (RecipeFluidInputConfig) object;
		throw new RuntimeException();
	}

	@Override
	protected RecipeFluidInputConfig decode(ArrayIterator<Object> itr)
	{
		return decode$(itr);
	}

	@Override
	protected boolean matchInput(RecipeFluidInputConfig arg, FluidStack target)
	{
		return (target == null && arg.input == null) || target.containsFluid(arg.input);
	}

	@Override
	protected void onInput(int x, int y, RecipeFluidInputConfig arg, ICraftingMatrix<FluidStack> matrix)
	{
		FluidStack stack = matrix.get(x, y);
		if(arg.useFluid)
		{
			if(stack.amount == arg.input.amount)
			{
				matrix.set(x, y, stack = null);
			}
			else
			{
				stack.amount -= arg.input.amount;
			}
		}
		if(arg.output != null && stack == null)
		{
			matrix.set(x, y, arg.output.copy());
		}
	}

	@Override
	protected boolean isValid(RecipeFluidInputConfig arg)
	{
		return true;
	}
}