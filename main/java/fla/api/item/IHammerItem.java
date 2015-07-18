package fla.api.item;

import net.minecraft.item.ItemStack;

public interface IHammerItem
{
	public ItemStack getCraftResult(ItemStack hammer, ItemStack target);
}
