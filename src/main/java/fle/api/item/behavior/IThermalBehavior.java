package fle.api.item.behavior;

import farcore.interfaces.energy.thermal.IThermalItem;
import fle.api.util.TemperatureHandler;
import net.minecraft.item.ItemStack;

public interface IThermalBehavior
{
	boolean canOutputThermalEnergy();

	float getThermalConductivity();
	
	float getHeatCapacity();

	default ItemStack setTemperature(ItemStack stack, float temp)
	{
		return TemperatureHandler.setTemperature(stack, temp);
	}
	
	default float getTemperature(ItemStack target)
	{
		return TemperatureHandler.getTemperature(target);
	}
	
	default ItemStack onHeatChanged(ItemStack target, float deltaHeat)
	{
		float temp = getTemperature(target);
		float deltaTemp = deltaHeat / getHeatCapacity();
		return setTemperature(target, temp + deltaTemp);
	}
}