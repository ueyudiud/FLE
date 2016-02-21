package flapi.item.interfaces;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import flapi.world.BlockPos;

public interface IUpdatableItem
{
	void onInventoryItemUpdate(ItemStack aStack, BlockPos aPos, IInventory inv);
}
