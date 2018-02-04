/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.fluid.container;

import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

import java.util.Map.Entry;

import javax.annotation.Nullable;

import nebula.base.Ety;
import nebula.base.IntegerEntry;
import nebula.common.inventory.IBasicInventory;
import nebula.common.tile.IFluidHandlerIO;
import nebula.common.util.Direction;
import nebula.common.util.FluidStacks;
import nebula.common.util.ItemStacks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * The fluid container function helper.
 * 
 * @author ueyudiud
 * @see net.minecraftforge.fluids.capability.IFluidHandler IFluidHandler
 * @see nebula.common.fluid.container.IItemFluidContainer IItemFluidContainer
 */
public class FluidContainerHandler
{
	/**
	 * Get stack contained fluid, or <code>null</code> if stack is not a fluid container.
	 * @param stack the stack.
	 * @return the fluid in container.
	 */
	public static FluidStack getContain(ItemStack stack)
	{
		if (stack.getItem() instanceof IItemFluidContainer)
		{
			IItemFluidContainer raw = (IItemFluidContainer) stack.getItem();
			if (raw.hasFluid(stack))
			{
				if (raw.isV1())
					return raw.castV1().drain(stack, Integer.MAX_VALUE, false);
				else// if (raw.isV2())
					return raw.castV2().getContain(stack);
			}
		}
		return null;
	}
	
