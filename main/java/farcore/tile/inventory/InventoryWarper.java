package farcore.tile.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryWarper implements IInventory
{
	public static IInventory warp(IInventory inventory)
	{
		return new InventoryWarper(inventory);
	}
	
	private final ItemStack[] itemstacks;
	private InventoryWarper(ItemStack[] stacks) 
	{
		this.itemstacks = stacks;
	}
	private InventoryWarper(IInventory inventory) 
	{
		this.itemstacks = new ItemStack[inventory.getSizeInventory()];
		for(int i = 0; i < inventory.getSizeInventory(); ++i)
		{
			itemstacks[i] = inventory.getStackInSlot(i);
			if(itemstacks[i] != null) itemstacks[i] = itemstacks[i].copy();
		}
	}

	public int getSizeInventory() {return itemstacks.length;}

	public ItemStack getStackInSlot(int i) {return itemstacks[i];}

	public ItemStack decrStackSize(int i, int size) 
	{
		if(itemstacks[i] == null) return null;
		ItemStack ret = itemstacks[i].copy();
		itemstacks[i].stackSize -= size;
		if(itemstacks[i].stackSize < 1) itemstacks[i] = null;
		ret.stackSize = Math.min(size, ret.stackSize);
		return ret;
	}

	public ItemStack getStackInSlotOnClosing(int i) 
	{
		return decrStackSize(i, getInventoryStackLimit());
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) 
	{
		if(itemstack != null) itemstacks[i] = itemstack.copy();
	}

	public String getInventoryName() {return null;}
	public boolean hasCustomInventoryName() {return false;}
	public int getInventoryStackLimit() {return 64;}
	public void markDirty() {}
	public boolean isUseableByPlayer(EntityPlayer player) {return true;}
	public void openInventory() {}
	public void closeInventory() {}
	public boolean isItemValidForSlot(int i, ItemStack stack) {return true;}	
}