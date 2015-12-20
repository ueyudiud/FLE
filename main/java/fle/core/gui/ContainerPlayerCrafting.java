package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import flapi.gui.ContainerCraftable;
import flapi.gui.SlotOutput;
import flapi.gui.SlotTool;
import fle.core.inventory.InventoryPlayerCrafting;

public class ContainerPlayerCrafting extends ContainerCraftable
{
	public ContainerPlayerCrafting(InventoryPlayer player)
	{
		super(player, null);
		inv = new InventoryPlayerCrafting(this);
		addSlotToContainer(new Slot(inv, 0, 48, 17));
		addSlotToContainer(new SlotTool(inv, 1, 48, 35));
		addSlotToContainer(new Slot(inv, 2, 48, 53));
		addSlotToContainer(new SlotOutput(inv, 3, 112, 35));
		locateRecipeInput = new TransLocation("input", 36, 39);
		locateRecipeOutput = new TransLocation("output", 39);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		for(int i = 0; i < inv.getSizeInventory(); ++i)
			player.dropPlayerItemWithRandomChoice(inv.getStackInSlotOnClosing(i), false);
	}
}