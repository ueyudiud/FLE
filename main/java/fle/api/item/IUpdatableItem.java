package fle.api.item;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import fle.api.world.BlockPos;

public interface IUpdatableItem
{
	public void onInventoryItemUpdate(ItemStack aStack, BlockPos aPos, IInventory inv);
}
