package fle.core.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import flapi.gui.ContainerCraftable;
import flapi.gui.SlotOutput;
import flapi.net.INetEventListener;
import fle.FLE;
import fle.core.net.FleGuiPacket;
import fle.core.te.TileEntityPolish;

public class ContainerPolish extends ContainerCraftable implements INetEventListener
{	
	public ContainerPolish(InventoryPlayer player, TileEntityPolish tile)
	{
		super(player, tile, 0, 0);
		locateRecipeInput = new TransLocation("inventory_input", 36);
		locateRecipeOutput = new TransLocation("inventory_output", 37);

		this.addSlotToContainer(new Slot(tile, 0, 20, 17));
		this.addSlotToContainer(new Slot(tile, 1, 104, 22));
		this.addSlotToContainer(new SlotOutput(tile, 2, 133, 35));
		if(!tile.isClient())
			FLE.fle.getNetworkHandler().sendTo(new FleGuiPacket((byte) 1, tile.getRecipeInput()));
		onCraftMatrixChanged(tile);
	}

	@Override
	public void onReceive(byte type, Object contain)
	{
		if(type == (byte) 1)
		{
			if((Integer) contain < 9)
			{
				((TileEntityPolish) inv).craftedOnce(player.player, (Integer) contain);
			}
			else if((Integer) contain == 9)
			{
				((TileEntityPolish) inv).clearMap();
			}
		}
	}
}