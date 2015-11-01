package fle.core.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import fle.api.gui.ContainerCraftable;
import fle.api.gui.SlotOutput;
import fle.api.gui.ContainerBase.TransLocation;
import fle.core.recipe.FLESoakRecipe;
import fle.core.te.tank.TileEntityMultiTankSoak;

public class ContainerMTSoak extends ContainerCraftable
{
	public ContainerMTSoak(InventoryPlayer player, TileEntityMultiTankSoak tile)
	{
		super(player, tile, 0, 0);
		addSlotToContainer(new Slot(tile, 0, 101, 23));
		addSlotToContainer(new Slot(tile, 1, 119, 23));
		addSlotToContainer(new Slot(tile, 2, 101, 44));
		addSlotToContainer(new Slot(tile, 3, 119, 44));
		locateRecipeInput = new TransLocation("input", 36, 40);
		locateRecipeOutput = new TransLocation("output", 36, 40);
		FLESoakRecipe.init();
	}

}