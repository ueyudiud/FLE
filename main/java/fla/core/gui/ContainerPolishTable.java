package fla.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import fla.api.network.IListenerContainer;
import fla.core.gui.base.ContainerCraftable;
import fla.core.gui.base.SlotOutput;
import fla.core.tileentity.TileEntityPolishTable;

public class ContainerPolishTable extends ContainerCraftable implements IListenerContainer
{	
	public ContainerPolishTable(InventoryPlayer player, TileEntityPolishTable tile)
	{
		super(player, tile, 0, 0);
		tile.initSelectSlot();
		locateRecipeInput = new TransLocation("inventory_input", 36);
		locateRecipeOutput = new TransLocation("inventory_output", 37);
		
		this.addSlotToContainer(new Slot(tile, 1, 104, 22));
		this.addSlotToContainer(new SlotOutput(tile, 2, 133, 35));
	}

	@Override
	public void onPacketData(int x, int y, int z, byte type, short contain) 
	{
		if(type == (byte) 1)
		{
			if(contain == (short) 0)
			{
				((TileEntityPolishTable) inv).craftedOnce(player.player);
			}
			else if(contain == (short) 1)
			{
				((TileEntityPolishTable) inv).changeSelect();
			}
			else if(contain == (short) 2)
			{
				((TileEntityPolishTable) inv).clearMap();
			}
		}
	}
	
	@Override
	public boolean transferStackInSlot(Slot slot, ItemStack baseItemStack,
			ItemStack itemstack, int locate) 
	{
		return super.transferStackInSlot(slot, baseItemStack, itemstack, locate);
	}
}