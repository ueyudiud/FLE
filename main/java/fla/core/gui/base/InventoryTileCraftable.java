package fla.core.gui.base;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import fla.core.Fla;
import fla.core.tileentity.TileEntityInventory;

public abstract class InventoryTileCraftable<T extends TileEntityInventory> extends InventoryTileBase<T>
{
	protected String customName = "";
	private final String name;

	public InventoryTileCraftable(String name, int i) 
	{
		super(i);
		this.name = name;
		boolean sendChangeToContainer;
	}
	
	public void syncSlot(T tile) 
	{
		if(!tile.getWorldObj().isRemote)
		{
			for(int i = 0; i < getSizeInventory(); ++i)
			{
				Fla.fla.nwm.get().updateInventoryTileSlot(tile, i, getStackInSlot(i));
			}
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
