/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.stack.fluid;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public interface IItemFluidContainerV2 extends IItemFluidContainer
{
	ItemStack fill(ItemStack stack, FluidStack resource, boolean doFill);
	
	FluidStack getContain(ItemStack stack);
	
	ItemStack drain(ItemStack stack);
}
