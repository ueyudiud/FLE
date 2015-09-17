package fle.api.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerCraftable extends ContainerWithPlayerInventory
{
	protected TransLocation locateRecipeInput;
	protected TransLocation locateRecipeOutput;
	
	public ContainerCraftable(InventoryPlayer player, IInventory inventory,
			int playerInvUpos, int playerInvVpos) 
	{
		super(player, inventory, playerInvUpos, playerInvVpos);
	}

	public void onInputChanged(IInventory inv, int slotId)
	{
		
	}
	
	public void onOutputChanged(IInventory inv, int slotId)
	{
		
	}
	
	@Override
	public boolean transferStackInSlot(Slot slot, ItemStack baseItemStack, ItemStack itemstack, int locate)
	{
		if(this.locateRecipeInput.conrrect(locate) || this.locateRecipeOutput.conrrect(locate))
		{
			if(!this.locatePlayer.mergeItemStack(itemstack, true))
			{
				return true;
			}
		}
		else if(this.locatePlayerBag.conrrect(locate))
		{
			if(!this.locateRecipeInput.mergeItemStack(itemstack, false))
			{
				if(!this.locatePlayerHand.mergeItemStack(itemstack, false))
				{
					return true;
				}
				return true;
			}
		}
		else if(this.locatePlayerHand.conrrect(locate))
		{
			if(!this.locateRecipeInput.mergeItemStack(itemstack, false))
			{
				if(!this.locatePlayerBag.mergeItemStack(itemstack, true))
				{
					return true;
				}
				return true;
			}
		}
		return false;
	}
}