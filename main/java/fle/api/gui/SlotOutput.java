package fle.api.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOutput extends Slot
{
	public SlotOutput(IInventory aInventory, int aSlotID, int aX, int aY) 
	{
		super(aInventory, aSlotID, aX, aY);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return false;
	}
}
