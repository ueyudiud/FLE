/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.common.gui.wooden.workbench;

import fle.api.recipes.instance.PolishRecipe;
import fle.core.tile.wooden.workbench.TEWoodenPolishTable;
import nebula.common.gui.ContainerTileInventory;
import nebula.common.gui.SlotBase;
import nebula.common.gui.SlotOutput;
import nebula.common.gui.SlotTool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class ContainerPolishTable extends ContainerTileInventory<TEWoodenPolishTable>
{
	public ContainerPolishTable(TEWoodenPolishTable tile, EntityPlayer player)
	{
		super(tile, player);
		int id = this.inventorySlots.size();
		addSlotToContainer(new SlotBase(tile, 0, 23, 35));
		addSlotToContainer(new SlotOutput(tile, 1, 132, 35));
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 3; ++j)
			{
				addSlotToContainer(new SlotTool(tile, i * 3 + j, 44 + j * 17, 18 + i * 17));
			}
		}
		TL input = new TL(id)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return PolishRecipe.isPolishable(stack);
			}
		}.addToList(), output = new TL(id + 1).addToList();
		this.locationHand.appendTransferLocate(input).appendTransferLocate(this.locationBag);
		this.locationBag.appendTransferLocate(input).appendTransferLocate(this.locationHand);
		input.appendTransferLocate(this.locationPlayer);
		output.appendTransferLocate(this.locationPlayer);
		tile.syncToPlayer(player);
	}
	
	@Override
	protected ItemStack onToolClick(ItemStack tool, IInventory inventoryBelong, int index)
	{
		this.tile.onPolish(this.opener, tool, index);
		detectAndSendChanges();
		return tool;
	}
	
	@Override
	public void onRecieveGUIAction(byte type, long value)
	{
		if (type == 0)
		{
			switch ((int) value)
			{
			case 0:
				this.tile.clearPolishMap();
				break;
			default:
				break;
			}
		}
		super.onRecieveGUIAction(type, value);
	}
}
