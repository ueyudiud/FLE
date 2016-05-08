package fle.api.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryCraftingDisplay implements IInventory
{
	ItemStack stack;
	
	@Override
	public int getSizeInventory()
	{
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int id)
	{
		return stack;
	}

	@Override
	public ItemStack decrStackSize(int id, int size)
	{
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int id)
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int id, ItemStack stack)
	{
		this.stack = ItemStack.copyItemStack(stack);
	}

	@Override
	public String getInventoryName()
	{
		return "Fle Crafting Display";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void markDirty()
	{
		
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	public void openInventory() {	}

	public void closeInventory() {	}

	@Override
	public boolean isItemValidForSlot(int id, ItemStack stack)
	{
		return false;
	}
}