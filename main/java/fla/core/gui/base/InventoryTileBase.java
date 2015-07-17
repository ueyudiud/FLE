package fla.core.gui.base;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import fla.api.block.IInventoryTile;
import fla.api.util.FlaValue;

public abstract class InventoryTileBase<T extends TileEntity> implements IInventoryTile<T>
{
	protected final Random rand = new Random();
	protected final ItemStack[] stacks;
	
	public InventoryTileBase(int i)
	{
		stacks = new ItemStack[i];
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		NBTTagList list = nbt.getTagList("Slots", 10);
		int i;
		for(i = 0; i < stacks.length; ++i)
		{
			stacks[i] = null;
		}
		for(i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound nbt1 = list.getCompoundTagAt(i);
			int slotId = nbt1.getByte("SlotId");
			stacks[slotId] = ItemStack.loadItemStackFromNBT(nbt1);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < stacks.length; ++i)
		{
			if(stacks[i] == null) continue;
			NBTTagCompound nbt1 = stacks[i].writeToNBT(new NBTTagCompound());
			nbt1.setByte("SlotId", (byte) i);
			list.appendTag(nbt1);
		}
		nbt.setTag("Slots", list);
	}

	@Override
	public int getSizeInventory() 
	{
		return stacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) 
	{
		return stacks[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int size) 
	{
		if(stacks[i] == null) return null;
		ItemStack ret = stacks[i].copy();
		int a = ret.stackSize;
		stacks[i].stackSize -= size;
		if(stacks[i].stackSize <= 0) stacks[i] = null;
		ret.stackSize = Math.min(size, ret.stackSize);
		return ret;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) 
	{
		return decrStackSize(i, getInventoryStackLimit());
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack stack)
	{
		if(stack == null) stacks[i] = null;
		else stacks[i] = stack.copy();
	}

	public abstract String getInventoryName();

	public abstract boolean hasCustomInventoryName();

	public int getInventoryStackLimit() 
	{
		return FlaValue.MAX_STACK_SIZE;
	}

	public void markDirty() { }

	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	public void openInventory() { }

	public void closeInventory() { }

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) 
	{
		return true;
	}

}
