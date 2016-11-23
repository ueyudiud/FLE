package farcore.lib.inv;

import javax.annotation.Nullable;

import farcore.util.L;
import farcore.util.U;
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
			return U.ItemStacks.isItemAndTagEqual(stack, target);
		case MATCH_STACK_FULLY_INSERT :
			max = Math.min(limit, stack.getMaxStackSize());
			if(max > 1 && U.ItemStacks.isItemAndTagEqual(stack, target))
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
			return U.ItemStacks.isItemAndTagEqual(stack, target) && stack.stackSize >= target.stackSize;
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
		if(addStack(inventory, index, onlyFullyInsert, stack, false, false) != 0)
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

	public static int addStack(IBasicInventory inventory, int index, boolean onlyFullyInsert, @Nullable ItemStack stack, boolean process, boolean affectOnSourceStack)
	{
		if(stack == null) return 0;
		if(!process)
		{
			if(onlyFullyInsert)
				return matchStack(MATCH_STACK_FULLY_INSERT, inventory, index, stack) ? stack.stackSize : 0;
			else
				return !inventory.hasStackInSlot(index) ? Math.min(stack.stackSize, inventory.getInventoryStackLimit()) :
					U.ItemStacks.isItemAndTagEqual(inventory.getStackInSlot(index), stack) ? Math.min(stack.stackSize, Math.min(inventory.getInventoryStackLimit(), getAllowedInsertSize(inventory.getStackInSlot(index)))) : 0;
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
			else if(U.ItemStacks.isItemAndTagEqual(inventory.getStackInSlot(index), stack))
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
}