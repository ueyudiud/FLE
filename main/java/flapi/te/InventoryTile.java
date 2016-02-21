package flapi.te;

import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import farcore.block.TEBase;
import flapi.FleAPI;
import flapi.util.FleValue;

public abstract class InventoryTile implements ISidedInventory
{
	public TEBase tile;
	
	protected String customeName;
	public final ItemStack[] stacks;
	
	public InventoryTile(int i)
	{
		stacks = new ItemStack[i];
	}
	
	public void setCustomeName(String customeName)
	{
		this.customeName = customeName;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("Slots", 10);
		int i;
		Arrays.fill(stacks, null);
		for(i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound nbt1 = list.getCompoundTagAt(i);
			int slotId = nbt1.getByte("SlotId");
			stacks[slotId] = ItemStack.loadItemStackFromNBT(nbt1);
		}
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
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
		return nbt;
	}
	
	public void syncSlot() 
	{
		if(!tile.isClient())
		{
			tile.sendLarge(FleAPI.mod.getNetworkHandler().getPacketMaker().makeInventoryPacket(tile), 16.0F);
		}
	}
	
	public void syncSlot(int startID, int endID) 
	{
		if(!tile.isClient())
		{
			tile.sendLarge(FleAPI.mod.getNetworkHandler().getPacketMaker().makeInventoryPacket(tile, startID, endID), 16.0F);
		}
	}
	
	@Override
	public void markDirty()
	{
		tile.markDirty();
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

	public String getInventoryName()
	{
		return hasCustomInventoryName() ? customeName : getDefaultName();
	}
	
	protected abstract String getDefaultName();

	public boolean hasCustomInventoryName()
	{
		return customeName != null;
	}

	public int getInventoryStackLimit() 
	{
		return FleValue.MAX_STACK_SIZE;
	}

	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return player.getDistanceSq(tile.xCoord + 0.5F, tile.yCoord + 0.5F, tile.zCoord + 0.5F) <= 64.0F;
	}

	public void openInventory() { }

	public void closeInventory() { }

	public abstract boolean isItemValidForSlot(int i, ItemStack itemstack);

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return getAccessibleSlotsFromSide(ForgeDirection.VALID_DIRECTIONS[side]);
	}
	
	public abstract int[] getAccessibleSlotsFromSide(ForgeDirection dir);

	public boolean canInsertItem(int slotID, ItemStack resource, ForgeDirection direction)
	{
		return isSlotInputable(slotID);
	}
	
	public boolean canExtractItem(int slotID, ItemStack resource, ForgeDirection direction)
	{
		return isSlotOutputable(slotID);
	}

	protected abstract boolean isSlotInputable(int slotID);
	protected abstract boolean isSlotOutputable(int slotID);
	
	@Override
	public boolean canInsertItem(int aSlotID, ItemStack aStack, int side)
	{
		return aSlotID >= getSizeInventory() || aSlotID < 0 ? false : canInsertItem(aSlotID, aStack, ForgeDirection.VALID_DIRECTIONS[side]);
	}
	
	@Override
	public boolean canExtractItem(int aSlotID, ItemStack aStack, int side)
	{
		return aSlotID >= getSizeInventory() || aSlotID < 0 ? false : canExtractItem(aSlotID, aStack, ForgeDirection.VALID_DIRECTIONS[side]);
	}
}