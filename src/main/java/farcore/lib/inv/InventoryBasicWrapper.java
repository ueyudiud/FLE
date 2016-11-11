/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.inv;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * @author ueyudiud
 */
public class InventoryBasicWrapper implements IBasicInventory, IInventory
{
	private static final ITextComponent EMPTY = new TextComponentString("");
	
	public static IInventory wrap(IBasicInventory inventory)
	{
		return new InventoryBasicWrapper(inventory);
	}

	private final IBasicInventory inventory;

	InventoryBasicWrapper(IBasicInventory inventory)
	{
		this.inventory = inventory;
	}

	@Override
	public String getName() { return ""; }
	
	@Override
	public boolean hasCustomName() { return false; }
	
	@Override
	public ITextComponent getDisplayName() { return EMPTY; }
	
	@Override
	public void markDirty() { }
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) { return true; }
	
	@Override
	public void openInventory(EntityPlayer player) { }
	
	@Override
	public void closeInventory(EntityPlayer player) { }
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) { return inventory.isItemValidForSlot(index, stack); }
	
	@Override
	public int getField(int id) { return 0; }
	
	@Override
	public void setField(int id, int value) { }
	
	@Override
	public int getFieldCount() { return 0; }
	
	@Override
	public void clear() { }
	
	@Override
	public int getSizeInventory() { return inventory.getSizeInventory(); }
	
	@Override
	@Nullable
	public ItemStack getStackInSlot(int index) { return inventory.getStackInSlot(index); }
	
	@Override
	@Nullable
	public ItemStack decrStackSize(int index, int count) { return inventory.decrStackSize(index, count); }
	
	@Override
	@Nullable
	public ItemStack removeStackFromSlot(int index) { return inventory.removeStackFromSlot(index); }
	
	@Override
	public void setInventorySlotContents(int index, @Nullable ItemStack stack) { inventory.setInventorySlotContents(index, stack); }
	
	@Override
	public int getInventoryStackLimit() { return inventory.getInventoryStackLimit(); }
}