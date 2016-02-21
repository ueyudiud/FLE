package fle.core.item.behavior;

import fle.core.item.behavior.tool.BehaviorTool;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class BehaviorFluidTank extends BehaviorTool implements IFluidContainerItem
{
	@Override
	public FluidStack getFluid(ItemStack container) 
	{
		return null;
	}

	@Override
	public int getCapacity(ItemStack container) 
	{
		return 0;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill) 
	{
		return 0;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) 
	{
		return null;
	}
}