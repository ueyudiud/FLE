package farcore.lib.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBase extends Slot
{
	protected boolean allowCraftingCheck = false;
	
	public SlotBase(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	public SlotBase setAllowCraftingCheck()
	{
		allowCraftingCheck = true;
		return this;
	}

	public boolean canPutStack(EntityPlayer player, ItemStack stack)
	{
		return true;
	}
	
	@Override
	public void onSlotChange(ItemStack crafted, ItemStack source)
	{
		if(allowCraftingCheck)
		{
			if(crafted != null && source != null)
			{
				if(source.isItemEqual(crafted))
				{
					onCrafting(source, source.stackSize - crafted.stackSize);
				}
			}
		}
	}
}