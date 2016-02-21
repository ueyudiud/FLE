package flapi.te;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TEInventory extends TEMachine implements ISidedInventory
{
	protected InventoryTile inv;
	
	public TEInventory(InventoryTile inv)
	{
		this.inv = inv;
		inv.tile = this;
	}
	
	//================================Net work start================================
	public void syncSlot() 
	{
		inv.syncSlot();
	}
	
	public void syncSlot(int startID, int endID) 
	{
		inv.syncSlot(startID, endID);
	}
	//================================Net work end=================================
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		inv.readFromNBT(nbt.getCompoundTag("ItemInventory"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setTag("ItemInventory", inv.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public int getSizeInventory()
	{
		return inv.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inv.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int size)
	{
		return inv.decrStackSize(index, size);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		return inv.getStackInSlotOnClosing(index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		inv.setInventorySlotContents(index, stack);
	}

	@Override
	public String getInventoryName()
	{
		return inv.getInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return inv.hasCustomInventoryName();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return inv.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return inv.isUseableByPlayer(player);
	}

	@Override
	public void openInventory()
	{
		inv.openInventory();
	}

	@Override
	public void closeInventory()
	{
		inv.closeInventory();		
	}

	@Override
	public boolean isItemValidForSlot(int size, ItemStack stack)
	{
		return inv.isItemValidForSlot(size, stack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return inv.getAccessibleSlotsFromSide(side);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack,
			int side)
	{
		return inv.canInsertItem(index, stack, side);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack,
			int side)
	{
		return inv.canExtractItem(index, stack, side);
	}
}