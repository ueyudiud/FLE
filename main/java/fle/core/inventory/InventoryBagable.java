package fle.core.inventory;

import net.minecraft.item.ItemStack;
import flapi.gui.InventoryBase;
import fle.core.gui.ContainerItemBagable;

public class InventoryBagable extends InventoryBase<ContainerItemBagable>
{
	private int targetID;
	
	public InventoryBagable(ContainerItemBagable container, int targetID)
	{
		super(container, 1);
		this.targetID = targetID;
	}
	
	@Override
	public int getSizeInventory()
	{
		return container.getBag().getSize(container.player.getStackInSlot(targetID));
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack stack)
	{
		container.getBag().setItemContain(container.player.getStackInSlot(targetID), i, stack);
	}
	
	@Override
	public ItemStack getStackInSlot(int i)
	{
		return container.getBag().getItemContain(container.player.getStackInSlot(targetID), i);
	}
	
	@Override
	public ItemStack decrStackSize(int i, int size)
	{
		if(getStackInSlot(i) == null) return null;
		ItemStack ret = getStackInSlot(i).copy();
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
		return container.getBag().isItemValid(container.player.getStackInSlot(targetID), itemstack);
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