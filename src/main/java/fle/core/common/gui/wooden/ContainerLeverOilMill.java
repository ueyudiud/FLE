/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.common.gui.wooden;

import fle.api.recipes.instance.RecipeMaps;
import fle.core.tile.wooden.TELeverOilMill;
import nebula.common.gui.ContainerTileInventory;
import nebula.common.gui.FluidSlot;
import nebula.common.gui.IGuiActionListener;
import nebula.common.gui.SlotBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class ContainerLeverOilMill extends ContainerTileInventory<TELeverOilMill> implements IGuiActionListener
{
	public ContainerLeverOilMill(TELeverOilMill tile, EntityPlayer player)
	{
		super(tile, player);
		int id = this.inventorySlots.size();
		addSlotToContainer(new SlotBase(tile, 0, 61, 21));
		addSlotToContainer(new SlotBase(tile, 1, 98, 21));
		addSlotToContainer(new FluidSlot(tile.tank, 60, 56, 20, 8).setRenderHorizontal());
		TL
		input = new TL(id)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return RecipeMaps.LEVER_OIL_MILL.findRecipe(stack) != null;
			}
		}.addToList(),
		output = new TL(id + 1).addToList();
		this.locationHand.appendTransferLocate(input).appendTransferLocate(this.locationBag);
		this.locationBag.appendTransferLocate(input).appendTransferLocate(this.locationHand);
		input.appendTransferLocate(this.locationPlayer);
		output.appendTransferLocate(this.locationPlayer);
	}
	
	@Override
	public void onRecieveGUIAction(byte type, long value)
	{
		switch (type)
		{
		case 0 :
			this.tile.onRotateMill(this.opener);
			break;
		default:
			break;
		}
	}
}