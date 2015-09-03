package fle.api.gui;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerBase extends Container
{
	public InventoryPlayer player;
	public IInventory inv;
	protected Map<Integer, Field> syncMap = new HashMap();
	protected Map<Field, Integer> preList;

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
		preList = new HashMap();
	}
	
	public void setField(Field field, int index)
	{
		preList.put(field, index);
	}

	protected void registerSync(int syncID, Field field)
	{
		syncMap.put(syncID, field);
	}
	
    public void addCraftingToCrafters(ICrafting crafter)
    {
    	super.addCraftingToCrafters(crafter);
    	if(preList != null)
    	{
    		setupNetWorkFields();
    	}
    	for(Integer index : syncMap.keySet())
		{
			crafter.sendProgressBarUpdate(this, index, getIntegerFromIndex(index));
		}
    }
    
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
    	if(preList != null)
    	{
    		setupNetWorkFields();
    	}

        for (int i = 0; i < crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting) crafters.get(i);

    		for(Integer index : syncMap.keySet())
    		{
    			boolean flag = false;
    			int x = getIntegerFromIndex(index);
    			int y = getPreIntegerFromIndex(index);
    			if(y != x) flag = true;
    			preList.put(syncMap.get(index), x);
    			if(flag) icrafting.sendProgressBarUpdate(this, index, x);
    		}
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int amount)
    {
    	if(preList != null)
    	{
    		setupNetWorkFields();
    	}
    	preList.put(syncMap.get(index), amount);
    }

    private int getIntegerFromIndex(int index)
    {
		try
		{
			return (Integer) (!syncMap.containsKey(index) ? 0 : syncMap.get(index).get(inv));
		}
		catch (Throwable e)
		{
			return 0;
		}
	}

    private int getPreIntegerFromIndex(int index)
    {
    	return (Integer) (!syncMap.containsKey(index) ? 0 : !preList.containsKey(syncMap.get(index)) ? 0 : preList.get(syncMap.get(index)));
	}
    
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
			return startId < 0 || endId < 0 ? false : i >= startId && i < endId;
		}
		
		public boolean mergeItemStack(ItemStack itemstack, boolean isFront)
		{
			return startId < 0 || endId < 0 ? true : ContainerBase.this.mergeItemStack(itemstack, startId, endId, isFront);
		}
	}
}