package fle.core.inventory;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventoryWorkbenchTire0 extends InventoryCrafting
{
	public InventoryWorkbenchTire0(Container container) 
	{
		super(container, 3, 3);
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
	
	@Override
	public boolean isItemValidForSlot(int id, ItemStack stack)
	{
		return isSlotAccess(id) && super.isItemValidForSlot(id, stack);
	}
	
	public boolean isSlotAccess(int slotID)
	{
		int count = 0;
		for(int i = 0; i < getSizeInventory(); ++i)
		{
			if(getStackInSlot(i) != null) ++count;
		}
		return count < 6 ? true : getStackInSlot(slotID) != null;
	}
}