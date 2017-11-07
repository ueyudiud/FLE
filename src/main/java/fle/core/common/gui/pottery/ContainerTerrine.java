/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.common.gui.pottery;

import fle.core.tile.pottery.TETerrine;
import nebula.common.gui.ContainerBase;
import nebula.common.gui.ContainerTileInventory;
import nebula.common.gui.FluidSlotN;
import nebula.common.gui.SlotBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author ueyudiud
 */
public class ContainerTerrine extends ContainerTileInventory<TETerrine>
{
	public ContainerTerrine(TETerrine tile, EntityPlayer player)
	{
		super(tile, player);
		int id = this.inventorySlots.size();
		addSlotToContainer(new SlotBase(tile, 0, 89, 28));
		addSlotToContainer(new SlotBase(tile, 1, 89, 46));
		
		addSlotToContainer(new FluidSlotN(tile.getTank(), 75, 32, 8, 30));
		ContainerBase.TL locationTerrine = new TL(id, id + 2);
		locationTerrine.appendTransferLocate(this.locationPlayer).addToList();
		this.locationBag.appendTransferLocate(locationTerrine).appendTransferLocate(this.locationHand);
		this.locationHand.appendTransferLocate(locationTerrine).appendTransferLocate(this.locationBag);
	}
}
