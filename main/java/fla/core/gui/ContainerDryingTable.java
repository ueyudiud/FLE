package fla.core.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import fla.core.gui.base.ContainerCraftable;
import fla.core.gui.base.SlotOutput;
import fla.core.tileentity.TileEntityDryingTable;

public class ContainerDryingTable extends ContainerCraftable
{
	public ContainerDryingTable(InventoryPlayer player, TileEntityDryingTable inventory) 
	{
		super(player, inventory, 0, 0);
		addSlotToContainer(new Slot(inventory, 0, 48, 35));
		addSlotToContainer(new SlotOutput(inventory, 1, 124, 35));
		locateRecipeInput = new TransLocation("recipe_input", 0);
		locateRecipeOutput = new TransLocation("recipe_output", 1);
	}
}