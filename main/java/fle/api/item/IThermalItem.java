package fle.api.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public interface IThermalItem 
{
	public int getTemperature(ItemStack stack);
	
	public int onReseaveHeat(ItemStack stack, int heat);

	public int onEmmitHeat(ItemStack stack, int heat);
	
	public int getThermalConductivity(ItemStack stack);
}