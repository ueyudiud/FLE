package fla.api.recipe;

import net.minecraft.util.IIcon;
import fla.api.util.Registry;

public abstract class Fuel
{
	public static Registry<Fuel> fuelMap = new Registry();
	
	public static void registryFuel(Fuel fuel)
	{
		fuelMap.register(fuel, fuel.getName());
	}
	public static Fuel getFuel(String fuelName)
	{
		return fuelMap.get(fuelName);
	}
	public static boolean isFuel(String fuelName)
	{
		Fuel fuel = fuelMap.get(fuelName);
		if(fuel != null)
		{
			return true;
		}
		return false;
	}
	public static boolean isFuel(String fuelName, Class<? extends Fuel> clazz)
	{
		Fuel fuel = fuelMap.get(fuelName);
		if(fuel != null)
		{
			if(clazz.isAssignableFrom(fuel.getClass()) || clazz == fuel.getClass())
			{
				return true;
			}
		}
		return false;
	}
	
	public abstract String getName();
	
	/**
	 * Get fuel buffer, mean get it burning time when it is burning.
	 * @return
	 */
	public abstract float getFuelBuffer();
	
	/**
	 * Get fuel texture.
	 * @return
	 */
	public abstract String getFuelTextureName();
	
	public abstract IIcon getIcon();
	
	public abstract Fuel setIcon(IIcon icon);
}
