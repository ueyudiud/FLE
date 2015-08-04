package fle.api.gui;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public interface IInventoryTile<T extends TileEntity> extends ISidedInventory
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
