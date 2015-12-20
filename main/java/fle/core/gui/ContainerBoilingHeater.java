package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import flapi.gui.ContainerCraftable;
import flapi.gui.FluidSlot;
import flapi.gui.SlotTool;
import flapi.net.INetEventListener;
import fle.core.te.argil.TileEntityBoilingHeater;

public class ContainerBoilingHeater extends ContainerCraftable implements INetEventListener
{
	public ContainerBoilingHeater(InventoryPlayer player, final TileEntityBoilingHeater tile)
	{
		super(player, tile, 0, 0);
		addSlotToContainer(new Slot(tile, 0, 43, 19));
		addSlotToContainer(new Slot(tile, 1, 43, 37));
		addSlotToContainer(new Slot(tile, 2, 76, 19)
		{
			@Override
			public boolean canTakeStack(EntityPlayer aPlayer)
			{
				return super.canTakeStack(aPlayer) && !tile.isWorking();
			}
		});
		addSlotToContainer(new Slot(tile, 3, 94, 19));
		addSlotToContainer(new Slot(tile, 4, 89, 57));
		addSlotToContainer(new SlotTool(tile, 5, 71, 57));
		
		addSlotToContainer(new FluidSlot(tile, 0, 66, 15, 8, 20));
		locateRecipeInput = new TransLocation("input", 36);
		locateRecipeOutput = new TransLocation("output", 37);
	}

	@Override
	public void onReceive(byte type, Object contain)
	{
		if(type == 0)
		{
			if((Integer) contain == 0)
			{
				((TileEntityBoilingHeater) inv).drainTank(0, ((TileEntityBoilingHeater) inv).getTank(0).getCapacity(), true);
				((TileEntityBoilingHeater) inv).resetRecipe();
			}
		}
	}
}