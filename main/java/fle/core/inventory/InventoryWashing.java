package fle.core.inventory;

import net.minecraft.item.ItemStack;
import flapi.gui.InventoryCraftable;
import fle.core.gui.ContainerWashing;
import fle.core.init.Lang;

public class InventoryWashing extends InventoryCraftable
{
	public InventoryWashing(ContainerWashing container)
	{
		super(Lang.inventory_washing, container, 11);
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