	/**
	 * Try drain fluid from stack to fluidIO.
	 * @param stack the stack, the container predicated.
	 * @param maxDrain the max drain amount.
	 * @param io the fluidIO.
	 * @param from the from facing.
	 * @param fullyDrain the fluid will only if when container fluid can fully drained into IO without
	 *                   remained fluid, if this option is <code>true</code>.
	 * @param simulate
	 * @return
	 */
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
		if (stack.hasCapability(FLUID_HANDLER_CAPABILITY, null))
		{
			IFluidHandler handler = stack.getCapability(FLUID_HANDLER_CAPABILITY, null);
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
	
	/**
	 * Drain fluid from fluid container in inventory.
	 * <p>
	 * The <tt>drain</tt> action will be handled with follows rules:
	 * <li>The stack should be non-null and the item should implements
	 * <code>IItemFluidContainer</code> or has
	 * {@link net.minecraftforge.fluids.capability.CapabilityFluidHandler#FLUID_HANDLER_CAPABILITY FLUID_HANDLER_CAPABILITY}
	 * capability, or action will not process.
	 * <li>If item implements
	 * {@link nebula.common.fluid.container.IItemFluidContainer IItemFluidContainer}, its capability
	 * will not be checked.
	 * <li>The action will be process when output slot can insert output stack
	 * or when item implements <code>IItemFluidContainer</code> and stack not
	 * changed after drained fluid, then the output will remain at input slot.
	 * </li>
	 * 
	 * @param inventory the inventory contain fluid container.
	 * @param required the fluid type and amount required.
	 * @param in the input slot id.
	 * @param out the output slot id.
	 * @return <tt>true</tt> when <tt>drain</tt> action is processed, and
	 *         <tt>false</tt> otherwise.
	 * @see net.minecraftforge.fluids.capability.IFluidHandler
	 * @see nebula.common.fluid.container.IItemFluidContainer
	 */
	public static boolean drainFromItem(IBasicInventory inventory, FluidStack required, int in, int out)
	{
		if (required == null) return true;
		if (inventory.hasStackInSlot(in))
		{
			ItemStack stack = ItemStacks.sizeOf(inventory.getStack(in), 1);
			if (stack.getItem() instanceof IItemFluidContainer)
			{
				IItemFluidContainer containerRaw = (IItemFluidContainer) stack.getItem();
				if (containerRaw.hasFluid(stack))
				{
					if (containerRaw.isV1())
					{
						IItemFluidContainerV1 container = containerRaw.castV1();
						container.drain(stack, required, true);
						if (!container.hasFluid(stack) && inventory.instItem(out, stack, true))
						{
							inventory.decrStackSize(in, 1);
						}
						return true;
					}
					else if (containerRaw.isV2())
					{
						IItemFluidContainerV2 container = containerRaw.castV2();
						if (container.getContain(stack).containsFluid(required))
						{
							ItemStack stack2 = container.drain(stack, false);
							if (stack2 != null && inventory.instItem(out, stack2, true))
							{
								return true;
							}
						}
					}
				}
			}
			else if (stack.hasCapability(FLUID_HANDLER_CAPABILITY, null))
			{
				IFluidHandler handler = stack.getCapability(FLUID_HANDLER_CAPABILITY, null);
				handler.drain(required, true);
				if (inventory.instItem(out, stack, true))
				{
					inventory.decrStackSize(in, 1);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Drain fluid from fluid container in inventory.
	 * <p>
	 * The <tt>drain</tt> action will be handled with follows rules:
	 * <li>The stack should be non-null and the item should implements
	 * <code>IItemFluidContainer</code> or has
	 * {@link net.minecraftforge.fluids.capability.CapabilityFluidHandler#FLUID_HANDLER_CAPABILITY FLUID_HANDLER_CAPABILITY}
	 * capability, or action will not process.
	 * <li>If item implements
	 * {@link nebula.common.fluid.container.IItemFluidContainer IItemFluidContainer}, its capability
	 * will not be checked.
	 * <li>The action will be process when output slot can insert output stack
	 * or when item implements <code>IItemFluidContainer</code> and stack not
	 * changed after drained fluid, then the output will remain at input slot.
	 * </li>
	 * 
	 * @param inventory the inventory contain fluid container.
	 * @param amount the fluid amount required.
	 * @param in the input slot id.
	 * @param out the output slot id.
	 * @return <tt>true</tt> when <tt>drain</tt> action is processed, and
	 *         <tt>false</tt> otherwise.
	 * @see net.minecraftforge.fluids.capability.IFluidHandler
	 * @see nebula.common.fluid.container.IItemFluidContainer
	 */
	public static boolean drainFromItem(IBasicInventory inventory, int amount, int in, int out)
	{
		if (amount == 0) return true;
		if (inventory.hasStackInSlot(in))
		{
			ItemStack stack = ItemStacks.sizeOf(inventory.getStack(in), 1);
			if (stack.getItem() instanceof IItemFluidContainer)
			{
				IItemFluidContainer containerRaw = (IItemFluidContainer) stack.getItem();
				if (containerRaw.hasFluid(stack))
				{
					if (containerRaw.isV1())
					{
						IItemFluidContainerV1 container = containerRaw.castV1();
						container.drain(stack, amount, true);
						if (!container.hasFluid(stack) && inventory.instItem(out, stack, true))
						{
							inventory.decrStackSize(in, 1);
						}
						return true;
					}
					else if (containerRaw.isV2())
					{
						IItemFluidContainerV2 container = containerRaw.castV2();
						if (container.capacity(stack) >= amount)
						{
							ItemStack stack2 = container.drain(stack, false);
							if (stack2 != null && inventory.instItem(out, stack2, true))
							{
								return true;
							}
						}
					}
				}
			}
			else if (stack.hasCapability(FLUID_HANDLER_CAPABILITY, null))
			{
				IFluidHandler handler = stack.getCapability(FLUID_HANDLER_CAPABILITY, null);
				handler.drain(amount, true);
				if (inventory.instItem(out, stack, true))
				{
					inventory.decrStackSize(in, 1);
					return true;
				}
			}
		}
		return false;
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
						return new IntegerEntry<>(ItemStacks.valid(stack), amount);
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
		if (stack.hasCapability(FLUID_HANDLER_CAPABILITY, null))
		{
			IFluidHandler handler = stack.getCapability(FLUID_HANDLER_CAPABILITY, null);
			int amount = handler.fill(resource, true);
			if (amount > 0)
			{
				return new IntegerEntry<>(stack, amount);
			}
		}
		return null;
	}
	
	public static @Nullable Entry<ItemStack, FluidStack> drainContainer(ItemStack stack, int amount)
	{
		if (stack.getItem() instanceof IItemFluidContainer)
		{
			IItemFluidContainer raw = (IItemFluidContainer) stack.getItem();
			if (raw.hasFluid(stack))
			{
				if (raw.isV1())
				{
					stack = stack.copy();
					FluidStack stack2 = raw.castV1().drain(stack, amount, true);
					return new Ety(stack, stack2);
				}
				else// if (raw.isV2())
				{
					FluidStack stack2 = raw.castV2().getContain(stack);
					stack = raw.castV2().drain(stack, true);
					stack2.amount = Math.min(stack2.amount, amount);
					return new Ety(ItemStacks.valid(stack), stack2);
				}
			}
		}
		if (stack.hasCapability(FLUID_HANDLER_CAPABILITY, null))
		{
			stack = stack.copy();
			IFluidHandler handler = stack.getCapability(FLUID_HANDLER_CAPABILITY, null);
			FluidStack stack2 = handler.drain(amount, true);
			return new Ety(ItemStacks.valid(stack), stack2);
		}
		return null;
	}
}
