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
	 * Returns the stack in the given slot.
	 */
	@Nullable
	ItemStack getStackInSlot(int index);

	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	@Nullable
	ItemStack decrStackSize(int index, int count);

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
	int getInventoryStackLimit();
}