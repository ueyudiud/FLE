/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.stack.fluid;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public interface IItemFluidContainerV1 extends IItemFluidContainer
{
	int fill(ItemStack stack, FluidStack resource, boolean doFill);
	
	FluidStack drain(ItemStack stack, int maxDrain, boolean doDrain);
	
	FluidStack drain(ItemStack stack, FluidStack resource, boolean doDrain);
}