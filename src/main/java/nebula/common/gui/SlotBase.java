package nebula.common.gui;

import nebula.common.inventory.IBasicInventory;
import nebula.common.inventory.InventoryWrapFactory;
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
		this.allowCraftingCheck = true;
		return this;
	}
	
	public boolean isHere(IBasicInventory inventory, int slot)
	{
		return InventoryWrapFactory.unwrap(this.inventory) == inventory && getSlotIndex() == slot;
	}
	
	public boolean canPutStack(EntityPlayer player, ItemStack stack)
	{
		return true;
	}
	
	@Override
	public void onSlotChange(ItemStack crafted, ItemStack source)
	{
		if (this.allowCraftingCheck)
		{
			if (crafted != null && source != null)
			{
				if (source.isItemEqual(crafted))
				{
					onCrafting(source, source.stackSize - crafted.stackSize);
				}
			}
		}
	}
}
