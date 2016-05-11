package fle.api.fuel;

import net.minecraft.item.ItemStack;

public interface IFuelValue
{
	float getMaxTempreture();
	
	float getMinBurnPoint();
	
	float getMaxPower(float tempreture);
	
	int getEnergyCurrent();
	
	ItemStack getOutput();
}