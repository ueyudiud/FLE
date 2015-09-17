package fle.core.gui;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import fle.api.gui.ContainerBase.TransLocation;
import fle.api.gui.ContainerCraftable;
import fle.api.gui.SlotOutput;
import fle.core.te.TileEntityDryingTable;

public class ContainerDryingTable extends ContainerCraftable
{
	public ContainerDryingTable(InventoryPlayer player, TileEntityDryingTable inventory) 
	{
		super(player, inventory, 0, 0);
		addSlotToContainer(new Slot(inventory, 0, 48, 35));
		addSlotToContainer(new SlotOutput(inventory, 1, 124, 35));
		locateRecipeInput = new TransLocation("recipe_input", 36);
		locateRecipeOutput = new TransLocation("recipe_output", 37);
		onCraftMatrixChanged(inventory);
	}
}