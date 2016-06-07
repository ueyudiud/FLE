package fle.api.fuel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class FuelHandler
{
	private static final List<IFuelManager> list = new ArrayList();
	
	public static void addFuelManager(IFuelManager manager)
	{
		list.add(manager);
	}
	
	public static IFuelValue getFuelValue(ItemStack stack)
	{
		if(stack == null) return null;
		IFuelValue value;
		for(IFuelManager manager : list)
		{
			if((value = manager.getFuelValue(stack)) != null)
			{
				return value;
			}
		}
		return null;
	}
}