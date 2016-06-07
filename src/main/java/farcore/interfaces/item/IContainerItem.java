package farcore.interfaces.item;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IContainerItem
{
    /**
     * Returns the number of slots in the inventory.
     */
    int getSizeInventory(ItemStack container);
    
    /**
     * Returns the stack in slot i
     */
    ItemStack getStackInSlot(ItemStack container, int i);

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    ItemStack decrStackSize(ItemStack container, int i, int size);

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    void setInventorySlotContents(ItemStack container, int i, ItemStack stack);
    
    /**
     * Returns the maximum stack size for a inventory slot.
     */
    int getInventoryStackLimit(ItemStack container);
}