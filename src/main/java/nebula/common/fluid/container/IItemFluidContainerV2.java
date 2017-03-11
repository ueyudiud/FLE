/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.fluid.container;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public interface IItemFluidContainerV2 extends IItemFluidContainer
{
	@Override
	default boolean hasFluid(ItemStack stack)
	{
		return getContain(stack) != null;
	}
	
	@Override
	default boolean isFull(ItemStack stack)
	{
		return getContain(stack) != null;
	}
	
	int capacity(ItemStack stack);
	
	ItemStack fill(ItemStack stack, FluidStack resource, boolean doFill);
	
	FluidStack getContain(ItemStack stack);
	
	@Nullable ItemStack drain(ItemStack stack);
}
