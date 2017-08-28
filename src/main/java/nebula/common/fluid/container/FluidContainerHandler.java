/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.fluid.container;

import java.util.Map.Entry;

import javax.annotation.Nullable;

import farcore.data.Capabilities;
import nebula.base.Ety;
import nebula.base.IntegerEntry;
import nebula.common.tile.IFluidHandlerIO;
import nebula.common.util.Direction;
import nebula.common.util.FluidStacks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * @author ueyudiud
 */
public class FluidContainerHandler
{
	public static @Nullable Entry<ItemStack, FluidStack> drainContainerToIO(ItemStack stack, int maxDrain, IFluidHandlerIO io, Direction from, boolean fullyDrain, boolean simulate)
	{
		FluidStack stack1;
		if (stack.getItem() instanceof IItemFluidContainer)
		{
			IItemFluidContainer containerRaw = (IItemFluidContainer) stack.getItem();
			if (containerRaw.canDrain(stack) && containerRaw.hasFluid(stack))
			{
				if (containerRaw.isV1())
				{
					IItemFluidContainerV1 container = containerRaw.castV1();
					stack1 = container.drain(stack, maxDrain, false);
					if (io.canInsertFluid(from, stack1))
					{
						int amount = io.insertFluid(stack1, from, simulate);
						stack1 = container.drain(stack, amount, true);
						return new Ety<>(stack, stack1);
					}
				}
				else if (containerRaw.isV2())
				{
					IItemFluidContainerV2 container = containerRaw.castV2();
					int cap = container.capacity(stack);
					stack1 = container.getContain(stack);
					if (stack1 != null && io.canInsertFluid(from, stack1))
					{
						int amount = io.insertFluid(stack1, from, true);
						if (fullyDrain ? amount == cap : amount > 0)
						{
							if (!simulate)
							{
								io.insertFluid(stack1, from, false);
							}
							return new Ety<>(container.drain(stack, !simulate), stack1);
						}
					}
				}
			}
		}
		if (stack.hasCapability(Capabilities.CAPABILITY_FLUID, null))
		{
			IFluidHandler handler = stack.getCapability(Capabilities.CAPABILITY_FLUID, null);
			FluidStack stack2 = handler.drain(Integer.MAX_VALUE, true);
			if (stack2 != null && io.canInsertFluid(from, stack2))
			{
				int amount = io.insertFluid(stack2, from, true);
				if (fullyDrain ? stack2.amount == amount : amount > 0)
				{
					if (!simulate)
					{
						io.insertFluid(stack2, from, false);
					}
					return new Ety<>(stack, FluidStacks.sizeOf(stack2, amount));
				}
			}
		}
		return null;
	}
	
	public static @Nullable IntegerEntry<ItemStack> fillContainerFromIO(ItemStack stack, int maxFill, IFluidHandlerIO io, Direction to, boolean simulate)
	{
		if (!io.canExtractFluid(to)) return null;
		FluidStack stack1 = io.extractFluid(maxFill, to, true);
		IntegerEntry<ItemStack> entry = fillContainer(stack, stack1);
		if (!simulate && entry != null)
		{
			io.extractFluid(entry.getValue(), to, false);
		}
		return entry;
	}
	
	public static @Nullable IntegerEntry<ItemStack> fillContainer(ItemStack stack, FluidStack resource)
	{
		if (stack.getItem() instanceof IItemFluidContainer)
		{
			IItemFluidContainer containerRaw = (IItemFluidContainer) stack.getItem();
			if (containerRaw.canFill(stack, resource) && !containerRaw.isFull(stack))
			{
				if (containerRaw.isV1())
				{
					IItemFluidContainerV1 container = containerRaw.castV1();
					int amount = container.fill(stack, resource, true);
					if (amount > 0)
					{
						return new IntegerEntry<>(stack, amount);
					}
				}
				else if (containerRaw.isV2())
				{
					IItemFluidContainerV2 container = containerRaw.castV2();
					int cap = container.capacity(stack);
					if (resource.amount >= cap)
					{
						ItemStack stack2 = container.fill(stack, resource, true);
						if (stack2 != null)
						{
							return new IntegerEntry<>(stack2, cap);
						}
					}
				}
			}
		}
		if (stack.hasCapability(Capabilities.CAPABILITY_FLUID, null))
		{
			IFluidHandler handler = stack.getCapability(Capabilities.CAPABILITY_FLUID, null);
			int amount = handler.fill(resource, true);
			if (amount > 0)
			{
				return new IntegerEntry<>(stack, amount);
			}
		}
		return null;
	}
}