/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.fluid.container;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * @author ueyudiud
 */
public abstract interface IItemFluidContainer
{
	IItemFluidContainer INSTANCE = new IItemFluidContainer()
	{
		@Override
		public boolean hasFluid(ItemStack stack)
		{
			return false;
		}
		
		@Override
		public boolean isFull(ItemStack stack)
		{
			return true;
		}
		
		@Override
		public boolean canDrain(ItemStack stack)
		{
			return false;
		}
		
		@Override
		public boolean canFill(ItemStack stack, @Nullable FluidStack resource)
		{
			return false;
		}
		
		@Override
		public IFluidHandler createFluidHandlerWrapper(ItemStack stack)
		{
			return new FluidTank(0);
		}
	};
	
	/**
	 * To predict can drain fluid container.
	 * @param stack
	 * @return
	 */
	boolean hasFluid(ItemStack stack);
	
	/**
	 * To predict is fluid container full.
	 * @param stack
	 * @return
	 */
	boolean isFull(ItemStack stack);
	
	boolean canDrain(ItemStack stack);
	
	boolean canFill(ItemStack stack, @Nullable FluidStack resource);
	
	default boolean isV1()
	{
		return this instanceof IItemFluidContainerV1;
	}
	
	default IItemFluidContainerV1 castV1()
	{
		return (IItemFluidContainerV1) this;
	}
	
	default boolean isV2()
	{
		return this instanceof IItemFluidContainerV2;
	}
	
	default IItemFluidContainerV2 castV2()
	{
		return (IItemFluidContainerV2) this;
	}
	
	IFluidHandler createFluidHandlerWrapper(ItemStack stack);
}