package flapi.gui;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import flapi.util.FleValue;

public abstract class InventoryBase<T extends Container> implements IInventory
{
	protected final Random rand = new Random();
	protected T container;
	protected final ItemStack[] stacks;
	
	public InventoryBase(T container, int i)
	{
		stacks = new ItemStack[i];
		this.container = container;
	}

	@Override
	public int getSizeInventory() 
	{
		return stacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) 
	{
		return stacks[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int size) 
	{
		if(stacks[i] == null) return null;
		ItemStack ret = stacks[i].copy();
		stacks[i].stackSize -= size;
		if(stacks[i].stackSize <= 0) stacks[i] = null;
		ret.stackSize = Math.min(size, ret.stackSize);
		return ret;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) 
	{
		return decrStackSize(i, getInventoryStackLimit());
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack stack)
	{
		if(stack == null) stacks[i] = null;
		else stacks[i] = stack.copy();
	}

	public abstract String getInventoryName();

	public abstract boolean hasCustomInventoryName();

	public int getInventoryStackLimit() 
	{
		return FleValue.MAX_STACK_SIZE;
	}

	public void markDirty() { }

	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	public void openInventory() { }

	public void closeInventory() { }

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) 
	{
		return true;
	}
}