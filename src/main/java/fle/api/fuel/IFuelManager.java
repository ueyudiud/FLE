package fle.api.fuel;

import net.minecraft.item.ItemStack;

public interface IFuelManager
{
	IFuelValue getFuelValue(ItemStack fuel);
}