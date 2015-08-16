package fle.api.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerBase extends Container
{
	public InventoryPlayer player;
	public IInventory inv;
	protected Map<String, Integer> list;

	public ContainerBase(InventoryPlayer player, IInventory inventory) 
	{
		this.player = player;
		this.inv = inventory;
	}
	public ContainerBase(IInventory inventory) 
	{
		this.inv = inventory;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) 
	{
		return inv.isUseableByPlayer(player);
	}
	
	public void setupNetWorkFields()
	{
		list = new HashMap();
	}
	
	public void setField(String str, int index)
	{
		list.put(str, index);
	}
	
	public abstract List getNetWorkField();
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i) 
	{
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(i);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if(transferStackInSlot(slot, itemstack, itemstack1, i))
            {
                if (itemstack1.stackSize == 0)
                {
                    slot.putStack((ItemStack) null);
                }
                else
                {
                    slot.onSlotChanged();
                }
            	return null;
            }
            
            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack) null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }
        }
        
        return itemstack;
	}
	
	public abstract boolean transferStackInSlot(Slot slot, ItemStack baseItemStack, ItemStack itemstack, int locate);
	
	public class TransLocation
	{
		String name;
		int startId;
		int endId;

		public TransLocation(String name, int id)
		{
			this.name = name;
			this.startId = id;
			this.endId = id + 1;
		}
		public TransLocation(String name, int startId, int endId)
		{
			this.name = name;
			this.startId = startId;
			this.endId = endId;
		}
		
		public String getName()
		{
			return name;
		}
		
		public boolean conrrect(int i)
		{
			return i >= startId && i < endId;
		}
		
		public boolean mergeItemStack(ItemStack itemstack, boolean isFront)
		{
			return ContainerBase.this.mergeItemStack(itemstack, startId, endId, isFront);
		}
	}
}