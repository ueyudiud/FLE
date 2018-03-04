/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.inventory;

import javax.annotation.Nullable;

import nebula.common.stack.AbstractStack;
import nebula.common.tile.IItemHandlerIO;
import nebula.common.util.Direction;
import nebula.common.util.ItemStacks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.IItemHandler;

/**
 * The inventory wrapper, uses to wrap inventory (item container).
 * <p>
 * <code>
 * IItemHandler handler = InventoryWrapFactory.wrap("yourinventoryname", inventory);
 * </code>
 * <p>
 * For you needn't take case about each side for a new handler.
 * 
 * @author ueyudiud
 */
public final class InventoryWrapFactory
{
	private static final ITextComponent EMPTY = new TextComponentString("");
	
	public static interface I1 extends IBasicInventory, IInventory, IItemHandler
	{
		@Override
		default boolean isValidForSlot(int index, ItemStack stack)
		{
			return IBasicInventory.super.isValidForSlot(index, stack);
		}
		
		@Override
		default int getStackLimit()
		{
			return IBasicInventory.super.getStackLimit();
		}
		
		@Override
		default ItemStack decrStackSize(int index, int count)
		{
			return IBasicInventory.super.decrStackSize(index, count);
		}
	}
	
	public static interface I2 extends I1, IItemHandlerIO
	{
	}
	
	static class InventoryBasicWrapper implements I1
	{
		private final IBasicInventory	inventory;
		private final String			name;
		
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
		
		public String getName()
		{
			return this.name;
		}
		
		public boolean hasCustomName()
		{
			return false;
		}
		
		public ITextComponent getDisplayName()
		{
			return this.name == null ? EMPTY : new TextComponentString(getName());
		}
		
		public void markDirty()
		{
		}
		
		public boolean isUsableByPlayer(EntityPlayer player)
		{
			return true;
		}
		
		public void openInventory(EntityPlayer player)
		{
		}
		
		public void closeInventory(EntityPlayer player)
		{
		}
		
		public boolean isValidForSlot(int index, ItemStack stack)
		{
			return this.inventory.isValidForSlot(index, stack);
		}
		
		public int getField(int id)
		{
			return 0;
		}
		
		public void setField(int id, int value)
		{
		}
		
		public int getFieldCount()
		{
			return 0;
		}
		
		public void clear()
		{
		}
		
		public int getSizeInventory()
		{
			return this.inventory.getSizeInventory();
		}
		
		public @Nullable ItemStack getStackInSlot(int index)
		{
			return this.inventory.getStack(index);
		}
		
		public @Nullable ItemStack getStack(int index)
		{
			return this.inventory.getStack(index);
		}
		
		public int incrItem(int index, ItemStack resource, boolean process)
		{
			return this.inventory.incrItem(index, resource, process);
		}
		
		public ItemStack decrStackSize(int index, int count)
		{
			return decrItem(index, count, true);
		}
		
		public @Nullable ItemStack decrItem(int index, int count, boolean process)
		{
			return this.inventory.decrItem(index, count, process);
		}
		
		public @Nullable ItemStack removeStackFromSlot(int index)
		{
			return this.inventory.removeStackFromSlot(index);
		}
		
		public void setSlotContents(int index, @Nullable ItemStack stack)
		{
			this.inventory.setSlotContents(index, stack);
		}
		
		public int getStackLimit()
		{
			return this.inventory.getStackLimit();
		}
		
		public int getSlots()
		{
			return this.inventory.getSizeInventory();
		}
		
		public ItemStack insertItem(int slot, @Nullable ItemStack stack, boolean simulate)
		{
			if (!this.inventory.isValidForSlot(slot, stack) || stack == null) return stack;
			stack = stack.copy();
			int size = this.inventory.incrItem(slot, stack, simulate);
			stack.stackSize -= size;
			return stack;
		}
		
		public ItemStack extractItem(int slot, int amount, boolean simulate)
		{
			return this.inventory.decrItem(slot, amount, simulate);
		}
		
