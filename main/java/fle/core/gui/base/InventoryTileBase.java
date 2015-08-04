package fle.core.gui.base;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.FleValue;
import fle.api.gui.IInventoryTile;
import fle.core.net.FlePackets.CoderInventorUpdate;

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
	
	public void syncSlot(T tile) 
	{
		if(!tile.getWorldObj().isRemote)
		{
			for(int i = 0; i < getSizeInventory(); ++i)
			{
				FLE.fle.getNetworkHandler().sendTo(new CoderInventorUpdate(tile.getWorldObj(), tile.xCoord, (short) tile.yCoord, tile.zCoord));
			}
		}
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
		return FleValue.MAX_STACK_SIZE;
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

	@Override
	public int[] getAccessibleSlotsFromSide(int aSide)
	{
		return null;
	}

	public abstract boolean canInsertItem(int aSlotID, ItemStack aResource, ForgeDirection aDirection);
	
	public abstract boolean canExtractItem(int aSlotID, ItemStack aResource, ForgeDirection aDirection);
	
	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aStack, int aSide)
	{
		return aSlotID >= getSizeInventory() || aSlotID < 0 ? false : canInsertItem(aSlotID, aStack, ForgeDirection.VALID_DIRECTIONS[aSide]);
	}
	
	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aStack, int aSide)
	{
		return aSlotID >= getSizeInventory() || aSlotID < 0 ? false : canExtractItem(aSlotID, aStack, ForgeDirection.VALID_DIRECTIONS[aSide]);
	}
}