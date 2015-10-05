package fle.api.te;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import fle.api.inventory.IInventoryTile;
import fle.api.net.FlePackets.CoderSolidTankUpdate;
import fle.api.soild.ISolidHandler;
import fle.api.soild.ISolidTanks;

public abstract class TEInventory<T extends IInventoryTile> extends TEBase implements ISidedInventory
{
	protected T inv;
	
	protected TEInventory(T inv)
	{
		this.inv = inv;
	}
	
	public final void updateEntity()
	{
		super.updateEntity();
		updateInventory();
	}
	
	protected abstract void updateInventory();

	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		NBTTagCompound nbt1 = nbt.getCompoundTag("Inventory");
		inv.readFromNBT(nbt1);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		NBTTagCompound nbt1 = new NBTTagCompound();
		inv.writeToNBT(nbt1);
		nbt.setTag("Inventory", nbt1);
	}

	@Override
	public int getSizeInventory() 
	{
		return inv.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int i) 
	{
		return inv.getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int size) 
	{
		return inv.decrStackSize(i, size);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) 
	{
		return inv.getStackInSlotOnClosing(i);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		inv.setInventorySlotContents(i, itemstack);
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
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return inv.isItemValidForSlot(i, itemstack);
	}	

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return inv.getAccessibleSlotsFromSide(side);
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack aStack,
			int side)
	{
		return inv.canInsertItem(slotID, aStack, side);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack aStack,
			int side)
	{
		return inv.canExtractItem(slotID, aStack, side);
	}
	
	public void syncSolidTank()
	{
		if(this instanceof ISolidTanks && !worldObj.isRemote)
			sendToNearBy(new CoderSolidTankUpdate(getBlockPos()), 16.0F);
	}
}