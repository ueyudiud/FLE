package fle.api.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import fle.api.FleAPI;
import fle.api.net.FlePackets.CoderInventoryUpdate;
import fle.api.te.TEInventory;

public abstract class InventoryTileCraftable<T extends TEInventory> extends InventoryTileAbstract<T>
{
	public InventoryTileCraftable(String name, int i) 
	{
		super(name, i);
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