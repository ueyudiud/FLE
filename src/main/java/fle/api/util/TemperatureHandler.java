package fle.api.util;

import farcore.interfaces.energy.thermal.IThermalItem;
import farcore.interfaces.energy.thermal.IThermalTile;
import farcore.util.U;
import net.minecraft.item.ItemStack;

public class TemperatureHandler
{
	public static float getTemperature(ItemStack stack)
	{
		return U.Inventorys.setupNBT(stack, false).getFloat("temp");
	}
	
	public static ItemStack setTemperature(ItemStack stack, float temp)
	{
		U.Inventorys.setupNBT(stack, true).setFloat("temp", temp);
		return stack;
	}
	
	public static ItemStack updateThermalItem(IThermalTile tile, ItemStack stack)
	{
		if(stack == null) return null;
		if(stack.getItem() instanceof IThermalItem)
		{
			return ((IThermalItem) stack.getItem()).updateItemInThermalTile(tile, stack);
		}
		else
		{
			return stack;
		}
	}
}