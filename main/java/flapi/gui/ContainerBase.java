package flapi.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.FleAPI;
import flapi.net.FluidUpdatePacket;
import flapi.net.FluidWindowPacket;
import flapi.net.SolidUpdatePacket;
import flapi.net.SolidWindowPacket;
import flapi.solid.SolidStack;
import flapi.te.interfaces.IContainerNetworkUpdate;
import flapi.te.interfaces.IObjectInWorld;

public abstract class ContainerBase extends Container
{
	public InventoryPlayer player;
	public IInventory inv;
	protected int[] lastNumbers;
	public List<FluidSlot> fluidSlotList;
	public List<FluidStack> fluidList;
	public List<SolidSlot> solidSlotList;
	public List<SolidStack> solidList;

	public ContainerBase(InventoryPlayer player, IInventory inventory) 
	{
		this.player = player;
		this.inv = inventory;
		if(inv instanceof IContainerNetworkUpdate)
			lastNumbers = new int[((IContainerNetworkUpdate) inv).getProgressSize()];
		fluidList = new ArrayList<FluidStack>();
		fluidSlotList = new ArrayList<FluidSlot>();
		solidList = new ArrayList<SolidStack>();
		solidSlotList = new ArrayList<SolidSlot>();
	}
	public ContainerBase(IInventory inventory) 
	{
		this.inv = inventory;
		if(inv instanceof IContainerNetworkUpdate)
			lastNumbers = new int[((IContainerNetworkUpdate) inv).getProgressSize()];
		fluidList = new ArrayList<FluidStack>();
		fluidSlotList = new ArrayList<FluidSlot>();
		solidList = new ArrayList<SolidStack>();
		solidSlotList = new ArrayList<SolidSlot>();
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) 
	{
		return inv.isUseableByPlayer(player);
	}
	
