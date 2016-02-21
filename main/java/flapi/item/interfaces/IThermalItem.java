package flapi.item.interfaces;

import net.minecraft.item.ItemStack;

public interface IThermalItem 
{
	int getTemperature(ItemStack stack);
	
	ItemStack onReseaveHeat(ItemStack stack, double heat);

	ItemStack onEmmitHeat(ItemStack stack, double heat);
	
	double getThermalConductivity(ItemStack stack);
}