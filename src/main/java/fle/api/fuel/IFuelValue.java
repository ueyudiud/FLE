package fle.api.fuel;

import net.minecraft.item.ItemStack;

public interface IFuelValue
{
	float getMaxTempreture(ItemStack stack);
	
	float getMinBurnPoint(ItemStack stack);
	
	float getMaxPower(ItemStack stack, float tempreture);
	
	int getEnergyCurrent(ItemStack stack);
	
	ItemStack getOutput(ItemStack stack);
}