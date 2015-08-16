package fle.api.item;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public interface IBagable
{
	int getSize(ItemStack aStack);
	
	ItemStack getItemContain(ItemStack aStack, int i);

	void setItemContain(ItemStack aStack, int i, ItemStack aInput);
	
	boolean isItemValid(ItemStack aStack, ItemStack aInput);
}
