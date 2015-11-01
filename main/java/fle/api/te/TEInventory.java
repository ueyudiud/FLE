package fle.api.te;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import fle.api.inventory.IInventoryTile;

public abstract class TEInventory<T extends IInventoryTile> extends TEBase implements ISidedInventory
{
	private T inv;
	
	protected TEInventory(T inv)
	{
		this.setTileInventory(inv);
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
		getTileInventory().readFromNBT(nbt1);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		NBTTagCompound nbt1 = new NBTTagCompound();
		getTileInventory().writeToNBT(nbt1);
		nbt.setTag("Inventory", nbt1);
	}

	@Override
	public int getSizeInventory() 
	{
		return getTileInventory().getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int i) 
	{
		return getTileInventory().getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int size) 
	{
		return getTileInventory().decrStackSize(i, size);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) 
	{
		return getTileInventory().getStackInSlotOnClosing(i);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		getTileInventory().setInventorySlotContents(i, itemstack);
	}

	@Override
	public String getInventoryName() 
	{
		return getTileInventory().getInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName() 
	{
		return getTileInventory().hasCustomInventoryName();
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return getTileInventory().getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return getTileInventory().isUseableByPlayer(player);
	}

	@Override
	public void openInventory()
	{
		getTileInventory().openInventory();
	}

	@Override
	public void closeInventory()
	{
		getTileInventory().closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return getTileInventory().isItemValidForSlot(i, itemstack);
	}	

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return getTileInventory().getAccessibleSlotsFromSide(side);
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack aStack,
			int side)
	{
		return getTileInventory().canInsertItem(slotID, aStack, side);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack aStack,
			int side)
	{
		return getTileInventory().canExtractItem(slotID, aStack, side);
	}

	public T getTileInventory()
	{
		return inv;
	}

	public void setTileInventory(T inv)
	{
		this.inv = inv;
	}
}