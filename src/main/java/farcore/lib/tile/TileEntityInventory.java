package farcore.lib.tile;

import farcore.inventory.Inventory;
import fle.api.tile.ITileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityInventory extends TileEntitySyncable implements ITileInventory
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
	
	@Override
	public Inventory getInventory()
	{
		return inventory;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return inventory.isUseableByPlayer(player);
	}

	@Override
	public boolean isItemValidForSlot(int id, ItemStack stack)
	{
		return inventory.isItemValidForSlot(id, stack);
	}
}