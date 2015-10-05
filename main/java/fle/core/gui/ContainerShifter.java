package fle.core.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import fle.api.gui.ContainerCraftable;
import fle.core.te.TileEntitySifter;

public class ContainerShifter extends ContainerCraftable
{
	public ContainerShifter(InventoryPlayer aPlayer,
			TileEntitySifter aTile)
	{
		super(aPlayer, aTile, 0, 0);
		addSlotToContainer(new Slot(aTile, 0, 66, 23));
		addSlotToContainer(new Slot(aTile, 1, 92, 23));
		addSlotToContainer(new Slot(aTile, 2, 48, 49));
		addSlotToContainer(new Slot(aTile, 3, 84, 49));
		locateRecipeInput = new TransLocation("input", 36);
		locateRecipeOutput = new TransLocation("output", 37);
	}
}