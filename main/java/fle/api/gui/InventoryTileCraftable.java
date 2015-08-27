package fle.api.gui;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import fle.api.FleAPI;
import fle.api.net.FlePackets.CoderInventoryUpdate;
import fle.api.te.TEInventory;

public abstract class InventoryTileCraftable<T extends TEInventory> extends InventoryTileBase<T>
{
	protected String customName = "";
	private final String name;

	public InventoryTileCraftable(String name, int i) 
	{
		super(i);
		this.name = name;
	}
	
	public void syncSlot(T tile) 
	{
		if(!tile.getWorldObj().isRemote)
		{
			FleAPI.mod.getNetworkHandler().sendTo(new CoderInventoryUpdate(tile.getWorldObj(), tile.xCoord, (short) tile.yCoord, tile.zCoord));
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
		
		customName = nbt.getString("CustomName");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		super.writeToNBT(nbt);
		
		nbt.setString("CustomName", customName);
	}

	@Override
	public String getInventoryName() 
	{
		return hasCustomInventoryName() ? customName : name;
	}

	@Override
	public boolean hasCustomInventoryName() 
	{
		return customName != "";
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) 
	{
		return isInputSlot(i) ? true : (isOutputSlot(i) ? false : isItemValidForAbstractSlot(i, itemstack));
	}
	
	protected abstract boolean isItemValidForAbstractSlot(int i, ItemStack itemstack);
	
	protected abstract boolean isInputSlot(int i);
	
	protected abstract boolean isOutputSlot(int i);
}