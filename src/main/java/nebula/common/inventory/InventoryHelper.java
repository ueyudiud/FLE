package nebula.common.inventory;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import nebula.base.ArrayListAddWithCheck;
import nebula.base.IntegerEntry;
import nebula.base.function.Appliable;
import nebula.common.fluid.container.FluidContainerHandler;
import nebula.common.fluid.container.IItemFluidContainer;
import nebula.common.fluid.container.IItemFluidContainerV1;
import nebula.common.fluid.container.IItemFluidContainerV2;
import nebula.common.tile.IFluidHandlerIO;
import nebula.common.util.A;
import nebula.common.util.Direction;
import nebula.common.util.ItemStacks;
import nebula.common.util.L;
import nebula.common.util.Players;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidStack;

public class InventoryHelper
{
	public static final byte
	MATCH_STACK_FULLY_INSERT = 0x0,
	MATCH_STACK_SAME = 0x1,
	MATCH_STACK_FULLY_INSERT_WITHOUTNBT = 0x2,
	MATCH_STACK_SAME_WITHOUTNBT = 0x3,
	MATCH_STACK_EMPTY = 0x4,
	MATCH_STACK_CONTAIN = 0x5,
	MATCH_STACK_CONTAIN_WITHOUTNBT = 0x6;
	
	public static final byte
	FD_FILL_SIMPLE = 0x1,
	FD_FILL_ONLYFULL = 0x2,
	FD_FILL_ANY = 0x3,
	FD_DRAIN = 0x8;
	
	public static ItemStack removeStack(ItemStack[] stacks, int index)
	{
		return ItemStackHelper.getAndRemove(stacks, index);
	}
	
	public static boolean matchStack(byte type, IBasicInventory inventory, int index, @Nullable ItemStack target)
	{
		if(target == null || !inventory.hasStackInSlot(index))
			return type != MATCH_STACK_CONTAIN && type != MATCH_STACK_CONTAIN_WITHOUTNBT;
		int max, size;
		int limit = inventory.getInventoryStackLimit();
		ItemStack stack = inventory.getStack(index);
		switch (type)
		{
		case MATCH_STACK_SAME :
			return ItemStacks.isItemAndTagEqual(stack, target);
		case MATCH_STACK_FULLY_INSERT :
			max = Math.min(limit, stack.getMaxStackSize());
			if(max > 1 && ItemStacks.isItemAndTagEqual(stack, target))
			{
				size = stack.stackSize + target.stackSize;
				return size <= max;
			}
			return false;
		case MATCH_STACK_SAME_WITHOUTNBT :
			return stack.isItemEqual(target);
		case MATCH_STACK_FULLY_INSERT_WITHOUTNBT :
			max = Math.min(limit, stack.getMaxStackSize());
			if(max > 1 && stack.isItemEqual(target))
			{
				size = stack.stackSize + target.stackSize;
				return size <= max;
			}
			return false;
		case MATCH_STACK_CONTAIN :
			return ItemStacks.isItemAndTagEqual(stack, target) && stack.stackSize >= target.stackSize;
		case MATCH_STACK_CONTAIN_WITHOUTNBT :
			return stack.isItemEqual(target) && stack.stackSize >= target.stackSize;
		case MATCH_STACK_EMPTY :
		default :
			return false;
		}
	}
	
	public static ItemStack addStackGetResult(IBasicInventory inventory, int index, boolean onlyFullyInsert, @Nullable ItemStack stack)
	{
		if(stack == null) return null;
		if(incrStack(inventory, index, onlyFullyInsert, stack, false, false) != 0)
		{
			int result;
			if(!inventory.hasStackInSlot(index))
			{
				result = Math.min(stack.stackSize, inventory.getInventoryStackLimit());
				inventory.setInventorySlotContents(index, stack.splitStack(result));
				return stack.stackSize <= 0 ? null : stack;
			}
			else
			{
				result = L.min(stack.stackSize, inventory.getInventoryStackLimit(), getAllowedInsertSize(inventory.getStack(index)));
				stack.stackSize -= result;
				inventory.getStack(index).stackSize += result;
				return stack.stackSize <= 0 ? null : stack;
			}
		}
		return stack;
	}
	
