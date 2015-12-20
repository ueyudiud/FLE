package fle.core.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import flapi.gui.ContainerWithPlayerInventory;

public class Container3By3 extends ContainerWithPlayerInventory
{
	protected TransLocation locateInventory = new TransLocation("inventory", 36, 45);
	
	public Container3By3(InventoryPlayer player, IInventory inventory)
	{
		super(player, inventory, 0, 0);
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 3; ++j)
			{
				addSlotToContainer(new Slot(inventory, i + j * 3, 62 + 18 * i, 17 + 18 * j));
			}
	}

	@Override
	public boolean transferStackInSlot(Slot slot, ItemStack baseItemStack,
			ItemStack itemstack, int locate)
	{
		if(locateInventory.conrrect(locate))
		{
			if(!locatePlayer.mergeItemStack(itemstack, true))
			{
				return true;
			}
		}
		else if(locatePlayerBag.conrrect(locate))
		{
			if(!locateInventory.mergeItemStack(itemstack, false))
			{
				if(!locatePlayerHand.mergeItemStack(itemstack, false))
				{
					return true;
				}
				return true;
			}
		}
		else if(locatePlayerHand.conrrect(locate))
		{
			if(!locateInventory.mergeItemStack(itemstack, false))
			{
				if(!locatePlayerBag.mergeItemStack(itemstack, true))
				{
					return true;
				}
				return true;
			}
		}
		return false;
	}
}