	public void addCraftingToCrafters(ICrafting crafter)
    {
		if(crafter instanceof EntityPlayerMP && inv instanceof IObjectInWorld)
		{
			if(!fluidSlotList.isEmpty())
			{				
				FleAPI.mod.getNetworkHandler().sendLargePacket(new FluidWindowPacket(this), (EntityPlayerMP) crafter);
			}
	    	if(!solidSlotList.isEmpty())
	    	{
	    		FleAPI.mod.getNetworkHandler().sendLargePacket(new SolidWindowPacket(this), (EntityPlayerMP) crafter);
	    	}
		}
    	super.addCraftingToCrafters(crafter);
    	if (inv instanceof IContainerNetworkUpdate)
    	{
			IContainerNetworkUpdate update = (IContainerNetworkUpdate) inv;
			for(int i = 0; i < update.getProgressSize(); ++i)
			{
				crafter.sendProgressBarUpdate(this, i, update.getProgress(i));
			}
		}
    }
    
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
    	if (inv instanceof IContainerNetworkUpdate)
    	{
			IContainerNetworkUpdate update = (IContainerNetworkUpdate) inv;
    		for(Object rawCrafter : crafters)
    		{
    			ICrafting crafter = (ICrafting) rawCrafter;
    			for(int i = 0; i < lastNumbers.length; ++i)
    			{
    				if(lastNumbers[i] != update.getProgress(i))
    					crafter.sendProgressBarUpdate(this, i, update.getProgress(i));
    			}
    		}
			for(int i = 0; i < lastNumbers.length; ++i)
				lastNumbers[i] = update.getProgress(i);
    	}
		for(int i = 0; i < fluidSlotList.size(); ++i)
		{
			FluidStack fluid1 = fluidSlotList.get(i).getStack();
			FluidStack fluid2 = fluidList.get(i);
			if(!FluidStack.areFluidStackTagsEqual(fluid1, fluid2))
			{
				fluid2 = fluid1 == null ? null : fluid1.copy();
				fluidList.set(i, fluid2);
				for(Object obj : crafters)
					if(obj instanceof EntityPlayerMP)
						FleAPI.mod.getNetworkHandler().sendToPlayer(new FluidUpdatePacket(this, i), (EntityPlayerMP) obj);
			}
		}
		for(int i = 0; i < solidSlotList.size(); ++i)
		{
			SolidStack solid1 = solidSlotList.get(i).getStack();
			SolidStack solid2 = solidList.get(i);
			if(!SolidStack.areStackEquls(solid1, solid2))
			{
				solid2 = solid1 == null ? null : solid1.copy();
				solidList.set(i, solid2);
				for(Object obj : crafters)
					if(obj instanceof EntityPlayerMP)
						FleAPI.mod.getNetworkHandler().sendToPlayer(new SolidUpdatePacket(this, i), (EntityPlayerMP) obj);
			}
		}
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int amount)
    {
    	if (inv instanceof IContainerNetworkUpdate)
    	{
			((IContainerNetworkUpdate) inv).setProgress(index, amount);
    	}
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
	
	@Override
	public ItemStack slotClick(int slotID, int mouseClick, int shiftHold,
			EntityPlayer player)
	{
		if(slotID >= 0 && getSlot(slotID) instanceof SlotTool)
		{
			IInventory inv = getSlot(slotID).inventory;
			if(inv instanceof IToolClickHandler)
			{
				ItemStack tStack = player.inventory.getItemStack();
			    if (tStack != null)
			    {
			    	ItemStack stack = ((IToolClickHandler) inv).onToolClick(tStack, player, getSlot(slotID).getSlotIndex());
			    	player.inventory.setItemStack(stack.stackSize <= 0 ? null : stack);
			    }
			    return null;
			}
		}
		return super.slotClick(slotID, mouseClick, shiftHold, player);
	}
	
	public abstract boolean transferStackInSlot(Slot slot, ItemStack baseItemStack, ItemStack itemstack, int locate);

	protected FluidSlot addSlotToContainer(FluidSlot slot)
	{
		slot.slotNumber = fluidSlotList.size();
		fluidSlotList.add(slot);
		fluidList.add((FluidStack) null);
		return slot;
	}
	protected SolidSlot addSlotToContainer(SolidSlot slot)
	{
		slot.slotNumber = solidSlotList.size();
		solidSlotList.add(slot);
		solidList.add((SolidStack) null);
		return slot;
	}
	
	protected boolean mergeItemStack(ItemStack stack, int start, int end, boolean flag)
	{
        boolean flag1 = false;
        int k = flag ? end - 1 : start;

        Slot slot;
        ItemStack itemstack1;

        if (stack.isStackable())
        {
            while (stack.stackSize > 0 && (!flag && k < end || flag && k >= start))
            {
                slot = (Slot) this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (slot.isItemValid(stack))
                {
                    if (itemstack1 != null && itemstack1.getItem() == stack.getItem() && (stack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, itemstack1))
                    {
                        int l = itemstack1.stackSize + stack.stackSize;

                        if (l <= stack.getMaxStackSize())
                        {
                            stack.stackSize = 0;
                            itemstack1.stackSize = l;
                            slot.onSlotChanged();
                            flag1 = true;
                        }
                        else if (itemstack1.stackSize < stack.getMaxStackSize())
                        {
                            stack.stackSize -= stack.getMaxStackSize() - itemstack1.stackSize;
                            itemstack1.stackSize = stack.getMaxStackSize();
                            slot.onSlotChanged();
                            flag1 = true;
                        }
                    }
                }

                if (flag)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        if (stack.stackSize > 0)
        {
            if (flag)
            {
                k = end - 1;
            }
            else
            {
                k = start;
            }

            while (!flag && k < end || flag && k >= start)
            {
                slot = (Slot) this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (slot.isItemValid(stack) && itemstack1 == null)
                {
                    slot.putStack(stack.copy());
                    slot.onSlotChanged();
                    stack.stackSize = 0;
                    flag1 = true;
                    break;
                }

                if (flag)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        return flag1;
    }
	
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