package fla.core.gui;

import fla.core.gui.base.InventoryCraftable;
import net.minecraft.item.ItemStack;

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
