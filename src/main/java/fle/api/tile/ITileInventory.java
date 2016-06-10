package fle.api.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface ITileInventory extends IInventory
{
	IInventory getInventory();
	
	@Override
	default int getSizeInventory()
	{
		return getInventory().getSizeInventory();
	}
	
	@Override
	default ItemStack getStackInSlot(int id)
	{
		return getInventory().getStackInSlot(id);
	}
	
	@Override
	default ItemStack decrStackSize(int id, int size)
	{
		return getInventory().decrStackSize(id, size);
	}
	
	@Override
	default ItemStack getStackInSlotOnClosing(int id)
	{
		return getInventory().getStackInSlotOnClosing(id);
	}
	
	@Override
	default void setInventorySlotContents(int id, ItemStack stack)
	{
		getInventory().setInventorySlotContents(id, stack);
	}
	
	@Override
	default String getInventoryName()
	{
		return getInventory().getInventoryName();
	}
	
	@Override
	default boolean hasCustomInventoryName()
	{
		return getInventory().hasCustomInventoryName();
	}
	
	@Override
	default int getInventoryStackLimit()
	{
		return getInventory().getInventoryStackLimit();
	}
	
	@Override
	default void openInventory()
	{
		getInventory().openInventory();
	}
	
	@Override
	default void closeInventory()
	{
		getInventory().closeInventory();
	}

	@Override
	default boolean isItemValidForSlot(int id, ItemStack stack)
	{
		return getInventory().isItemValidForSlot(id, stack);
	}
}