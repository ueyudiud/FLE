/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.stack.fluid;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public interface IItemFluidContainer
{
	IItemFluidContainer INSTANCE = new IItemFluidContainer()
	{
		@Override
		public boolean hasFluid(ItemStack stack)
		{
			return false;
		}
		
		@Override
		public boolean canDrain(ItemStack stack)
		{
			return false;
		}
		
		@Override
		public boolean canFill(ItemStack stack, FluidStack resource)
		{
			return false;
		}
	};
	
	boolean hasFluid(ItemStack stack);
	
	boolean canDrain(ItemStack stack);
	
	boolean canFill(ItemStack stack, FluidStack resource);
}