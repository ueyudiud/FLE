package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import fle.api.gui.ContainerCraftable;
import fle.api.gui.SlotHolographic;
import fle.api.net.INetEventListener;
import fle.api.te.IFluidTanks;
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
		addSlotToContainer(new SlotHolographic(tile, 5, 71, 57, false, false));
		locateRecipeInput = new TransLocation("input", 36);
		locateRecipeOutput = new TransLocation("output", 37);
	}

	@Override
	public ItemStack slotClick(int aSlotID, int aMouseclick, int aShifthold, EntityPlayer aPlayer)
	{
		if(aSlotID > 0)
		{
			if(getSlot(aSlotID) == getSlotFromInventory(inv, 5))
			{
				ItemStack tStack = aPlayer.inventory.getItemStack();
			    if (tStack != null)
			    {
			    	((TileEntityBoilingHeater) inv).onToolClick(tStack, aPlayer);
			    	if (tStack.stackSize <= 0)
			    	{
			    		aPlayer.inventory.setItemStack(null);
			    	}
			    }
			    return null;
			}
		}
		return super.slotClick(aSlotID, aMouseclick, aShifthold, aPlayer);
	}

	@Override
	public void onReseave(byte type, Object contain)
	{
		if(type == 0)
		{
			if((Integer) contain == 0)
			{
				((IFluidTanks) inv).drainTank(0, ((IFluidTanks) inv).getTank(0).getCapacity(), true);
				((TileEntityBoilingHeater) inv).resetRecipe();
			}
		}
	}
}