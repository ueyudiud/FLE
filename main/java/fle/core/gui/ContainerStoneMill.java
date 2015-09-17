package fle.core.gui;

import fle.api.gui.ContainerCraftable;
import fle.core.te.TileEntityStoneMill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerStoneMill extends ContainerCraftable
{
	public ContainerStoneMill(InventoryPlayer player, TileEntityStoneMill inventory)
	{
		super(player, inventory, 0, 0);

		addSlotToContainer(new Slot(inventory, 0, 65, 20));
		addSlotToContainer(new Slot(inventory, 1, 55, 52));
		addSlotToContainer(new Slot(inventory, 2, 73, 52));
		addSlotToContainer(new Slot(inventory, 3, 91, 52));
		
		locateRecipeInput = new TransLocation("input", 36);
		locateRecipeOutput = new TransLocation("output", 37, 40);
		
		onCraftMatrixChanged(inventory);
	}
}