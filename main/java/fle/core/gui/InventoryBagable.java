package fle.core.gui;

import net.minecraft.item.ItemStack;
import fle.core.gui.base.InventoryBase;

public class InventoryBagable extends InventoryBase<ContainerItemBagable>
{
	private int targetID;
	
	public InventoryBagable(ContainerItemBagable container, int targetID)
	{
		super(container, 1);
	}
	
	@Override
	public int getSizeInventory()
	{
		return container.getBag().getSize(container.player.getCurrentItem());
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack stack)
	{
		container.getBag().setItemContain(container.player.getCurrentItem(), i, stack);
	}
	
	@Override
	public ItemStack getStackInSlot(int i)
	{
		return container.getBag().getItemContain(container.player.getCurrentItem(), i);
	}
	
	@Override
	public ItemStack decrStackSize(int i, int size)
	{
		if(getStackInSlot(i) == null) return null;
		ItemStack ret = getStackInSlot(i).copy();
		int a = ret.stackSize;
		ItemStack in = getStackInSlot(i);
		in.stackSize -= size;
		if(in.stackSize <= 0) setInventorySlotContents(i, null);
		else setInventorySlotContents(i, in);
		ret.stackSize = Math.min(size, ret.stackSize);
		return ret;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return container.getBag().isItemValid(container.player.getCurrentItem(), itemstack);
	}

	@Override
	public String getInventoryName()
	{
		return null;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}
}