/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.common.gui.wooden;

import farcore.lib.container.ContainerTIF;
import farcore.lib.solid.SolidSlot;
import fle.api.recipes.instance.RecipeMaps;
import fle.core.tile.wooden.TEStoneMill;
import nebula.common.gui.FluidSlotN;
import nebula.common.gui.SlotBase;
import nebula.common.gui.SlotOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class ContainerStoneMill extends ContainerTIF<TEStoneMill>
{
	public ContainerStoneMill(TEStoneMill tile, EntityPlayer player)
	{
		super(tile, player);
		int id = this.inventorySlots.size();
		addSlotToContainer(new SlotBase(tile, 0, 65, 20));
		addSlotToContainer(new SlotBase(tile, 1, 55, 52));
		addSlotToContainer(new SlotOutput(tile, 2, 91, 52));
		addSlotToContainer(new FluidSlotN(tile.tank2, 117, 48, 8, 20).setRenderHorizontal());
		addSlotToContainer(new SolidSlot(tile.tank1, 73, 52, 16, 16));
		TL
		input = new TL(id)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return RecipeMaps.STONE_MILL.findRecipe(stack) != null;
			}
		}.addToList(),
		solid1 = new TL(id + 1).addToList(),
		solid2 = new TL(id + 2).addToList();
		this.locationHand.appendTransferLocate(input, solid1, this.locationBag);
		this.locationBag.appendTransferLocate(input, solid1, this.locationHand);
		input.appendTransferLocate(this.locationPlayer);
		solid1.appendTransferLocate(this.locationPlayer);
		solid2.appendTransferLocate(this.locationPlayer);
	}
}
