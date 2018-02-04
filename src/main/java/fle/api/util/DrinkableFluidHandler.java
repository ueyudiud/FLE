/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.api.util;

import java.util.HashMap;
import java.util.Map;

import nebula.common.util.Maths;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author ueyudiud
 */
public class DrinkableFluidHandler
{
	public static class DrinkableFluidEntry
	{
		public final Fluid	fluid;
		public final int	amount1;
		public final int	amount2;
		
		DrinkableFluidEntry(Fluid fluid, int amount1, int amount2)
		{
			this.fluid = fluid;
			this.amount1 = amount1;
			this.amount2 = amount2;
		}
	}
	
	private static final Map<Fluid, DrinkableFluidEntry> MAP = new HashMap<>();
	
	public static void registerDrinkableFluid(Fluid fluid, int amount)
	{
		registerDrinkableFluid(new DrinkableFluidEntry(fluid, amount, 1));
	}
	
	public static void registerDrinkableFluid(Fluid fluid, int amount1, int amount2)
	{
		int gcd = Maths.gcd(amount1, amount2);
		registerDrinkableFluid(new DrinkableFluidEntry(fluid, amount1 / gcd, amount2 / gcd));
	}
	
	public static void registerDrinkableFluid(DrinkableFluidEntry entry)
	{
		if (entry.amount1 < 1 || entry.amount2 < 1) throw new IllegalArgumentException();
		MAP.put(entry.fluid, entry);
	}
	
	public static DrinkableFluidEntry getEntry(FluidStack stack)
	{
		return stack == null ? null : MAP.get(stack.getFluid());
	}
}
