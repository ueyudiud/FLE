/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.inventory;

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
	
	public static IInventory wrap(String name, IBasicInventory inventory)
	{
		return new InventoryBasicWrapper(name, inventory);
	}
	
	private final IBasicInventory inventory;
	private final String name;
	
	InventoryBasicWrapper(String name, IBasicInventory inventory)
	{
		this.inventory = inventory;
		this.name = name;
	}
	
	@Override
	public ItemStack[] toArray()
	{
		return this.inventory.toArray();
	}
	
	@Override
	public void fromArray(ItemStack[] stacks)
	{
		this.inventory.fromArray(stacks);
	}
	
	public String getName() { return this.name; }
	public boolean hasCustomName() { return false; }
	public ITextComponent getDisplayName() { return this.name == null ? EMPTY : new TextComponentString(getName()); }
	public void markDirty() { }
	public boolean isUsableByPlayer(EntityPlayer player) { return true; }
	public void openInventory(EntityPlayer player) { }
	public void closeInventory(EntityPlayer player) { }
	public boolean isItemValidForSlot(int index, ItemStack stack) { return this.inventory.isItemValidForSlot(index, stack); }
	public int getField(int id) { return 0; }
	public void setField(int id, int value) { }
	public int getFieldCount() { return 0; }
	public void clear() { }
	public int getSizeInventory() { return this.inventory.getSizeInventory(); }
	public @Nullable ItemStack getStackInSlot(int index) { return this.inventory.getStackInSlot(index); }
	public int insertStack(int index, ItemStack resource, boolean process) { return this.inventory.insertStack(index, resource, process); }
	public ItemStack decrStackSize(int index, int count) { return decrStackSize(index, count, true); }
	public @Nullable ItemStack decrStackSize(int index, int count, boolean process) { return this.inventory.decrStackSize(index, count, process); }
	public @Nullable ItemStack removeStackFromSlot(int index) { return this.inventory.removeStackFromSlot(index); }
	public void setInventorySlotContents(int index, @Nullable ItemStack stack) { this.inventory.setInventorySlotContents(index, stack); }
	public int getInventoryStackLimit() { return this.inventory.getInventoryStackLimit(); }
}