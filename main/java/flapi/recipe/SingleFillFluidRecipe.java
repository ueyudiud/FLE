package flapi.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import flapi.recipe.stack.FluidContainerStack;
import flapi.recipe.stack.AbstractStack;

public class SingleFillFluidRecipe implements SingleInputRecipe
{
	private FluidContainerStack stack;
	
	public SingleFillFluidRecipe(FluidContainerStack aStack) 
	{
		stack = aStack;
	}

	@Override
	public boolean match(ItemStack aInput) 
	{
		return stack.contain(aInput);
	}

	@Override
	public ItemStack getResult(ItemStack aInput) 
	{
		return aInput == null ? new ItemStack(Items.bucket) : stack.fillOrDrainFluid(aInput);
	}

	@Override
	public AbstractStack getShowStack() 
	{
		return stack;
	}
}