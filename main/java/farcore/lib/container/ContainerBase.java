package farcore.lib.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import farcore.FarCoreSetup;
import farcore.lib.net.gui.PacketFluidUpdate;
import farcore.lib.net.gui.PacketFluidUpdateLarge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidStack;

public class ContainerBase<I extends IInventory> extends Container
{
	public I inventory;
	InventoryPlayer player;
	public List<FluidSlot> fluidSlotList = new ArrayList();
	public List<FluidStack> fluidList;
	public Map<String, TransLocate> locates = new HashMap();

	public ContainerBase(I inventory)
	{
		this.inventory = inventory;
	}
	public ContainerBase(I inventory, InventoryPlayer player)
	{
		this.inventory = inventory;
		this.player = player;
	}

	protected void addPlayerSlot()
	{
		addPlayerSlot(0, 0);
	}
	protected void addPlayerSlot(int xOffset, int yOffset)
	{
		int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18 + xOffset, 84 + i * 18 + yOffset));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            addSlotToContainer(new Slot(player, i, 8 + i * 18 + xOffset, 142 + yOffset));
        }
	}

	protected void addTransLocate(TransLocate locate)
	{
		locates.put(locate.tag, locate);
	}
	
	@Override
	protected Slot addSlotToContainer(Slot slot)
	{
		if(!(slot instanceof SlotBase)) throw new ClassCastException("The slot must extended SlotBase!");
		return super.addSlotToContainer(slot);
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
						FarCoreSetup.network.sendToPlayer(new PacketFluidUpdate(this, i), (EntityPlayerMP) obj);
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
	public ItemStack transferStackInSlot(EntityPlayer player, int idx)
	{
        ItemStack itemstack = null;
        SlotBase slot = (SlotBase) inventorySlots.get(idx);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            for(String string : slot.getTransferTarget())
            {
            	TransLocate locate = locates.get(string);
            	if(locate == null) continue;
            	if(!locate.match(slot, itemstack, itemstack1))
            	{
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
		TransLocate next;

		public TransLocate(String tag, int locate)
		{
			this(tag, locate, false);
		}
		public TransLocate(String tag, int locate, boolean causeSlotChance)
		{
			this(tag, locate, locate + 1, false, causeSlotChance);
		}
		public TransLocate(String tag, int start, int end, boolean flag, boolean causeSlotChance)
		{
			this.tag = tag;
			this.startID = start;
			this.endID = end;
			this.flag = flag;
		}
		
		public boolean isItemValid(ItemStack stack)
		{
			return true;
		}
		
		public boolean match(SlotBase slot, ItemStack old, ItemStack stack)
		{
			if(isItemValid(stack))
			{
				if(!mergeItemStack(stack, startID, endID, flag))
				{
					return false;
				}
				if(causeSlotChange)
				{
					slot.onSlotChange(old, stack);
				}
			}
			if(next != null)
			{
				return next.match(slot, old, stack);
			}
			return true;
		}
	}
}