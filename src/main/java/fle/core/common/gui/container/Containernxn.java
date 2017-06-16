/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.common.gui.container;

import nebula.common.gui.ContainerTileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

/**
 * @author ueyudiud
 */
public class Containernxn<T extends TileEntity & IInventory> extends ContainerTileInventory<T>
{
	public final int row;
	public final int column;
	
	public Containernxn(T tile, EntityPlayer player, EnumSlotsSize size)
	{
		this(tile, player, size.x, size.y, size.row, size.column, 18, 18);
	}
	public Containernxn(T tile, EntityPlayer player, int x, int y, int row, int column)
	{
		this(tile, player, x, y, row, column, 18, 18);
	}
	public Containernxn(T tile, EntityPlayer player,
			int x, int y, int row, int column, int spacingU, int spacingV)
	{
		super(tile, player);
		int size = this.inventorySlots.size();
		
		addStandardSlotMatrix(tile, x, y, this.row = row, this.column = column, 0, spacingU, spacingV);
		
		TL tl = new TL(size, size + row * column);
		tl.appendTransferLocate(this.locationHand).appendTransferLocate(this.locationBag).addToList();
		this.locationHand.appendTransferLocate(tl).appendTransferLocate(this.locationBag);
		this.locationBag.appendTransferLocate(tl).appendTransferLocate(this.locationHand);
		
		tile.openInventory(player);
	}
}