	public static ItemStack decrStack(IBasicInventory inventory, int index, boolean onlyFullyExtract, int maxCount, boolean process)
	{
		if (maxCount == 0) return null;
		ItemStack stack = inventory.getStack(index);
		if (stack != null)
		{
			if (!process)
			{
				if (onlyFullyExtract)
				{
					return stack.stackSize >= maxCount ? ItemStacks.sizeOf(stack, maxCount) : null;
				}
				else
				{
					return ItemStacks.copyNomoreThan(stack, maxCount);
				}
			}
			else
			{
				if (stack.stackSize > maxCount)
				{
					return stack.splitStack(maxCount);
				}
				else if (!onlyFullyExtract)
				{
					inventory.removeStackFromSlot(index);
					return stack;
				}
			}
		}
		return null;
	}
	
	public static int incrStack(IBasicInventory inventory, int index, boolean onlyFullyInsert, @Nullable ItemStack stack, boolean process, boolean affectOnSourceStack)
	{
		if (stack == null) return 0;
		if (!process)
		{
			if (onlyFullyInsert)
				return matchStack(MATCH_STACK_FULLY_INSERT, inventory, index, stack) ? stack.stackSize : 0;
			else
				return !inventory.hasStackInSlot(index) ? Math.min(stack.stackSize, inventory.getInventoryStackLimit()) :
					ItemStacks.isItemAndTagEqual(inventory.getStack(index), stack) ? Math.min(stack.stackSize, Math.min(inventory.getInventoryStackLimit(), getAllowedInsertSize(inventory.getStack(index)))) : 0;
		}
		else
		{
			int result;
			if(!inventory.hasStackInSlot(index))
			{
				result = Math.min(stack.stackSize, inventory.getInventoryStackLimit());
				if(affectOnSourceStack)
				{
					inventory.setInventorySlotContents(index, stack.splitStack(result));
				}
				else
				{
					ItemStack stack1 = stack.copy();
					stack1.stackSize = result;
					inventory.setInventorySlotContents(index, stack1);
				}
				return result;
			}
			else if(ItemStacks.isItemAndTagEqual(inventory.getStack(index), stack))
			{
				result = L.min(stack.stackSize, inventory.getInventoryStackLimit(), getAllowedInsertSize(inventory.getStack(index)));
				if(affectOnSourceStack)
				{
					stack.stackSize -= result;
				}
				inventory.getStack(index).stackSize += result;
				return result;
			}
			else return 0;
		}
	}
	
