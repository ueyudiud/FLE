package fle.core.gui;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import fle.FLE;
import fle.api.gui.SlotOutput;
import fle.api.net.INetEventListener;
import fle.core.gui.base.ContainerCraftable;
import fle.core.net.FlePackets.CoderTileUpdate;
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
		if(!tile.getWorldObj().isRemote)
			FLE.fle.getNetworkHandler().sendTo(new CoderTileUpdate(tile.getWorldObj(), tile, (byte) 1, tile.getRecipeInput()));
	}
	
	@Override
	public boolean transferStackInSlot(Slot slot, ItemStack baseItemStack,
			ItemStack itemstack, int locate) 
	{
		return super.transferStackInSlot(slot, baseItemStack, itemstack, locate);
	}

	@Override
	public void onReseave(byte type, Object contain)
	{
		if(type == (byte) 1)
		{
			if((Short) contain < 9)
			{
				((TileEntityPolish) inv).craftedOnce(player.player, (Short) contain);
			}
			else if((Short) contain == 9)
			{
				((TileEntityPolish) inv).clearMap();
			}
		}
	}

	@Override
	public List getNetWorkField()
	{
		return null;
	}
}