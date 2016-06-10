package farcore.lib.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCoreSetup;
import farcore.interfaces.item.IContainerCustomBehaviorItem;
import farcore.interfaces.tile.IToolClickHandler;
import farcore.lib.net.gui.PacketFluidUpdate;
import farcore.lib.net.gui.PacketFluidUpdateLarge;
import farcore.util.U;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ContainerBase<I extends IInventory> extends Container
{
	protected int[] lastCache;
	public I inventory;
	protected EntityPlayer player;
	public List<FluidSlot> fluidSlotList = new ArrayList();
	public List<FluidStack> fluidList = new ArrayList();
	public Map<String, TransLocate> locates = new HashMap();
	protected TransLocate locateHand;
	protected TransLocate locateBag;
	protected TransLocate locatePlayer;

	public ContainerBase(I inventory)
	{
		this.inventory = inventory;
	}
	public ContainerBase(I inventory, EntityPlayer player)
	{
		this.inventory = inventory;
		this.player = player;
	}

	protected void addPlayerSlot()
	{
		addPlayerSlot(8, 84);
	}
	protected void addPlayerSlot(int xOffset, int yOffset)
	{
		int i;
		int idOffset = inventorySlots.size();

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                addSlotToContainer(new SlotBase(player.inventory, j + i * 9 + 9, j * 18 + xOffset, i * 18 + yOffset));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            addSlotToContainer(new SlotBase(player.inventory, i, i * 18 + xOffset, 58 + yOffset));
        }
        addTransLocate(locateBag = new TransLocate("bag", 0 + idOffset, 27 + idOffset, false, false));
        addTransLocate(locateHand = new TransLocate("hand", 27 + idOffset, 36 + idOffset, false, false));
        addTransLocate(locatePlayer = new TransLocate("player", 0 + idOffset, 36 + idOffset, true, false));
	}
	
	protected int getUpdateSize()
	{
		return 0;
	}
	
	protected void setUpdate(int id, int value)
	{
		
	}
	
	protected int getUpdate(int id)
	{
		return 0;
	}

	protected void addTransLocate(TransLocate locate)
	{
		locates.put(locate.tag, locate);
	}
	
	@Override
	protected SlotBase addSlotToContainer(Slot slot)
	{
		if(!(slot instanceof SlotBase)) throw new ClassCastException("The slot must extended SlotBase!");
		return (SlotBase) super.addSlotToContainer(slot);
	}
	
	protected FluidSlot addSlotToContainer(FluidSlot slot)
	{
		slot.slotNumber = fluidSlotList.size();
		fluidSlotList.add(slot);
		fluidList.add((FluidStack) null);
		return slot;
	}
	
	public void addCraftingToCrafters(ICrafting crafter)
    {
    	super.addCraftingToCrafters(crafter);
    	for(int i = 0; i < getUpdateSize(); ++i)
    	{
    		crafter.sendProgressBarUpdate(this, i, getUpdate(i));
    	}
		if(crafter instanceof EntityPlayerMP)
		{
			if(!fluidSlotList.isEmpty())
			{				
				FarCoreSetup.network.sendToPlayer(new PacketFluidUpdateLarge(this), (EntityPlayerMP) crafter);
			}
//	    	if(!solidSlotList.isEmpty())
//	    	{
//	    		FleAPI.mod.getNetworkHandler().sendLargePacket(new SolidWindowPacket(this), (EntityPlayerMP) crafter);
//	    	}
		}
    }
    
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
		for(int i = 0; i < fluidSlotList.size(); ++i)
		{
			FluidStack fluid1 = fluidSlotList.get(i).getStack();
			FluidStack fluid2 = fluidList.get(i);
			if(!FluidStack.areFluidStackTagsEqual(fluid1, fluid2))
			{
				fluid2 = fluid1 == null ? null : fluid1.copy();
				fluidList.set(i, fluid2);
				for(Object obj : crafters)
				{
					if(obj instanceof EntityPlayerMP)
					{
						FarCoreSetup.network.sendToPlayer(new PacketFluidUpdate(this, i), (EntityPlayerMP) obj);
					}
				}
			}
		}
		if(lastCache == null)
		{
			lastCache = new int[getUpdateSize()];
			int value;
			for(int i = 0; i < getUpdateSize(); ++i)
			{
				value = getUpdate(i);
				for(Object obj : crafters)
				{
					ICrafting crafter = (ICrafting) obj;
					crafter.sendProgressBarUpdate(this, i, value);
				}
				lastCache[i] = value;
			}
		}
		else
		{
			List<int[]> changed = new ArrayList();
			int value;
			for(int i = 0; i < getUpdateSize(); ++i)
			{
				if((value = getUpdate(i)) != lastCache[i])
				{
					for(Object obj : crafters)
					{
						ICrafting crafter = (ICrafting) obj;
						crafter.sendProgressBarUpdate(this, i, value);
					}
					lastCache[i] = value;
				}
			}
		}
