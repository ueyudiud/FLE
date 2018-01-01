/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.fluid.container;

import javax.annotation.Nullable;

import nebula.common.util.FluidStacks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

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
	
	@Nullable
	ItemStack drain(ItemStack stack, boolean doDrain);
	
	@Override
	default IFluidHandler createFluidHandlerWrapper(ItemStack stack)
	{
		return new IFluidHandler()
		{
			@Override
			public IFluidTankProperties[] getTankProperties()
			{
				return new IFluidTankProperties[] { new FluidTankProperties(getContain(stack), capacity(stack), !isFull(stack), isFull(stack)) };
			}
			
			@Override
			public int fill(FluidStack resource, boolean doFill)
			{
				ItemStack stack1 = IItemFluidContainerV2.this.fill(stack, resource, doFill);
				if (stack1 != stack)
				{
					stack.deserializeNBT(stack1.serializeNBT());
				}
				return capacity(stack);
			}
			
			@Override
			public FluidStack drain(int maxDrain, boolean doDrain)
			{
				FluidStack stack0 = IItemFluidContainerV2.this.getContain(stack);
				ItemStack stack1 = IItemFluidContainerV2.this.drain(stack, doDrain);
				if (stack1 != stack)
				{
					stack.deserializeNBT(stack1.serializeNBT());
				}
				return stack0;
			}
			
			@Override
			public FluidStack drain(FluidStack resource, boolean doDrain)
			{
				FluidStack stack0 = IItemFluidContainerV2.this.getContain(stack);
				if (stack0.isFluidEqual(resource))
				{
					ItemStack stack1 = IItemFluidContainerV2.this.drain(stack, doDrain);
					if (stack1 != stack)
					{
						stack.deserializeNBT(stack1.serializeNBT());
					}
					return FluidStacks.sizeOf(stack0, resource.amount);
				}
				return null;
			}
		};
	}
}
