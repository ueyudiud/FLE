package farcore.lib.tile;

import farcore.inventory.Inventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityInventory extends TileEntitySyncable implements IInventory
{
	protected Inventory inventory;
	
	public TileEntityInventory(int size, String name, int limit)
	{
		this(new Inventory(size, name, limit));
	}
	protected TileEntityInventory(Inventory inventory)
	{
		this.inventory = inventory;
		this.inventory.setTile(this);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		inventory.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		inventory.writeToNBT(nbt);
	}
	
	public Inventory getInventory()
	{
		return inventory;
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int id)
	{
		return inventory.getStackInSlot(id);
	}

	@Override
	public ItemStack decrStackSize(int id, int size)
	{
		return inventory.decrStackSize(id, size);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int id)
	{
		return inventory.getStackInSlotOnClosing(id);
	}

	@Override
	public void setInventorySlotContents(int id, ItemStack stack)
	{
		inventory.setInventorySlotContents(id, stack);
	}

	@Override
	public String getInventoryName()
	{
		return inventory.getInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return inventory.hasCustomInventoryName();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return inventory.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return inventory.isUseableByPlayer(player);
	}

	@Override
	public void openInventory()
	{
		inventory.openInventory();
	}

	@Override
	public void closeInventory()
	{
		inventory.closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(int id, ItemStack stack)
	{
		return inventory.isItemValidForSlot(id, stack);
	}
}