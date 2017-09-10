/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.api.recipes.instance;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import nebula.common.stack.AbstractStack;
import nebula.common.util.L;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class FuelHandler
{
	public static class FuelKey
	{
		public final AbstractStack fuel;
		public final float flameTemperature;
		public final float normalTemperature;
		public final int normalPower;
		public final long fuelValue;
		
		FuelKey(AbstractStack fuel, float flameTemperature, float normalTemperature, int normalPower, long fuelValue)
		{
			this.fuel = fuel;
			this.flameTemperature = flameTemperature;
			this.normalTemperature = normalTemperature;
			this.normalPower = normalPower;
			this.fuelValue = fuelValue;
		}
	}
	
	private static final List<FuelKey> FUELS = new ArrayList<>();
	
	public static void addFuel(AbstractStack fuel, float flameTemperature, float normalTemperature, int normalPower, long fuelValue)
	{
		addFuel(fuel, flameTemperature, flameTemperature, normalTemperature, normalPower, fuelValue);
	}
	
	public static void addFuel(AbstractStack fuel, float smoderTemperature, float flameTemperature, float normalTemperature, int normalPower, long fuelValue)
	{
		FUELS.add(new FuelKey(fuel, flameTemperature, normalTemperature, normalPower, fuelValue));
		FlamableItems.addFlamableItem(fuel, smoderTemperature, flameTemperature);
	}
	
	public static boolean isFuel(ItemStack stack)
	{
		return L.contain(FUELS, f->f.fuel.similar(stack));
	}
	
	@Nullable
	public static FuelKey getFuel(ItemStack stack)
	{
		return L.get(FUELS, f->f.fuel.contain(stack));
	}
}