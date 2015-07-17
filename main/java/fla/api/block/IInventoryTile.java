package fla.api.block;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public interface IInventoryTile<T extends TileEntity> extends IInventory
{
	/**
	 * 
	 */
	void updateEntity(T tile);
	
	/**
	 * 
	 */
	void readFromNBT(NBTTagCompound nbt);
	
	/**
	 * 
	 */
	void writeToNBT(NBTTagCompound nbt);
}
