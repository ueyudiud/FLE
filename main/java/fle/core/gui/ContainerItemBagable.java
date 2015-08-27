package fle.core.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import fle.api.gui.ContainerBase;
import fle.api.gui.ContainerWithPlayerInventory;
import fle.api.gui.SlotHolographic;
import fle.api.item.IBagable;

public class ContainerItemBagable extends ContainerWithPlayerInventory
{
	int bagID;
	
	public ContainerItemBagable(InventoryPlayer player, int targetID)
	{
		super(player, null);
		inv = new InventoryBagable(this, targetID);
		bagID = targetID;
		int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
            	if(j + i * 9 + 9 == targetID) addSlotToContainer(new SlotHolographic(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18, false, false));
            	else addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
        	if(i == targetID) addSlotToContainer(new SlotHolographic(player, i, 8 + i * 18, 142, false, false));
        	else addSlotToContainer(new Slot(player, i, 8 + i * 18, 142));
        }
        addSlotToContainer(new Slot(inv, 0, 71, 26)
        {
        	@Override
        	public boolean isItemValid(ItemStack aStack)
        	{
        		return inv.isItemValidForSlot(0, aStack);
        	}
        });
        addSlotToContainer(new Slot(inv, 1, 89, 26)
        {
        	@Override
        	public boolean isItemValid(ItemStack aStack)
        	{
        		return inv.isItemValidForSlot(0, aStack);
        	}
        });
        addSlotToContainer(new Slot(inv, 2, 71, 44)
        {
        	@Override
        	public boolean isItemValid(ItemStack aStack)
        	{
        		return inv.isItemValidForSlot(0, aStack);
        	}
        });
        addSlotToContainer(new Slot(inv, 3, 89, 44)
        {
        	@Override
        	public boolean isItemValid(ItemStack aStack)
        	{
        		return inv.isItemValidForSlot(0, aStack);
        	}
        });
	}

	@Override
	public boolean transferStackInSlot(Slot slot, ItemStack baseItemStack,
			ItemStack itemstack, int locate)
	{
		return false;
	}
	
	@Override
	public boolean canDragIntoSlot(Slot aSlot)
	{
		return aSlot.getSlotIndex() != bagID;
	}
	
	public IBagable getBag()
	{
		return (IBagable) player.getStackInSlot(bagID).getItem();
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return getBag() != null;
	}
}