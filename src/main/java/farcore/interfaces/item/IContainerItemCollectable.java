package farcore.interfaces.item;

import net.minecraft.item.ItemStack;

public interface IContainerItemCollectable extends IContainerItem
{
	boolean canCollectItemToContainer(ItemStack container, ItemStack stack);
	
	int collectItemToContainer(ItemStack container, ItemStack stack);
}