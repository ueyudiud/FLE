package fle.core.inventory;

import net.minecraft.item.ItemStack;
import fle.api.inventory.InventoryCraftable;
import fle.core.gui.ContainerCeramics;
import fle.core.init.Lang;

public class InventoryCeramics extends InventoryCraftable<ContainerCeramics>
{
	public InventoryCeramics(ContainerCeramics container)
	{
		super(Lang.inventory_cermaics, container, 1);
	}

	@Override
	protected boolean isItemValidForAbstractSlot(int i, ItemStack itemstack)
	{
		return true;
	}

	@Override
	protected boolean isInputSlot(int i)
	{
		return false;
	}

	@Override
	protected boolean isOutputSlot(int i)
	{
		return i == 0;
	}
}