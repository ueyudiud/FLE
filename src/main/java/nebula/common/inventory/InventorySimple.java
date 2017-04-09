/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.inventory;

import java.util.Arrays;

import nebula.common.util.A;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class InventorySimple implements IBasicInventory
{
	protected ItemStack[] stacks;
	protected int limit;
	
	public InventorySimple(int size)
	{
		this(size, 64);
	}
	public InventorySimple(int size, int limit)
	{
		this.stacks = new ItemStack[size];
		this.limit = limit;
	}
	
	@Override
	public ItemStack[] toArray()
	{
		return A.transform(this.stacks, ItemStack.class, stack->ItemStack.copyItemStack(stack));
	}
	
	@Override
	public void fromArray(ItemStack[] stacks)
	{
		System.arraycopy(stacks, 0, this.stacks, 0, this.stacks.length);
	}
	
	@Override
	public int getSizeInventory()
	{
		return this.stacks.length;
	}
	
	@Override
	public ItemStack getStack(int index)
	{
		return this.stacks[index];
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return this.limit;
	}
	
	@Override
	public int insertStack(int index, ItemStack resource, boolean process)
	{
		return InventoryHelper.incrStack(this, index, false, resource, process, false);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count, boolean process)
	{
		return InventoryHelper.decrStack(this, index, false, count, process);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return InventoryHelper.removeStack(this.stacks, index);
	}
	
	@Override
	public void removeAllStacks()
	{
		Arrays.fill(this.stacks, null);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.stacks[index] = ItemStack.copyItemStack(stack);
	}
}