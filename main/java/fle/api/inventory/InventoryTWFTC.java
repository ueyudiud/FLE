package fle.api.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.te.TEIT;

public abstract class InventoryTWFTC<T extends TEIT> extends InventoryWithFluidTank<T>
{
	public InventoryTWFTC(String name, int size, int capacity)
	{
		super(name, size, capacity);
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) 
	{
		return isInputSlot(i) ? true : (isOutputSlot(i) ? false : isItemValidForAbstractSlot(i, itemstack));
	}
	
	protected abstract boolean isItemValidForAbstractSlot(int i, ItemStack itemstack);
	
	protected abstract boolean isInputSlot(int i);
	
	protected abstract boolean isOutputSlot(int i);
}