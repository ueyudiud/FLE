package fle.core.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import flapi.gui.ContainerCraftable;
import flapi.gui.SolidSlot;
import fle.core.te.TileEntityStoneMill;

public class ContainerStoneMill extends ContainerCraftable
{
	public ContainerStoneMill(InventoryPlayer player, TileEntityStoneMill inventory)
	{
		super(player, inventory, 0, 0);

		addSlotToContainer(new Slot(inventory, 0, 65, 20));
		addSlotToContainer(new Slot(inventory, 1, 55, 52));
		//addSlotToContainer(new Slot(inventory, 2, 73, 52));
		addSlotToContainer(new Slot(inventory, 2, 91, 52));
		addSlotToContainer(new SolidSlot(inventory, 0, 73, 52));
		
		locateRecipeInput = new TransLocation("input", 36);
		locateRecipeOutput = new TransLocation("output", 37, 40);
	}
}