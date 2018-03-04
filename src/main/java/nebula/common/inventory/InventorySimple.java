/*
 * copyright© 2016-2018 ueyudiud
 */

package nebula.common.inventory;

import java.util.Arrays;

import nebula.base.A;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author ueyudiud
 */
public class InventorySimple implements IBasicInventory
{
	protected ItemStack[]	stacks;
	protected int			limit;
	
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
	public void toNBT(NBTTagCompound compound, String key)
	{
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < this.stacks.length; ++i)
		{
			if (this.stacks[i] != null)
			{
				NBTTagCompound compound2 = this.stacks[i].writeToNBT(new NBTTagCompound());
				compound2.setByte("id", (byte) i);
				list.appendTag(compound2);
			}
		}
		compound.setTag(key, list);
	}
	
	@Override
	public void fromNBT(NBTTagCompound compound, String key)
	{
		removeAllStacks();
		NBTTagList list = compound.getTagList(key, NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound compound2 = list.getCompoundTagAt(i);
			this.stacks[compound2.getByte("id")] = ItemStack.loadItemStackFromNBT(compound2);
		}
	}
	
	@Override
	public ItemStack[] toArray()
	{
		return A.transform(this.stacks, ItemStack.class, ItemStack::copyItemStack);
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
	public int getStackLimit()
	{
		return this.limit;
	}
	
	@Override
	public int incrItem(int index, ItemStack resource, boolean process)
	{
		return InventoryHelper.incrStack(this, index, false, resource, process, false);
	}
	
	@Override
	public ItemStack decrItem(int index, int count, boolean process)
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
	public void setSlotContents(int index, ItemStack stack)
	{
		this.stacks[index] = ItemStack.copyItemStack(stack);
	}
}
