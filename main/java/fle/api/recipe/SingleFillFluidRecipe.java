package fle.api.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class SingleFillFluidRecipe implements SingleInputRecipe
{
	private ItemFluidContainerStack stack;
	
	public SingleFillFluidRecipe(ItemFluidContainerStack aStack) 
	{
		stack = aStack;
	}

	@Override
	public boolean match(ItemStack aInput) 
	{
		return stack.isStackEqul(aInput);
	}

	@Override
	public ItemStack getResult(ItemStack aInput) 
	{
		return aInput == null ? new ItemStack(Items.bucket) : stack.fillOrDrainFluid(aInput);
	}
}