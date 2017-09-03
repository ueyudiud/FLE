/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The container class include a tile entity implemented
 * <code>Inventory</code>.<p>
 * @author ueyudiud
 *
 * @param <T> the tile entity type.
 */
public abstract class ContainerTileInventory<T extends TileEntity & IInventory> extends ContainerBase
{
	public T tile;
	protected final int[] lastTileFieldValues;
	
	/**
	 * Include opener slots and auto generated fields cache.
	 * @param tile
	 * @param player
	 */
	public ContainerTileInventory(T tile, EntityPlayer player)
	{
		super(player);
		addOpenerSlots();
		this.tile = tile;
		this.lastTileFieldValues = new int[tile.getFieldCount()];
	}
	
	public T getTileEntity()
	{
		return this.tile;
	}
	
	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tile);
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for(int i = 0; i < this.lastTileFieldValues.length; ++i)
		{
			int value1 = this.tile.getField(i);
			int value2 = this.lastTileFieldValues[i];
			if(value1 != value2)
			{
				for(IContainerListener listener : this.listeners)
				{
					listener.sendProgressBarUpdate(this, i, value1);
				}
				this.lastTileFieldValues[i] = value1;
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data)
	{
		this.tile.setField(id, data);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.tile.isUsableByPlayer(playerIn);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		if (!this.isClosed)
		{
			this.tile.closeInventory(playerIn);
		}
		super.onContainerClosed(playerIn);
	}
}