/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.inventory;

import javax.annotation.Nullable;

import nebula.common.stack.AbstractStack;
import nebula.common.tile.IItemHandlerIO;
import nebula.common.util.Direction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.IItemHandler;

/**
 * @author ueyudiud
 */
public final class InventoryWrapFactory
{
	private static final ITextComponent EMPTY = new TextComponentString("");
	
	public static interface I1 extends IBasicInventory, IInventory, IItemHandler
	{
		@Override
		default boolean isItemValidForSlot(int index, ItemStack stack)
		{
			return IBasicInventory.super.isItemValidForSlot(index, stack);
		}
		
		@Override
		default int getInventoryStackLimit()
		{
			return IBasicInventory.super.getInventoryStackLimit();
		}
		
		@Override
		default ItemStack decrStackSize(int index, int count)
		{
			return IBasicInventory.super.decrStackSize(index, count);
		}
	}
	
	public static interface I2 extends I1, IItemHandlerIO { }
	
	static class InventoryBasicWrapper implements I1
	{
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
		public int getSlots() { return this.inventory.getSizeInventory(); }
		public ItemStack insertItem(int slot, @Nullable ItemStack stack, boolean simulate)
		{
			if (!this.inventory.isItemValidForSlot(slot, stack) || stack == null) return stack;
			stack = stack.copy();
			int size = this.inventory.insertStack(slot, stack, simulate);
			stack.stackSize -= size;
			return stack;
		}
		public ItemStack extractItem(int slot, int amount, boolean simulate) { return this.inventory.decrStackSize(slot, amount, simulate); }
	}
	
	static class InventorySidedWrapper<I extends IBasicInventory & IItemHandlerIO> extends InventoryBasicWrapper implements I2
	{
		I inventory;
		
		InventorySidedWrapper(String name, I inventory)
		{
			super(name, inventory);
			this.inventory = inventory;
		}
		
		public boolean canExtractItem(Direction to) { return this.inventory.canExtractItem(to); }
		public boolean canInsertItem(Direction from, ItemStack stack) { return this.inventory.canInsertItem(from, stack); }
		public ItemStack extractItem(int size, Direction to, boolean simulate) { return this.inventory.extractItem(size, to, simulate); }
		public ItemStack extractItem(AbstractStack suggested, Direction to, boolean simulate) { return this.inventory.extractItem(suggested, to, simulate); }
		public int insertItem(ItemStack stack, Direction from, boolean simulate) { return this.inventory.insertItem(stack, from, simulate); }
		public ActionResult<ItemStack> onPlayerTryUseIO(ItemStack current, EntityPlayer player, Direction side, float x,
				float y, float z, boolean isActiveHeld) { return this.inventory.onPlayerTryUseIO(current, player, side, x, y, z, isActiveHeld); }
	}
	
	public static I1 wrap(String name, IBasicInventory inventory)
	{
		return inventory instanceof IItemHandlerIO ? new InventorySidedWrapper(name, inventory) : new InventoryBasicWrapper(name, inventory);
	}
}