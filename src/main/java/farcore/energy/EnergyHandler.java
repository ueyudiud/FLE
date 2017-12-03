/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.energy;

import nebula.base.Register;
import nebula.common.util.Strings;

/**
 * The far core provide an energy formatter to give a unification of all of
 * energy unit registered in far addons.
 * 
 * @author ueyudiud
 *
 */
public class EnergyHandler
{
	public static enum EnergyScaleType
	{
		INTEGER, LONG, FLOAT, DOUBLE;
	}
	
	private static final Register<Energy> REGISTER = new Register<>();
	
	public static final Energy	STANDARD_DOUBLE;
	public static final Energy	STANDARD_LONG;
	
	static
	{
		STANDARD_DOUBLE = registerEnergy("standard_double", "J", 1.0, EnergyScaleType.DOUBLE);
		STANDARD_LONG = registerEnergy("standard_long", "J", 1.0, EnergyScaleType.LONG);
	}
	
	public static Energy registerEnergy(String name, String unit, double energyScale, EnergyScaleType type)
	{
		if (REGISTER.contain(name))
		{
			Energy energy = REGISTER.get(name);
			if (energy.energyScale != energyScale || energy.type != type) throw new RuntimeException("The energy can not cast as one energy type!");
			return energy;
		}
		else
		{
			Energy energy = new Energy(unit, energyScale, type);
			REGISTER.register(name, energy);
			return energy;
		}
	}
	
	public static Energy getEnergy(String name)
	{
		return REGISTER.get(name);
	}
	
	public static class Energy
	{
		public final String				unit;
		private final double			energyScale;
		private final EnergyScaleType	type;
		
		Energy(String unit, double energyScale, EnergyScaleType type)
		{
			this.unit = unit;
			this.energyScale = energyScale;
			this.type = type;
		}
		
		public long encode(Number value)
		{
			switch (this.type)
			{
			case INTEGER:
				return value.intValue();
			case LONG:
				return value.longValue();
			case DOUBLE:
				return Double.doubleToLongBits(value.doubleValue());
			case FLOAT:
				return Float.floatToIntBits(value.floatValue());
			default:
				return 0;
			}
		}
		
		public Number decode(long amount)
		{
			switch (this.type)
			{
			case INTEGER:
				return amount & 0xFFFFFFFF;
			case LONG:
				return amount;
			case DOUBLE:
				return Double.longBitsToDouble(amount);
			case FLOAT:
				return Float.intBitsToFloat((int) (amount & 0xFFFFFFFF));
			default:
				return 0;
			}
		}
		
		public Number formatEnergy(double amount)
		{
			switch (this.type)
			{
			case INTEGER:
				return (int) amount;
			case LONG:
				return (long) amount;
			default:
			case DOUBLE:
				return amount;
			case FLOAT:
				return (float) amount;
			}
		}
		
		public Number transferToAnotherEnergy(long amount, Energy energy)
		{
			double mul = energy.energyScale / this.energyScale;
			switch (energy.type)
			{
			case INTEGER:
				return (int) (amount * mul);
			case LONG:
				return (long) (amount * mul);
			case FLOAT:
				return (float) (amount * mul);
			case DOUBLE:
				return (double) (amount * mul);
			default:
				return 0;
			}
		}
		
		public Number transferToAnotherEnergy(double amount, Energy energy)
		{
			double mul = energy.energyScale / this.energyScale;
			switch (energy.type)
			{
			case INTEGER:
				return (int) (amount * mul);
			case LONG:
				return (long) (amount * mul);
			case FLOAT:
				return (float) (amount * mul);
			case DOUBLE:
				return (double) (amount * mul);
			default:
				return 0;
			}
		}
		
		public String formatDisplayEnergy(Number energy)
		{
			switch (this.type)
			{
			case INTEGER:
				return Integer.toString(energy.intValue()) + this.unit;
			case LONG:
				return Long.toString(energy.longValue()) + this.unit;
			case FLOAT:
				return Strings.getDecimalNumber(energy.floatValue(), 2) + this.unit;
			default:
			case DOUBLE:
				return Strings.getDecimalNumber(energy.doubleValue(), 3) + this.unit;
			}
		}
	}
}
