package fla.core.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import fla.api.network.IListenerContainer;
import fla.core.gui.base.ContainerWithPlayerInventory;
import fla.core.gui.base.SlotWithInputCheck;
import fla.core.tileentity.argil.TileEntityTerrine;

public class ContainerTerrine extends ContainerWithPlayerInventory implements IListenerContainer
{
	protected TransLocation locateContainer;
	
	public ContainerTerrine(InventoryPlayer player, TileEntityTerrine tile) 
	{
		super(player, tile, 0, 0);
		addSlotToContainer(new SlotWithInputCheck<TileEntityTerrine>(tile, 0, 89, 28)
		{
			@Override
			public boolean isItemValid(ItemStack item) 
			{
				return inventory.getFluidAmount() == 0 ? true :
					FluidContainerRegistry.isContainer(item);
			}
		});
		addSlotToContainer(new SlotWithInputCheck<TileEntityTerrine>(tile, 1, 89, 46)
		{
			@Override
			public boolean isItemValid(ItemStack item) 
			{
				return inventory.getFluidAmount() == 0;
			}
		});
		locateContainer = new TransLocation("container", 36);
	}

	@Override
	public boolean transferStackInSlot(Slot slot, ItemStack baseItemStack,
			ItemStack itemstack, int locate) 
	{
		if(this.locateContainer.conrrect(locate))
		{
			if(!this.locatePlayer.mergeItemStack(itemstack, true))
			{
				return true;
			}
		}
		else if(this.locatePlayerBag.conrrect(locate))
		{
			if(((TileEntityTerrine) inv).getFluidAmount() == 0 ? true :	FluidContainerRegistry.isContainer(itemstack))
			{
				if(!this.locateContainer.mergeItemStack(itemstack, false))
				{
					return true;
				}
			}
			if(!this.locatePlayerHand.mergeItemStack(itemstack, false))
			{
				return true;
			}
		}
		else if(this.locatePlayerHand.conrrect(locate))
		{
			if(((TileEntityTerrine) inv).getFluidAmount() == 0 ? true :	FluidContainerRegistry.isContainer(itemstack))
			{
				if(!this.locateContainer.mergeItemStack(itemstack, false))
				{
					return true;
				}
			}
			if(!this.locatePlayerBag.mergeItemStack(itemstack, false))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void onPacketData(int x, int y, int z, byte type, short contain) 
	{
		if(type == (byte) 1)
		{
			((TileEntityTerrine) this.inv).drain(((TileEntityTerrine) this.inv).getCapacity(), true);
		}
	}
}