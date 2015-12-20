package fle.core.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import flapi.gui.ContainerCraftable;
import flapi.gui.FluidSlot;
import flapi.gui.SlotOutput;
import flapi.net.INetEventListener;
import fle.core.te.TileEntityOilMill;

public class ContainerLeverOilMill extends ContainerCraftable implements INetEventListener
{
	public ContainerLeverOilMill(InventoryPlayer player, TileEntityOilMill tile)
	{
		super(player, tile, 0, 0);

		addSlotToContainer(new Slot(tile, 0, 61, 21));
		addSlotToContainer(new SlotOutput(tile, 1, 98, 21));
		addSlotToContainer(new FluidSlot(tile, 0, 60, 56, 20, 8));
		
		locateRecipeInput = new TransLocation("input", 36);
		locateRecipeOutput = new TransLocation("output", 37);
	}

	@Override
	public void onReceive(byte type, Object contain)
	{
		if(type == 0)
		{
			if(((Integer) contain) == 0)
			{
				((TileEntityOilMill) inv).onWork();
			}
		}
	}
}