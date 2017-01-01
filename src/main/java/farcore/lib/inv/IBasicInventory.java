package farcore.lib.inv;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

/**
 * The basic inventory, without redundant method.
 * @author ueyudiud
 *
 */
public interface IBasicInventory
{
	/**
	 * Returns the number of slots in the inventory.
	 */
	int getSizeInventory();
	
	/**
	 * Returns whether the given slot has stack,
	 * default to check stack in slot.
	 * If you override this method, it will changed
	 * inventory helper behavior (Most result is you
	 * do not want to see).
	 */
	default boolean hasStackInSlot(int index)
	{
		return getStackInSlot(index) != null;
	}
	
	/**
	 * Returns the stack in the given slot.
	 */
	@Nullable
	ItemStack getStackInSlot(int index);
	
	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	@Nullable
	default ItemStack decrStackSize(int index, int count)
	{
		return decrStackSize(index, count, true);
	}
	
	int insertStack(int index, ItemStack resource, boolean process);
	
	@Nullable
	ItemStack decrStackSize(int index, int count, boolean process);
	
	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Nullable
	ItemStack removeStackFromSlot(int index);
	
	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	void setInventorySlotContents(int index, @Nullable ItemStack stack);
	
	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
	 */
	default int getInventoryStackLimit()
	{
		return 64;
	}
	
	/**
	 * Match is item can insert to slot.
	 * @param index
	 * @param stack
	 * @return
	 */
	default boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return true;
	}
}