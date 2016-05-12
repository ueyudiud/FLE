package farcore.lib.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCoreSetup;
import farcore.interfaces.tile.IToolClickHandler;
import farcore.lib.net.gui.PacketFluidUpdate;
import farcore.lib.net.gui.PacketFluidUpdateLarge;
import farcore.util.U;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ContainerBase<I extends IInventory> extends Container
{
	protected int[] lastCache;
	public I inventory;
	protected EntityPlayer player;
	public List<FluidSlot> fluidSlotList = new ArrayList();
	public List<FluidStack> fluidList;
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
		addPlayerSlot(8, 142);
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
		return super.slotClick(id, mouseClick, shiftHold, player);
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
        }

        return itemstack;
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