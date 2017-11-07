/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.fluid.container;

import javax.annotation.Nullable;

import farcore.data.Capabilities;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

/**
 * @author ueyudiud
 */
public interface IItemFluidContainerV1 extends IItemFluidContainer
{
	default void removeFluidFromContainer(ItemStack stack)
	{
		setFluidInContainer(stack, null);
	}
	
	void setFluidInContainer(ItemStack stack, @Nullable FluidStack fluid);
	
	default int fill(ItemStack stack, FluidStack resource, boolean doFill)
	{
		return stack.getCapability(Capabilities.CAPABILITY_FLUID, null).fill(resource, doFill);
	}
	
	default FluidStack drain(ItemStack stack, int maxDrain, boolean doDrain)
	{
		return stack.getCapability(Capabilities.CAPABILITY_FLUID, null).drain(maxDrain, doDrain);
	}
	
	default FluidStack drain(ItemStack stack, FluidStack resource, boolean doDrain)
	{
		return stack.getCapability(Capabilities.CAPABILITY_FLUID, null).drain(resource, doDrain);
	}
	
	@Override
	default IFluidHandler createFluidHandlerWrapper(ItemStack stack)
	{
		return new IFluidHandler()
		{
			@Override
			public IFluidTankProperties[] getTankProperties()
			{
				return new IFluidTankProperties[0];// Unused
			}
			
			@Override
			public int fill(FluidStack resource, boolean doFill)
			{
				return IItemFluidContainerV1.this.fill(stack, resource, doFill);
			}
			
			@Override
			public FluidStack drain(int maxDrain, boolean doDrain)
			{
				return IItemFluidContainerV1.this.drain(stack, maxDrain, doDrain);
			}
			
			@Override
			public FluidStack drain(FluidStack resource, boolean doDrain)
			{
				return IItemFluidContainerV1.this.drain(stack, resource, doDrain);
			}
		};
	}
}
