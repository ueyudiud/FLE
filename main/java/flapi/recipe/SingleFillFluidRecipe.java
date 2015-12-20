package flapi.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import flapi.recipe.stack.FluidContainerStack;
import flapi.recipe.stack.ItemAbstractStack;

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
		return stack.equal(aInput);
	}

	@Override
	public ItemStack getResult(ItemStack aInput) 
	{
		return aInput == null ? new ItemStack(Items.bucket) : stack.fillOrDrainFluid(aInput);
	}

	@Override
	public ItemAbstractStack getShowStack() 
	{
		return stack;
	}
}