		@Override
		public final boolean isItemValidForSlot(int index, ItemStack stack)
		{
			return isValidForSlot(index, stack);
		}
		
		@Override
		public final int getInventoryStackLimit()
		{
			return getStackLimit();
		}
		
		@Override
		public final void setInventorySlotContents(int index, ItemStack stack)
		{
			setSlotContents(index, stack);
		}
	}
	
	static class InventoryContainerWrapper extends InventoryBasicWrapper
	{
		private Container container;
		
		InventoryContainerWrapper(String name, Container container, IBasicInventory inventory)
		{
			super(name, inventory);
			this.container = container;
		}
		
		@Override
		public void setSlotContents(int index, ItemStack stack)
		{
			super.setSlotContents(index, stack);
			this.container.onCraftMatrixChanged(this);
		}
		
		@Override
		public ItemStack decrItem(int index, int count, boolean process)
		{
			ItemStack stack = super.decrItem(index, count, process);
			if (process && stack != null)
			{
				this.container.onCraftMatrixChanged(this);
			}
			return stack;
		}
		
		@Override
		public ItemStack removeStackFromSlot(int index)
		{
			ItemStack stack = super.removeStackFromSlot(index);
			this.container.onCraftMatrixChanged(this);
			return stack;
		}
		
		@Override
		public void setField(int id, int value)
		{
			this.container.updateProgressBar(id, value);
		}
	}
	
	static class InventorySidedWrapper<I extends IBasicInventory & IItemHandlerIO> extends InventoryBasicWrapper implements I2
	{
		I			inventory;
		Direction	direction;
		
		InventorySidedWrapper(String name, I inventory, EnumFacing facing)
		{
			super(name, inventory);
			this.inventory = inventory;
			this.direction = Direction.of(facing);
		}
		
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
		{
			int size = insertItem(stack, this.direction, simulate);
			return size == 0 ? stack :
				size == stack.stackSize ? null : ItemStacks.setSizeOf(stack, stack.stackSize - size);
		}
		
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate)
		{
			return extractItem(amount, this.direction, simulate);
		}
		
		public boolean canExtractItem(Direction to)
		{
			return this.inventory.canExtractItem(to);
		}
		
		public boolean canInsertItem(Direction from, ItemStack stack)
		{
			return this.inventory.canInsertItem(from, stack);
		}
		
		public ItemStack extractItem(int size, Direction to, boolean simulate)
		{
			return this.inventory.extractItem(size, to, simulate);
		}
		
		public ItemStack extractItem(AbstractStack suggested, Direction to, boolean simulate)
		{
			return this.inventory.extractItem(suggested, to, simulate);
		}
		
		public int insertItem(ItemStack stack, Direction from, boolean simulate)
		{
			return this.inventory.insertItem(stack, from, simulate);
		}
		
		public ActionResult<ItemStack> onPlayerTryUseIO(ItemStack current, EntityPlayer player, Direction side, float x, float y, float z, boolean isActiveHeld)
		{
			return this.inventory.onPlayerTryUseIO(current, player, side, x, y, z, isActiveHeld);
		}
	}
	
	public static I1 wrap(String name, IBasicInventory inventory)
	{
		return wrap(name, inventory, null);
	}
	
	public static I1 wrap(String name, IBasicInventory inventory, @Nullable EnumFacing facing)
	{
		return inventory instanceof IItemHandlerIO ? new InventorySidedWrapper(name, inventory, facing) : new InventoryBasicWrapper(name, inventory);
	}
	
	public static I1 wrap(String name, Container container, IBasicInventory inventory)
	{
		return new InventoryContainerWrapper(name, container, inventory);
	}
	
	public static <I extends IBasicInventory> I unwrap(IInventory inventory)
	{
		return inventory instanceof InventoryBasicWrapper ? (I) ((InventoryBasicWrapper) inventory).inventory : null;
	}
}
