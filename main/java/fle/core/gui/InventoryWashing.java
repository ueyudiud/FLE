package fle.core.gui;

import net.minecraft.item.ItemStack;
import fle.api.gui.InventoryCraftable;

public class InventoryWashing extends InventoryCraftable
{
	public InventoryWashing(ContainerWashing container)
	{
		super("inventory.washing", container, 10);
	}

	@Override
	protected boolean isItemValidForAbstractSlot(int i, ItemStack itemstack) 
	{
		return false;
	}

	@Override
	protected boolean isInputSlot(int i) 
	{
		return i == 0;
	}

	@Override
	protected boolean isOutputSlot(int i) 
	{
		return i != 0;
	}
}