//		for(int i = 0; i < solidSlotList.size(); ++i)
//		{
//			SolidStack solid1 = solidSlotList.get(i).getStack();
//			SolidStack solid2 = solidList.get(i);
//			if(!SolidStack.areStackEquls(solid1, solid2))
//			{
//				solid2 = solid1 == null ? null : solid1.copy();
//				solidList.set(i, solid2);
//				for(Object obj : crafters)
//					if(obj instanceof EntityPlayerMP)
//						FleAPI.mod.getNetworkHandler().sendToPlayer(new SolidUpdatePacket(this, i), (EntityPlayerMP) obj);
//			}
//		}
    }
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int value)
	{
		if(lastCache == null)
		{
			lastCache = new int[getUpdateSize()];
		}
		setUpdate(id, lastCache[id] = value);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return inventory.isUseableByPlayer(player);
	}
	
	/**
	 * 
	 * @param index
	 * @param player
	 */
	public void slotFluidClick(int index, EntityPlayer player)
	{
		if(player.inventory.getItemStack() != null)
		{
			if(fluidSlotList.get(index).slotClick(player, player.inventory.getItemStack()))
			{
				detectAndSendChanges();
			}
		}
	}
		
	/**
	 *  Merges provided ItemStack with the first avaliable one in the container/player inventory.<br>
	 *  
	 *  Override method because this method is ignore max stack limit of slot. 
	 */
	@Override
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
				slot = (Slot)this.inventorySlots.get(k);
				itemstack1 = slot.getStack();
				
				//To prevent add stack in to one slot although it does not has subtypes.
				if (itemstack1 != null && U.Inventorys.areStackSimilar(stack, itemstack1))
				{
					int l = itemstack1.stackSize + stack.stackSize;
					
					int m;
					if (l <= (m = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize())))
					{
						stack.stackSize = 0;
						itemstack1.stackSize = l;
						slot.onSlotChanged();
						flag1 = true;
					}
					else if (itemstack1.stackSize < m)
					{
						stack.stackSize -= m - itemstack1.stackSize;
						itemstack1.stackSize = m;
						slot.onSlotChanged();
						flag1 = true;
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
				slot = (Slot)this.inventorySlots.get(k);
				itemstack1 = slot.getStack();
				
				if (itemstack1 == null && slot.isItemValid(stack))
				{
					int m;
					if (stack.stackSize <= (m = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize())))
					{
						slot.putStack(stack.copy());
						slot.onSlotChanged();
						stack.stackSize = 0;
						flag1 = true;
						break;
					}
					else
					{
						slot.putStack(stack.splitStack(m));
						slot.onSlotChanged();
						flag1 = true;
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
		
		return flag1;
	}
	
	@Override
	public ItemStack slotClick(int id, int mouseClick, int shiftHold, EntityPlayer player)
	{
		if(id >= 0 && getSlot(id) instanceof SlotTool)
		{
			IInventory inv = getSlot(id).inventory;
			if(inv instanceof IToolClickHandler)
			{
				ItemStack tStack = player.inventory.getItemStack();
			    if (tStack != null)
			    {
			    	ItemStack stack = ((IToolClickHandler) inv).onToolClick(tStack, player, getSlot(id).getSlotIndex());
			    	player.inventory.setItemStack(U.Inventorys.valid(stack));
			    }
			    return null;
			}
			else
			{
				return null;
			}
		}
		return onSlotClick(id, mouseClick, shiftHold, player);
	}

	private int mouseMode;
	private int mouseMode2;
    private final Set<Slot> clickedSet = new HashSet();
	private byte[] clickCache;
	
    @Override
    public void func_94533_d()
    {
    	clearMode();
    }
    
    protected void clearMode()
    {
    	mouseMode = 0;
    	clickedSet.clear();
    }
    
	protected ItemStack onSlotClick(int id, int mouseClick, int shiftHold, EntityPlayer player)
	{
        ItemStack itemstack = null;
        InventoryPlayer inventoryplayer = player.inventory;
        int i1;
        ItemStack itemstack3;

        if (shiftHold == 5)
        {
            int l = this.mouseMode;
            this.mouseMode = func_94532_c(mouseClick);

            if ((l != 1 || this.mouseMode != 2) && l != this.mouseMode)
            {
                clearMode();
            }
            else if (inventoryplayer.getItemStack() == null)
            {
                clearMode();
            }
            else if (this.mouseMode == 0)
            {
                this.mouseMode2 = func_94529_b(mouseClick);

                if (func_94528_d(this.mouseMode2))
                {
                    this.mouseMode = 1;
                    this.clickedSet.clear();
                }
                else
                {
                    clearMode();
                }
            }
            else if (this.mouseMode == 1)
            {
                Slot slot = (Slot) inventorySlots.get(id);

                if (slot != null && func_94527_a(slot, inventoryplayer.getItemStack(), true) && slot.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize > this.clickedSet.size() && this.canDragIntoSlot(slot))
                {
                    this.clickedSet.add(slot);
                }
            }
            else if (this.mouseMode == 2)
            {
                if (!clickedSet.isEmpty())
                {
                    itemstack3 = inventoryplayer.getItemStack().copy();
                    i1 = inventoryplayer.getItemStack().stackSize;
                    
                    for(Slot slot1 : clickedSet)
                    {
                        if (slot1 != null && func_94527_a(slot1, inventoryplayer.getItemStack(), true) && slot1.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize >= this.clickedSet.size() && this.canDragIntoSlot(slot1))
                        {
                            ItemStack itemstack1 = itemstack3.copy();
                            int j1 = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
                            func_94525_a(this.clickedSet, this.mouseMode2, itemstack1, j1);

                            int k;
                            if (itemstack1.stackSize > (k = Math.min(itemstack1.getMaxStackSize(), slot1.getSlotStackLimit())))
                            {
                                itemstack1.stackSize = k;
                            }

                            i1 -= itemstack1.stackSize - j1;
                            slot1.putStack(itemstack1);
                        }
                    }

                    itemstack3.stackSize = i1;

                    if (itemstack3.stackSize <= 0)
                    {
                        itemstack3 = null;
                    }

                    inventoryplayer.setItemStack(itemstack3);
                }

                clearMode();
            }
            else
            {
                clearMode();
            }
        }
        else if (this.mouseMode != 0)
        {
            clearMode();
        }
        else
        {
            Slot slot2;
            int l1;
            ItemStack itemstack5;

            if ((shiftHold == 0 || shiftHold == 1) && (mouseClick == 0 || mouseClick == 1))
            {
                if (id == -999)
                {
                    if (inventoryplayer.getItemStack() != null && id == -999)
                    {
                        if (mouseClick == 0)
                        {
                            player.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), true);
                            inventoryplayer.setItemStack((ItemStack) null);
                        }

                        if (mouseClick == 1)
                        {
                            player.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack().splitStack(1), true);

                            if (inventoryplayer.getItemStack().stackSize == 0)
                            {
                                inventoryplayer.setItemStack((ItemStack) null);
                            }
                        }
                    }
                }
                else if (shiftHold == 1)
                {
                    if (id < 0)
                    {
                        return null;
                    }

                    slot2 = (Slot)this.inventorySlots.get(id);

                    if (slot2 != null && slot2.canTakeStack(player))
                    {
                        itemstack3 = transferStackInSlot(player, id);

                        if (itemstack3 != null)
                        {
                            Item item = itemstack3.getItem();
                            itemstack = itemstack3.copy();

                            if (slot2.getStack() != null && slot2.getStack().getItem() == item)
                            {
                                retrySlotClick(id, mouseClick, true, player);
                            }
                        }
                    }
                }
                else
                {
                    if (id < 0)
                    {
                        return null;
                    }

                    slot2 = (Slot) inventorySlots.get(id);

                    if (slot2 != null)
                    {
                        itemstack3 = slot2.getStack();
                        ItemStack itemstack4 = inventoryplayer.getItemStack();

                        if (itemstack3 != null)
                        {
                            itemstack = itemstack3.copy();
                        }

                        if (itemstack3 == null)
                        {
                            if (itemstack4 != null && slot2.isItemValid(itemstack4))
                            {
                                l1 = mouseClick == 0 ? itemstack4.stackSize : 1;

                                if (l1 > slot2.getSlotStackLimit())
                                {
                                    l1 = slot2.getSlotStackLimit();
                                }

                                if (itemstack4.stackSize >= l1)
                                {
                                    slot2.putStack(itemstack4.splitStack(l1));
                                }

                                if (itemstack4.stackSize == 0)
                                {
                                    inventoryplayer.setItemStack((ItemStack) null);
                                }
                            }
                        }
                        else if (slot2.canTakeStack(player))
                        {
                            if (itemstack4 == null)
                            {
                            	if(itemstack3.getItem() instanceof IContainerCustomBehaviorItem && mouseClick != 0)//Custom behavior start.
                            	{
                            		IContainerCustomBehaviorItem item = (IContainerCustomBehaviorItem) itemstack3.getItem();
                            		inventoryplayer.setItemStack(item.splitItemWhenRightClick(slot2, itemstack3));
                            	}
                            	else
                            	{
                                    l1 = mouseClick == 0 ? itemstack3.stackSize : (itemstack3.stackSize + 1) / 2;
                                    itemstack5 = slot2.decrStackSize(l1);
                                    inventoryplayer.setItemStack(itemstack5);

                                    if (itemstack3.stackSize == 0)
                                    {
                                        slot2.putStack((ItemStack)null);
                                    }
                            	}

                                slot2.onPickupFromSlot(player, inventoryplayer.getItemStack());
                            }
                            else if (slot2.isItemValid(itemstack4))
                            {
                            	if(itemstack3.getItem() instanceof IContainerCustomBehaviorItem)//Custom behavior start.
                            	{
                            		IContainerCustomBehaviorItem item = (IContainerCustomBehaviorItem) itemstack3.getItem();
                            		if(item.canAccessStack(slot2, itemstack3, itemstack4))
                            		{
                            			itemstack3 = item.splitSize(slot2, itemstack3, itemstack4, mouseClick == 0);
                            			inventoryplayer.setItemStack(itemstack3);
                            		}
                            		else if (itemstack4.stackSize <= slot2.getSlotStackLimit())
                                    {
                                        slot2.putStack(itemstack4);
                                        inventoryplayer.setItemStack(itemstack3);
                                    }
                            	}
                            	else
                                if (U.Inventorys.areStackSimilar(itemstack3, itemstack4))
                                {
                                    l1 = mouseClick == 0 ? itemstack4.stackSize : 1;
                                    int m = Math.min(slot2.getSlotStackLimit(), itemstack4.getMaxStackSize());
                                    if (l1 > m - itemstack3.stackSize)
                                    {
                                        l1 = m - itemstack3.stackSize;
                                    }

                                    itemstack4.splitStack(l1);

                                    if (itemstack4.stackSize == 0)
                                    {
                                        inventoryplayer.setItemStack((ItemStack) null);
                                    }

                                    itemstack3.stackSize += l1;
                                }
                                else if (itemstack4.stackSize <= slot2.getSlotStackLimit())
                                {
                                    slot2.putStack(itemstack4);
                                    inventoryplayer.setItemStack(itemstack3);
                                }
                            }
                            else if (itemstack3.getItem() == itemstack4.getItem() && itemstack4.getMaxStackSize() > 1 && (!itemstack3.getHasSubtypes() || itemstack3.getItemDamage() == itemstack4.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemstack3, itemstack4))
                            {
                                l1 = itemstack3.stackSize;

                                if (l1 > 0 && l1 + itemstack4.stackSize <= itemstack4.getMaxStackSize())
                                {
                                    itemstack4.stackSize += l1;
                                    itemstack3 = slot2.decrStackSize(l1);

                                    if (itemstack3.stackSize == 0)
                                    {
                                        slot2.putStack((ItemStack)null);
                                    }

                                    slot2.onPickupFromSlot(player, inventoryplayer.getItemStack());
                                }
                            }
                        }

                        slot2.onSlotChanged();
                    }
                }
            }
            else if (shiftHold == 2 && mouseClick >= 0 && mouseClick < 9)//Fixed over inventory stack limit bug.
            {
                slot2 = (Slot)this.inventorySlots.get(id);

                if (slot2.canTakeStack(player))
                {
                    itemstack3 = inventoryplayer.getStackInSlot(mouseClick);
                    if(slot2.inventory == inventoryplayer)
                    {
                    	inventoryplayer.setInventorySlotContents(mouseClick, slot2.getStack());
                    	slot2.putStack(itemstack3);
                    }
                    else
                    {
                        if (slot2.getHasStack())
                        {
                            itemstack5 = slot2.getStack();
                            l1 = mouseClick;
                            label:
                        	if(itemstack3 != null)
                        	{
                        		if(!slot2.isItemValid(itemstack3)) break label;
                                int m = Math.min(itemstack3.getMaxStackSize(), slot2.getSlotStackLimit());
                                if(!U.Inventorys.areStackSimilar(itemstack3, itemstack5))
                                {
                                    if(m < itemstack3.stackSize)
                                    {
                                    	l1 = inventoryplayer.getFirstEmptyStack();
                                    	if(l1 == -1) break label;
                                    }
                                    slot2.putStack(inventoryplayer.decrStackSize(mouseClick, m));
                                    itemstack3 = inventoryplayer.getStackInSlot(mouseClick);
                                }
                                else
                                {
                                    m = itemstack3.getMaxStackSize();
                                    int m1 = Math.min(m - itemstack3.stackSize, itemstack5.stackSize);
                                    if(itemstack5.stackSize - m1 > 0)
                                    {
                                        l1 = inventoryplayer.getFirstEmptyStack();
                                        if(l1 == -1) break label;
                                        itemstack3.stackSize += m1;
                                        itemstack5 = itemstack5.copy();
                                        itemstack5.stackSize -= m1;
                                        slot2.decrStackSize(m1);
                                    	inventoryplayer.setInventorySlotContents(l1, itemstack5);
                                    }
                                    else
                                    {
                                        itemstack3.stackSize += m1;
                                        slot2.decrStackSize(m1);
                                    }
                                    itemstack5.stackSize = 0;
                                	slot2.putStack(null);
                                }
                        	}
                        	else
                            {
                            	slot2.putStack(null);
                            }
                            if(itemstack5.stackSize > 0)
                            {
                            	inventoryplayer.setInventorySlotContents(l1, itemstack5.copy());
                            }
                        }
                        else if (!slot2.getHasStack() && itemstack3 != null && slot2.isItemValid(itemstack3))
                        {
                        	int m = Math.min(itemstack3.getMaxStackSize(), slot2.getSlotStackLimit());
                            slot2.putStack(inventoryplayer.decrStackSize(mouseClick, m));
                        }
                    }
                    detectAndSendChanges();
                }
            }
            else if (shiftHold == 3 && player.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && id >= 0)
            {
                slot2 = (Slot)this.inventorySlots.get(id);

                if (slot2 != null && slot2.getHasStack())
                {
                    itemstack3 = slot2.getStack().copy();
                    itemstack3.stackSize = itemstack3.getMaxStackSize();
                    inventoryplayer.setItemStack(itemstack3);
                }
            }
            else if (shiftHold == 4 && inventoryplayer.getItemStack() == null && id >= 0)
            {
                slot2 = (Slot)this.inventorySlots.get(id);

                if (slot2 != null && slot2.getHasStack() && slot2.canTakeStack(player))
                {
                    itemstack3 = slot2.decrStackSize(mouseClick == 0 ? 1 : slot2.getStack().stackSize);
                    slot2.onPickupFromSlot(player, itemstack3);
                    player.dropPlayerItemWithRandomChoice(itemstack3, true);
                }
            }
            else if (shiftHold == 6 && id >= 0)
            {
                slot2 = (Slot)this.inventorySlots.get(id);
                itemstack3 = inventoryplayer.getItemStack();

                if (itemstack3 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(player)))
                {
                    i1 = mouseClick == 0 ? 0 : this.inventorySlots.size() - 1;
                    l1 = mouseClick == 0 ? 1 : -1;

                    for (int i2 = 0; i2 < 2; ++i2)
                    {
                        for (int j2 = i1; j2 >= 0 && j2 < this.inventorySlots.size() && itemstack3.stackSize < itemstack3.getMaxStackSize(); j2 += l1)
                        {
                            Slot slot3 = (Slot)this.inventorySlots.get(j2);

                            if (slot3.getHasStack() && func_94527_a(slot3, itemstack3, true) && slot3.canTakeStack(player) && this.func_94530_a(itemstack3, slot3) && (i2 != 0 || slot3.getStack().stackSize != slot3.getStack().getMaxStackSize()))
                            {
                                int k1 = Math.min(itemstack3.getMaxStackSize() - itemstack3.stackSize, slot3.getStack().stackSize);
                                ItemStack itemstack2 = slot3.decrStackSize(k1);
                                itemstack3.stackSize += k1;

                                if (itemstack2.stackSize <= 0)
                                {
                                    slot3.putStack((ItemStack)null);
                                }

                                slot3.onPickupFromSlot(player, itemstack2);
                            }
                        }
                    }
                }

                detectAndSendChanges();
            }
        }

        return itemstack;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int idx)
	{
        ItemStack itemstack = null;
        SlotBase slot = (SlotBase) inventorySlots.get(idx);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            for(TransLocate locate : locates.values())
            {
            	if(!locate.match(slot, itemstack, itemstack1))
            	{
            		if (itemstack1.stackSize == 0)
                    {
                        slot.putStack((ItemStack) null);
                    }
					detectAndSendChanges();
            		return null;
            	}
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

            slot.onPickupFromSlot(player, itemstack1);
			detectAndSendChanges();
        }

        return itemstack;
	}

    public static boolean func_94527_a(Slot slot, ItemStack stack, boolean flag)
    {
        boolean flag1 = slot == null || !slot.getHasStack();

        if (slot != null && slot.getHasStack() && stack != null && stack.isItemEqual(slot.getStack()) && ItemStack.areItemStackTagsEqual(slot.getStack(), stack))
        {
            int i = flag ? 0 : stack.stackSize;
            flag1 |= slot.getStack().stackSize + i <= stack.getMaxStackSize();
        }

        return flag1;
    }
	
	public class TransLocate
	{
		int startID;
		int endID;
		boolean flag;
		boolean causeSlotChange;
		String tag;
		LinkedList<TransLocate> target = new LinkedList();

		public TransLocate(String tag, int locate)
		{
			this(tag, locate, false);
		}
		public TransLocate(String tag, int locate, boolean causeSlotChance)
		{
			this(tag, locate, locate + 1, false, causeSlotChance);
		}
		public TransLocate(String tag, int start, int end, boolean flag, boolean causeSlotChange)
		{
			this.tag = tag;
			this.startID = start;
			this.endID = end;
			this.flag = flag;
			this.causeSlotChange = causeSlotChange;
		}
		
		public TransLocate append(TransLocate locate)
		{
			target.addLast(locate);
			return this;
		}
		
		public TransLocate appendFirst(TransLocate locate)
		{
			target.addFirst(locate);
			return this;
		}
		
		public boolean isItemValid(ItemStack stack)
		{
			return true;
		}
		
		public boolean match(SlotBase slot, ItemStack old, ItemStack stack)
		{
			if(!contain(slot.slotNumber)) return true;
			boolean flag = false;
			for(TransLocate locate : target)
			{
				if(locate.isItemValid(stack))
				{
					if(!mergeItemStack(stack, locate.startID, locate.endID, locate.flag))
					{
						continue;
					}
					flag = true;
					if(causeSlotChange)
					{
						slot.onSlotChange(old, stack);
					}
				}
			}
			return flag;
		}
		
		public boolean contain(int id)
		{
			return startID <= id && endID > id;
		}
	}
}