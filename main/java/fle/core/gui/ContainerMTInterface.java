package fle.core.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import fle.api.gui.ContainerCraftable;
import fle.api.gui.SlotOutput;
import fle.core.te.tank.TileEntityMultiTankInterface;

public class ContainerMTInterface extends ContainerCraftable
{
	public ContainerMTInterface(InventoryPlayer player, TileEntityMultiTankInterface tile)
	{
		super(player, tile, 0, 0);
		addSlotToContainer(new Slot(tile, 0, 93, 18));
		addSlotToContainer(new SlotOutput(tile, 1, 93, 52));
		locateRecipeInput = new TransLocation("input", 36);
		locateRecipeOutput = new TransLocation("output", 37);
	}
}