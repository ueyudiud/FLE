package flapi.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class SlotHolographic extends Slot
{
	public final int mSlotIndex;
	public boolean mCanInsertItem;
	public boolean mCanStackItem;
	public boolean mPutStackInClient;
	public int mMaxStacksize = 127;

	public SlotHolographic(IInventory aInventory, int aSlotID, int aX, int aY, boolean aCanInsertItem, boolean aCanStackItem)
	{
		this(aInventory, aSlotID, aX, aY, aCanInsertItem, aCanStackItem, 127, true);
	}
	public SlotHolographic(IInventory aInventory, int aSlotID, int aX, int aY, boolean aCanInsertItem, boolean aCanStackItem, int aMaxStacksize, boolean aPutStackInClient)
	{
		super(aInventory, aSlotID, aX, aY);
	    mCanInsertItem = aCanInsertItem;
	    mCanStackItem = aCanStackItem;
	    mMaxStacksize = aMaxStacksize;
	    mSlotIndex = aSlotID;
	    mPutStackInClient = aPutStackInClient;
	}
	
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return mCanInsertItem;
	}
	
	public int getSlotStackLimit()
	{
		return mMaxStacksize;
	}
	  
	public boolean getHasStack()
	{
		return false;
	}
	  
	public ItemStack decrStackSize(int par1)
	{
		if (!mCanStackItem)
		{
			return null;
	    }
		return super.decrStackSize(par1);
	}
	  
	public boolean canTakeStack(EntityPlayer par1EntityPlayer)
	{
		return false;
	}

	public void putStack(ItemStack aStack)
	{
		if(mPutStackInClient)
		{
			if (((inventory instanceof TileEntity)) && (((TileEntity) inventory).getWorldObj().isRemote)) 
			{
				inventory.setInventorySlotContents(getSlotIndex(), aStack);
		    }
		    onSlotChanged();
		}
		else 
		{
			super.putStack(aStack);
		}
	}
}