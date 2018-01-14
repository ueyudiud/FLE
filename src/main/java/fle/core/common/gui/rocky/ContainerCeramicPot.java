/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.common.gui.rocky;

import fle.core.tile.rocky.TECeramicPot;
import nebula.common.gui.ContainerTileInventory;
import nebula.common.gui.FluidSlotN;
import nebula.common.gui.SlotBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author ueyudiud
 */
public class ContainerCeramicPot extends ContainerTileInventory<TECeramicPot>
{
	public ContainerCeramicPot(TECeramicPot tile, EntityPlayer player)
	{
		super(tile, player);
		int id = this.inventorySlots.size();
		addSlotToContainer(new SlotBase(tile, 0, 76, 32));
		addSlotToContainer(new SlotBase(tile, 1, 94, 32));
		addSlotToContainer(new SlotBase(tile, 2, 38, 19));
		addSlotToContainer(new SlotBase(tile, 3, 38, 44));
		addSlotToContainer(new SlotBase(tile, 4, 117, 19));
		addSlotToContainer(new SlotBase(tile, 5, 117, 44));
		addSlotToContainer(new FluidSlotN(tile.tank(), 66, 28, 8, 20));
		TL
		tl1 = new TL(id).appendTransferLocate(this.locationPlayer).addToList(),
		tl3 = new TLFluidContainerOnly(id + 2).appendTransferLocate(this.locationPlayer).addToList();
		new TL(id + 1).appendTransferLocate(this.locationPlayer).addToList();
		new TL(id + 5).appendTransferLocate(this.locationPlayer).addToList();
		this.locationBag.appendTransferLocate(tl3).appendTransferLocate(tl1).appendTransferLocate(this.locationHand);
		this.locationHand.appendTransferLocate(tl3).appendTransferLocate(tl1).appendTransferLocate(this.locationBag);
	}
}
