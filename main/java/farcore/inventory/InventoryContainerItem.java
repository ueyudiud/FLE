package farcore.inventory;

import farcore.interfaces.item.IContainerItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryContainerItem implements IInventory
{
	private ItemStack stack;
	private IContainerItem item;
	
	public InventoryContainerItem(EntityPlayer player, int id)
	{
		stack = player.inventory.mainInventory[id];
		item = (IContainerItem) player.inventory.mainInventory[id].getItem();
	}
	
	public InventoryContainerItem(ItemStack stack, IContainerItem item)
	{
		this.stack = stack;
		this.item = item;
	}
	
	@Override
	public int getSizeInventory()
	{
		return item.getSizeInventory(stack);
	}

	@Override
	public ItemStack getStackInSlot(int id)
	{
		return item.getStackInSlot(stack, id);
	}

	@Override
	public ItemStack decrStackSize(int id, int size)
	{
		return item.decrStackSize(stack, id, size);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int id)
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int id, ItemStack stack)
	{
		item.setInventorySlotContents(stack, id, stack);		
	}

	@Override
	public String getInventoryName()
	{
		return stack.getDisplayName();
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return item.getInventoryStackLimit(stack);
	}

	public void markDirty() {	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	public void openInventory() {}

	public void closeInventory() {}

	public boolean isItemValidForSlot(int id, ItemStack stack)
	{
		return true;
	}
}