	public static boolean drainTankFromInventoryItem(IBasicInventory inventory, IFluidHandlerIO io, int in, int out, int maxFill)
	{
		if (inventory.hasStackInSlot(in))
		{
			ItemStack stack = inventory.getStack(in);
			IntegerEntry<ItemStack> entry;
			if (in == out)
			{
				if (stack.stackSize > 1)
				{
					stack = ItemStacks.sizeOf(stack, 1);
					entry = FluidContainerHandler.fillContainerFromIO(stack, maxFill, io, Direction.Q, true);
					if (entry != null && entry.getKey() == null)
					{
						stack = inventory.decrStackSize(in, 1);
						FluidContainerHandler.fillContainerFromIO(stack, maxFill, io, Direction.Q, false);
						return true;
					}
				}
				else
				{
					entry = FluidContainerHandler.fillContainerFromIO(stack, maxFill, io, Direction.Q, false);
					if (entry != null)
					{
						inventory.setInventorySlotContents(in, entry.getKey());
						return true;
					}
				}
			}
			else
			{
				ItemStack stack1 = ItemStacks.sizeOf(stack, 1);
				entry = FluidContainerHandler.fillContainerFromIO(stack1, maxFill, io, Direction.Q, true);
				if (entry != null)
				{
					if (inventory.incrStack(out, entry.getKey(), true) != 0)
					{
						stack1 = inventory.decrStackSize(in, 1);
						FluidContainerHandler.fillContainerFromIO(stack1, maxFill, io, Direction.Q, false);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean fillTankFromInventoryItem(IBasicInventory inventory, IFluidHandlerIO io, int in, int out, int maxDrain, boolean onlyFullyDrain)
	{
		if (inventory.hasStackInSlot(in))
		{
			ItemStack stack = inventory.getStack(in);
			Entry<ItemStack, FluidStack> entry;
			if (in == out)
			{
				if (stack.stackSize == 1)
				{
					entry = FluidContainerHandler.drainContainerToIO(stack, maxDrain, io, Direction.Q, onlyFullyDrain, false);
					if (entry != null)
					{
						inventory.setInventorySlotContents(in, entry.getKey());
						return true;
					}
				}
			}
			else
			{
				stack = ItemStacks.sizeOf(stack, 1);
				entry = FluidContainerHandler.drainContainerToIO(stack, maxDrain, io, Direction.Q, onlyFullyDrain, true);
				if (entry != null)
				{
					if (inventory.incrStack(out, entry.getKey(), true) > 0)
					{
						stack = inventory.decrStackSize(in, 1);
						FluidContainerHandler.drainContainerToIO(stack, maxDrain, io, Direction.Q, onlyFullyDrain, false);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean drainOrFillTank(IBasicInventory inventory, IFluidHandlerIO io, int in, int out, byte fdType)
	{
		return drainOrFillTank(inventory, io, in, out, Integer.MAX_VALUE, fdType);
	}
	
	public static boolean drainOrFillTank(IBasicInventory inventory, IFluidHandlerIO io, int in, int out, int maxIO, byte fdType)
	{
		if (inventory.hasStackInSlot(in))
		{
			if ((fdType & FD_DRAIN) != 0)
			{
				if (drainTankFromInventoryItem(inventory, io, in, out, maxIO))
				{
					return true;
				}
			}
			if ((fdType & FD_FILL_ANY) != 0)
			{
				if (fillTankFromInventoryItem(inventory, io, in, out, maxIO, (fdType & FD_FILL_ONLYFULL) != 0))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean drainOrFillTank(IFluidHandlerIO io, EntityPlayer player, EnumHand hand, Direction direction, @Nullable ItemStack stack, byte fdType)
	{
		if (stack == null) return false;
		if (stack.getItem() instanceof IItemFluidContainer)
		{
			if (stack.getItem() instanceof IItemFluidContainerV1)
			{
				IItemFluidContainerV1 container = (IItemFluidContainerV1) stack.getItem();
				boolean flag = container.hasFluid(stack);
				if (!flag || container.canFill(stack, null))
				{
					if ((fdType & FD_DRAIN) != 0)
					{
						FluidStack stack2 = io.extractFluid(Integer.MAX_VALUE, direction, true);
						if (stack2 != null)
						{
							ItemStack item1 = stack;
							if (item1.stackSize > 1)
							{
								item1 = item1.splitStack(1);
								io.extractFluid(container.fill(item1, stack2, true), direction, false);
								Players.giveOrDrop(player, item1);
							}
							else
							{
								io.extractFluid(container.fill(item1, stack2, true), direction, false);
							}
							return true;
						}
					}
				}
				if (flag)
				{
					if ((fdType & FD_FILL_ANY) != 0)
					{
						FluidStack stack2 = container.drain(stack, Integer.MAX_VALUE, false);
						if (io.canInsertFluid(direction, stack2))
						{
							ItemStack item1 = stack;
							if (item1.stackSize > 1)
							{
								item1 = item1.splitStack(1);
								io.insertFluid(container.drain(item1, stack2, true), direction, false);
								Players.giveOrDrop(player, item1);
							}
							else
							{
								io.insertFluid(container.drain(item1, stack2, true), direction, false);
							}
							return true;
						}
					}
				}
			}
			else
			{
				IItemFluidContainerV2 container = (IItemFluidContainerV2) stack.getItem();
				if (!container.hasFluid(stack))
				{
					if ((fdType & FD_DRAIN) == 0) return false;
					int capacity = container.capacity(stack);
					FluidStack stack2 = io.extractFluid(capacity, direction, true);
					if (stack2.amount == capacity && container.canFill(stack, stack2))
					{
						if (stack.stackSize > 1)
						{
							Players.giveOrDrop(player, container.fill(stack, stack2, true));
						}
						else
						{
							player.setHeldItem(hand, container.fill(stack, stack2, true));
						}
						return true;
					}
				}
				else
				{
					if ((fdType & FD_FILL_ANY) == 0) return false;
					FluidStack stack2 = container.getContain(stack);
					if (io.canInsertFluid(direction, stack2))
					{
						int amount = io.insertFluid(stack2, direction, true);
						if ((fdType & FD_FILL_ONLYFULL) != 0 ? amount == stack2.amount : amount > 0)
						{
							io.insertFluid(stack2, direction, false);
							if (stack.stackSize > 1)
							{
								Players.giveOrDrop(player, container.drain(stack));
							}
							else
							{
								player.setHeldItem(hand, container.drain(stack));
							}
							return true;
						}
					}
				}
				return false;
			}
		}
		return false;
	}
	
	private static int getAllowedInsertSize(ItemStack stack)
	{
		return stack.getMaxStackSize() - stack.stackSize;
	}
	
	/**
	 * Insert all stack into inventory, and if successes, will apply and return the result.
	 * @param inventory
	 * @param from The start insert index of inventory.
	 * @param to The end insert index of inventory (Not include 'to' id self).
	 * @param stacks
	 * @param appliable The result applier.
	 * @return The result apply by appliable.
	 */
	public static <T> T insertAllStacks(IBasicInventory inventory, int from, int to, ItemStack[] stacks, @Nullable Appliable<T> appliable)
	{
		if (insertAllStacks(inventory, from, to, stacks, false))
		{
			insertAllStacks(inventory, from, to, stacks, true);
			return appliable == null ? null : appliable.apply();
		}
		return null;
	}
	
	private static boolean insertAllStacks(IBasicInventory inventory, int from, int to, ItemStack[] stacks, boolean process)
	{
		if (stacks == null || stacks.length == 0) return false;
		ItemStack[] array = inventory.toArray();
		int limit = inventory.getInventoryStackLimit();
		List<ItemStack> list = ArrayListAddWithCheck.requireNonnull();
		A.executeAll(stacks, stack->list.add(ItemStack.copyItemStack(stack)));
		for (int i = from; i < to; ++i)
		{
			if (array[i] != null)
			{
				ItemStack stack = array[i];
				int allowSize = Math.min(getAllowedInsertSize(stack), limit - stack.stackSize);
				if (allowSize <= 0) continue;
				Iterator<ItemStack> itr = list.iterator();
				while (itr.hasNext())
				{
					ItemStack stack1 = itr.next();
					if (ItemStacks.isItemAndTagEqual(stack, stack1))
					{
						if (allowSize >= stack1.stackSize)
						{
							stack.stackSize += stack1.stackSize;
							itr.remove();
						}
						else
						{
							stack.stackSize += allowSize;
							stack1.stackSize -= allowSize;
						}
						break;
					}
				}
			}
		}
		if (!list.isEmpty())
		{
			for (int i = from; i < to && !list.isEmpty(); ++i)
			{
				if (array[i] == null)
				{
					ItemStack stack = list.get(0);
					int size = Math.min(limit, stack.getMaxStackSize());
					if (stack.stackSize > size)
					{
						array[i] = stack.splitStack(size);
					}
					else
					{
						list.remove(0);
						array[i] = stack;
					}
				}
			}
		}
		if (process)
		{
			inventory.fromArray(array);
			return true;
		}
		else
		{
			return list.isEmpty();
		}
	}
	
	public static int insertStacks(IInventory inventory, int i, ItemStack stack, boolean process)
	{
		if (inventory.getStackInSlot(i) == null)
		{
			stack = ItemStacks.copyNomoreThan(stack, inventory.getInventoryStackLimit());
			if (process)
				inventory.setInventorySlotContents(i, stack);
			return stack.stackSize;
		}
		else if (ItemStacks.isItemAndTagEqual(stack, inventory.getStackInSlot(i)))
		{
			ItemStack stack2 = inventory.getStackInSlot(i);
			int size = Math.min(stack.stackSize, inventory.getInventoryStackLimit() - stack2.stackSize);
			if (process)
				stack2.stackSize += size;
			return size;
		}
		else return 0;
	}
}