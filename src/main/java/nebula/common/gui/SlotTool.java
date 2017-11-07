package nebula.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotTool extends SlotBase
{
	public SlotTool(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer playerIn)
	{
		return false;
	}
	
	@Override
	public boolean canPutStack(EntityPlayer player, ItemStack stack)
	{
		return false;
	}
	
	@Override
	public ItemStack getStack()
	{
		return null;
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return 1;
	}
	
	@Override
	public void putStack(ItemStack stack)
	{
		;
	}
	
	@Override
	public ItemStack decrStackSize(int amount)
	{
		return null;
	}
	
	@Override
	public void onSlotChanged()
	{
		;
	}
	
	@Override
	public void onSlotChange(ItemStack crafted, ItemStack source)
	{
		;
	}
	
	@Override
	public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack)
	{
		;
	}
}
