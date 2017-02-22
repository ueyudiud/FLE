package nebula.common.inventory;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import nebula.common.base.Appliable;
import nebula.common.base.ArrayListAddWithCheck;
import nebula.common.util.ItemStacks;
import nebula.common.util.L;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;

public class InventoryHelper
{
	public static final byte MATCH_STACK_FULLY_INSERT = 0x0;
	public static final byte MATCH_STACK_SAME = 0x1;
	public static final byte MATCH_STACK_FULLY_INSERT_WITHOUTNBT = 0x2;
	public static final byte MATCH_STACK_SAME_WITHOUTNBT = 0x3;
	public static final byte MATCH_STACK_EMPTY = 0x4;
	public static final byte MATCH_STACK_CONTAIN = 0x5;
	public static final byte MATCH_STACK_CONTAIN_WITHOUTNBT = 0x6;
	
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
		ItemStack stack = inventory.getStackInSlot(index);
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
				result = L.min(stack.stackSize, inventory.getInventoryStackLimit(), getAllowedInsertSize(inventory.getStackInSlot(index)));
				stack.stackSize -= result;
				inventory.getStackInSlot(index).stackSize += result;
				return stack.stackSize <= 0 ? null : stack;
			}
		}
		return stack;
	}
	
	public static ItemStack decrStack(IBasicInventory inventory, int index, boolean onlyFullyExtract, int maxCount, boolean process)
	{
		if (maxCount == 0) return null;
		ItemStack stack = inventory.getStackInSlot(index);
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
					ItemStacks.isItemAndTagEqual(inventory.getStackInSlot(index), stack) ? Math.min(stack.stackSize, Math.min(inventory.getInventoryStackLimit(), getAllowedInsertSize(inventory.getStackInSlot(index)))) : 0;
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
			else if(ItemStacks.isItemAndTagEqual(inventory.getStackInSlot(index), stack))
			{
				result = L.min(stack.stackSize, inventory.getInventoryStackLimit(), getAllowedInsertSize(inventory.getStackInSlot(index)));
				if(affectOnSourceStack)
				{
					stack.stackSize -= result;
				}
				inventory.getStackInSlot(index).stackSize += result;
				return result;
			}
			else return 0;
		}
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
		L.executeAll(stacks, stack->list.add(ItemStack.copyItemStack(stack)));
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
}