/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.inventory;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nebula.common.stack.AbstractStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * The basic inventory, without redundant method.
 * 
 * @author ueyudiud
 *
 */
public interface IBasicInventory
{
	default void toNBT(NBTTagCompound compound, String key)
	{
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < getSizeInventory(); ++i)
		{
			ItemStack stack = getStack(i);
			if (stack != null)
			{
				NBTTagCompound compound2 = stack.writeToNBT(new NBTTagCompound());
				compound2.setByte("id", (byte) i);
				list.appendTag(compound2);
			}
		}
		compound.setTag(key, list);
	}
	
	default void fromNBT(NBTTagCompound compound, String key)
	{
		removeAllStacks();
		NBTTagList list = compound.getTagList(key, NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound compound2 = list.getCompoundTagAt(i);
			setSlotContents(compound2.getByte("id"), ItemStack.loadItemStackFromNBT(compound2));
		}
	}
	
	/**
	 * Return inventory slot as an array. For general uses, the array is
	 * suggested be a copy of source array.
	 * 
	 * @return
	 */
	ItemStack[] toArray();
	
	/**
	 * Copy stacks data from array.
	 * 
	 * @param stacks the source array.
	 * @throws IllegalArgumentException if stacks length is less than inventory slot size.
	 */
	void fromArray(ItemStack[] stacks);
	
	/**
	 * Returns the number of slots in the inventory.
	 */
	int getSizeInventory();
	
	/**
	 * Returns whether the given slot has stack, default to check stack in slot.
	 * If you override this method, it will changed inventory helper behavior
	 * (Most result is you do not want to see).
	 */
	default boolean hasStackInSlot(int index)
	{
		return getStack(index) != null;
	}
	
	/**
	 * Returns the stack in the given slot.
	 */
	@Nullable
	ItemStack getStack(int index);
	
	/**
	 * Removes up to a specified number of items from an inventory slot and
	 * returns them in a new stack.
	 * 
	 * @see #decrItem(int, int, boolean)
	 */
	@Nullable
	default ItemStack decrStackSize(int index, int count)
	{
		return decrItem(index, count, true);
	}
	
	@Nullable
	default ItemStack decrStackSize(int index, @Nonnull AbstractStack stack)
	{
		return decrStackSize(index, stack.size(getStack(index)));
	}
	
	/**
	 * The <tt>insert</tt> action, try to add item stack fully to inventory.
	 * <p>
	 * The resource will be input into inventory only if :
	 * <li>The slot is empty or the item and tag from stack in slot is equal to
	 * resource.
	 * <li>The new stack size in slot is no greater than the size get from
	 * {@link #getStackLimit()}.</li>
	 * <p>
	 * If input stack is <tt>null</tt>, the method will return <tt>true</tt>. To
	 * be convince, using <br>
	 * <code>if (insertStack(idx, stack, true)) { other code... }</code><br>
	 * instead of<br>
	 * <code>if (insertStack(idx, stack, false)) { insertStack(idx, stack, true); other code... }</code><br>
	 * is allowed, for the checking codes is already contains in this method, if
	 * <tt>false</tt> is returned, the <tt>insert</tt> action will not process.
	 * 
	 * @param index the slot index.
	 * @param resource the input stack.
	 * @param process should inventory changed after action, if input is
	 *            <tt>false</tt> the inventory will only give a simulate result.
	 * @return return <tt>true</tt> when all can be insert into inventory.
	 */
	default boolean instItem(int index, @Nullable ItemStack resource, boolean process)
	{
		if (resource == null) return true;
		if (incrItem(index, resource, false) == resource.stackSize)
		{
			if (process) incrItem(index, resource, true);
			return true;
		}
		return false;
	}
	
	/**
	 * The <tt>increase</tt> action, try to add item stack to inventory.
	 * <p>
	 * The resource will be input into inventory only if :
	 * <li>The slot is empty or the item and tag from stack in slot is equal to
	 * resource.
	 * <li>The current stack size in slot is less than the size get from
	 * {@link #getStackLimit()}.</li>
	 * <p>
	 * If input stack is <tt>null</tt>, the method will return <tt>0</tt>.
	 * 
	 * @param index the slot index.
	 * @param resource the input stack.
	 * @param process should inventory changed after action, if input is
	 *            <tt>false</tt> the inventory will only give a simulate result.
	 * @return the insert stack size.
	 */
	int incrItem(int index, @Nullable ItemStack resource, boolean process);
	
	@Nullable
	ItemStack decrItem(int index, int count, boolean process);
	
	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Nullable
	ItemStack removeStackFromSlot(int index);
	
	/**
	 * Remove all stacks from slots.
	 */
	default void removeAllStacks()
	{
		for (int i = 0; i < getSizeInventory(); ++i)
		{
			removeStackFromSlot(i);
		}
	}
	
	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	void setSlotContents(int index, @Nullable ItemStack stack);
	
	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be
	 * 64, possibly will be extended.
	 */
	default int getStackLimit()
	{
		return 64;
	}
	
	/**
	 * Match is item can insert to slot.
	 * 
	 * @param index
	 * @param stack
	 * @return
	 */
	default boolean isValidForSlot(int index, ItemStack stack)
	{
		return true;
	}
	
	default <T> T insertAllStacks(ItemStack[] stacks, Supplier<T> appliable)
	{
		return insertAllStacks(stacks, 0, getSizeInventory(), appliable);
	}
	
	default <T> T insertAllStacks(ItemStack[] stacks, int from, int to, Supplier<T> appliable)
	{
		return InventoryHelper.insertAllStacks(this, from, to, stacks, appliable);
	}
}
