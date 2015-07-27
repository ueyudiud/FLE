package fle.api.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidStack;
import fle.api.FleAPI;

public class ItemFluidContainerStack extends ItemAbstractStack
{
	private boolean isDrain;
	private FluidStack stack;
	
	public ItemFluidContainerStack(boolean aDrain, FluidStack aStack) 
	{
		isDrain = aDrain;
		if(aStack != null)
			stack = aStack.copy();
	}
	
	ItemStack fillOrDrainFluid(ItemStack aStack)
	{
		if(isDrain)
		{
			return FluidContainerRegistry.drainFluidContainer(aStack);
		}
		else
		{
			return FluidContainerRegistry.fillFluidContainer(stack, aStack);
		}
	}

	@Override
	public boolean isStackEqul(ItemStack aStack) 
	{
		return aStack == null ? false : isDrain ? 
				(FluidContainerRegistry.isFilledContainer(aStack) ? FleAPI.fluidMatch(stack, FluidContainerRegistry.getFluidForFilledItem(aStack)) : false) :
					FluidContainerRegistry.fillFluidContainer(stack, aStack.copy()) != null;
	}

	@Override
	public boolean isStackEqul(FluidStack aStack) 
	{
		return FleAPI.fluidMatch(stack, aStack);
	}

	@Override
	public boolean isStackEqul(ItemAbstractStack aStack) 
	{
		return aStack instanceof ItemFluidContainerStack ? ((ItemFluidContainerStack) aStack).stack.isFluidStackIdentical(stack) && ((ItemFluidContainerStack) aStack).isDrain == isDrain : false;
	}
	
	private List<ItemStack> containerStacks;

	@Override
	public List<ItemStack> toArray() 
	{
		if(containerStacks == null)
		{
			containerStacks = new ArrayList();
			if(isDrain)
			{
				for(FluidContainerData tData : FluidContainerRegistry.getRegisteredFluidContainerData())
				{
					if(FleAPI.fluidMatch(stack, tData.fluid))
					{
						containerStacks.add(tData.filledContainer);
					}
				}
			}
			else
			{
				for(FluidContainerData tData : FluidContainerRegistry.getRegisteredFluidContainerData())
				{
					if(FleAPI.fluidMatch(stack, tData.fluid))
					{
						containerStacks.add(tData.emptyContainer);
					}
				}
			}
		}
		return containerStacks;
	}
}