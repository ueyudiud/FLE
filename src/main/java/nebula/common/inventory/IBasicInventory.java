package nebula.common.inventory;

import javax.annotation.Nullable;

import nebula.common.base.Appliable;
import nebula.common.stack.AbstractStack;
import net.minecraft.item.ItemStack;

/**
 * The basic inventory, without redundant method.
 * @author ueyudiud
 *
 */
public interface IBasicInventory
{
	/**
	 * Return inventory slot as an array.
	 * For general uses, the array is suggested be a copy of source array.
	 * @return
	 */
	ItemStack[] toArray();
	
	/**
	 * Copy stacks data from array.
	 * @param stacks
	 */
	void fromArray(ItemStack[] stacks);
	
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
	
	@Nullable
	default ItemStack decrStackSize(int index, AbstractStack stack)
	{
		return decrStackSize(index, stack.size(getStackInSlot(index)));
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
	 * Remove all stacks from slots.
	 */
	default void removeAllStacks()
	{
		for (int i = 0; i < getSizeInventory(); ++i)
		{
			removeStackFromSlot(i);
		}
	}
	
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
	
	default <T> T insertAllStacks(ItemStack[] stacks, Appliable<T> consumer)
	{
		return insertAllStacks(stacks, 0, getSizeInventory(), consumer);
	}
	
	default <T> T insertAllStacks(ItemStack[] stacks, int from, int to, Appliable<T> consumer)
	{
		return InventoryHelper.insertAllStacks(this, from, to, stacks, consumer);
	}
}