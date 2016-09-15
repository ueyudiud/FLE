package farcore.energy;

import farcore.lib.collection.Register;

/**
 * The far core provide an energy formatter to give
 * a unification of all of energy unit registered in
 * far addons.
 * @author ueyudiud
 *
 */
public class EnergyHandler
{
	public static enum EnergyScaleType
	{
		INTEGER,
		LONG,
		FLOAT,
		DOUBLE;
	}
	
	private static final Register<Energy> REGISTER = new Register();

	public static final Energy STANDARD_DOUBLE;
	public static final Energy STANDARD_LONG;
	
	static
	{
		STANDARD_DOUBLE = registerEnergy("standard_double", 1.0, EnergyScaleType.DOUBLE);
		STANDARD_LONG = registerEnergy("standard_long", 1.0, EnergyScaleType.LONG);
	}
	
	public static Energy registerEnergy(String name, double energyScale, EnergyScaleType type)
	{
		if(REGISTER.contain(name))
		{
			Energy energy = REGISTER.get(name);
			if(energy.energyScale != energyScale || energy.type != type)
				throw new RuntimeException("The energy can not cast as one energy type!");
			return energy;
		}
		else
		{
			Energy energy = new Energy(energyScale, type);
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
		private final double energyScale;
		private final EnergyScaleType type;

		Energy(double energyScale, EnergyScaleType type)
		{
			this.energyScale = energyScale;
			this.type = type;
		}

		public Number formatCodedEnergy(long amount)
		{
			switch (type)
			{
			case INTEGER :
				return amount & 0xFFFFFFFF;
			case LONG :
				return amount;
			case DOUBLE:
				return Double.longBitsToDouble(amount);
			case FLOAT:
				return Float.intBitsToFloat((int) (amount & 0xFFFFFFFF));
			default :
				return 0;
			}
		}

		public Number formatEnergy(double amount)
		{
			switch (type)
			{
			case INTEGER :
				return (int) amount;
			case LONG :
				return (long) amount;
			case DOUBLE:
				return amount;
			case FLOAT:
				return (float) amount;
			default :
				return 0;
			}
		}

		public Number transferToAnotherEnergy(long amount, Energy energy)
		{
			Number amt = formatEnergy(amount);
			double mul = energy.energyScale / energyScale;
			switch (energy.type)
			{
			case INTEGER : return (int) (amt.intValue() * mul);
			case LONG : return (long) (amt.longValue() * mul);
			case FLOAT : return (float) (amt.floatValue() * mul);
			case DOUBLE : return (double) (amt.doubleValue() * mul);
			default : return 0;
			}
		}

		public Number transferToAnotherEnergy(double amount, Energy energy)
		{
			double mul = energy.energyScale / energyScale;
			switch (energy.type)
			{
			case INTEGER : return (int) (amount * mul);
			case LONG : return (long) (amount * mul);
			case FLOAT : return (float) (amount * mul);
			case DOUBLE : return (double) (amount * mul);
			default : return 0;
			}
		}
	}
}