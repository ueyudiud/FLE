package fle.core.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import flapi.gui.ContainerCraftable;
import flapi.gui.SolidSlot;
import fle.core.te.TileEntitySifter;

public class ContainerShifter extends ContainerCraftable
{
	public ContainerShifter(InventoryPlayer aPlayer,
			TileEntitySifter tile)
	{
		super(aPlayer, tile, 0, 0);
		addSlotToContainer(new Slot(tile, 0, 66, 23));
		addSlotToContainer(new Slot(tile, 1, 92, 23));
		addSlotToContainer(new Slot(tile, 2, 48, 49));
		addSlotToContainer(new Slot(tile, 3, 84, 49));
		addSlotToContainer(new SolidSlot(tile, 0, 66, 23));
		addSlotToContainer(new SolidSlot(tile, 1, 66, 49));
		locateRecipeInput = new TransLocation("input", 36);
		locateRecipeOutput = new TransLocation("output", 37);
	}
}