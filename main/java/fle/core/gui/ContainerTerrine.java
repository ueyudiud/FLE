package fle.core.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;
import fle.api.gui.ContainerWithPlayerInventory;
import fle.api.material.MatterDictionary;
import fle.api.net.INetEventListener;
import fle.core.te.argil.TileEntityTerrine;

public class ContainerTerrine extends ContainerWithPlayerInventory implements INetEventListener
{
	protected TransLocation locateContainer;
	
	public ContainerTerrine(InventoryPlayer player, TileEntityTerrine tile) 
	{
		super(player, tile, 0, 0);
		addSlotToContainer(new Slot(tile, 0, 89, 28)
		{
			@Override
			public boolean isItemValid(ItemStack item) 
			{
				return ((TileEntityTerrine) inventory).mode == 1 ? false : 
					((TileEntityTerrine) inventory).getFluidAmount() == 0 ? true :
					FluidContainerRegistry.isContainer(item) || MatterDictionary.getMelting(item) != null || item.getItem() instanceof IFluidContainerItem;
			}
			@Override
			public ItemStack decrStackSize(int par1)
			{
				return super.decrStackSize(par1);
			}
		});
		addSlotToContainer(new Slot(tile, 1, 89, 46)
		{
			@Override
			public boolean isItemValid(ItemStack item) 
			{
				return ((TileEntityTerrine) inventory).mode == 1 ? false : 
					((TileEntityTerrine) inventory).getFluidAmount() == 0;
			}
			@Override
			public ItemStack decrStackSize(int par1)
			{
				if (((TileEntityTerrine) inventory).mode == 1)
				{
					return null;
			    }
				return super.decrStackSize(par1);
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
	public void onReseave(byte type, Object contain)
	{
		if(type == (byte) 1)
		{
			int id = (Integer) contain;
			if(id == 0)
			{
				((TileEntityTerrine) this.inv).drain(((TileEntityTerrine) this.inv).getCapacity(), true);
			}
			else if(id == 1)
			{
				((TileEntityTerrine) this.inv).setClose();
			}
		}
	}
}