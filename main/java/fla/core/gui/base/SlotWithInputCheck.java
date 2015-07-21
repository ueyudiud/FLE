package fla.core.gui.base;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class SlotWithInputCheck<T extends IInventory> extends Slot
{
	public T inventory;
	
	public SlotWithInputCheck(T inv, int id, int x, int y)
	{
		super(inv, id, x, y);
		inventory = inv;
	}

	@Override
	public abstract boolean isItemValid(ItemStack item);
}
