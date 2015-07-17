package fla.core.gui.base;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOutput extends Slot
{
	public SlotOutput(IInventory inv, int id, int x, int y)
	{
		super(inv, id, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack item) 
	{
		return false;
	}
}
