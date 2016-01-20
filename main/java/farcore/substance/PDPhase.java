package farcore.substance;

import farcore.substance.PhaseDiagram.Phase;

/**
 * Standard material phase diagram.<br>
 * The defualt value about temperature is in 101kPa condition.
 * (Without three point temperature, superfluity temperature)<br>
 * 
 * Used to get the phase the substance are being.
 * @author ueyudiud
 *
 */
public class PDPhase implements PhaseDiagram<IPhaseCondition, Phase>
{
	public static final long STANDARD_PRESS = 101000;
	public static final long STANDARD_TEMP = 273;
	
	final long pM;
	final long pB;
	boolean enableP = false;
	final long pP;
	boolean enableLC = false;
	long pLC = -1;
	boolean enableSF = false;
	long pSFt = -1;
	long pSFp = -1;
	boolean enableEM = false;
	long pEM = -1;
	long Tt = 0;
	long Tp = 0;
	double pE = 0.0D;
	long pMPE = STANDARD_PRESS;
	
	public PDPhase(long meltingPoint)
	{
		this(meltingPoint, meltingPoint * 10, meltingPoint * 200);
	}
	public PDPhase(long meltingPoint, long boilingPoint)
	{
		this(meltingPoint, boilingPoint, boilingPoint * 20, false);
	}
	public PDPhase(long meltingPoint, long boilingPoint, long plasmaPoint)
	{
		this(meltingPoint, boilingPoint, plasmaPoint, true);
	}
	public PDPhase(long meltingPoint, long boilingPoint, long plasmaPoint, boolean flag)
	{
		pM = meltingPoint;
		pB = boilingPoint;
		pP = plasmaPoint;
		enableP = true;
	}
	
	public PDPhase enableLiquidCrystal(long point)
	{
		enableLC = true;
		pLC = point;
		return this;
	}
	
	public PDPhase enableSuperfluid(long pointTemp, long pointPress)
	{
		enableSF = true;
		pSFp = pointPress;
		pSFt = pointTemp;
		return this;
	}
	
	public PDPhase enablPlasma()
	{
		return this;
	}
	
	public PDPhase enableMetal(long point)
	{
		enableEM = true;
		pEM = point;
		return this;
	}
	
	public PDPhase threePhasePoint(long pointTemp, long pointPress)
	{
		Tt = pointTemp;
		Tp = pointPress;
		return this;
	}
	
	public PDPhase maxPressEffcitive(long point)
	{
		pMPE = point;
		pE = pMPE * (1 - Math.log(pMPE));
		return this;
	}
	
	@Override
	public Phase getPhase(IPhaseCondition condition)
	{
		if(enableP && condition.getTemperature() >= pP)
		{
			return Phase.PLASMA;
		}
		if(enableSF && condition.getPress() >= pSFp && condition.getTemperature() >= pSFt)
		{
			return Phase.SUPERFLUID;
		}
		if(condition.getPress() <= Tp)
		{
			return condition.getTemperature() > Tt ? Phase.GAS : Phase.SOLID;
		}
		else if(Tp != STANDARD_PRESS)
		{
			//Press effect.
			double pe1 = (long) ((double) (condition.getPress() - Tp) / (double) (STANDARD_PRESS - Tp));
			long l1 = Tt + (long) ((double) (pB - Tt) * pe1);
			if(condition.getTemperature() >= l1) return Phase.GAS;
			/** 
			 * Use x^a*e^x for function to calculate press effect of liquid and solid phase.
			 */
			double pe2 = Math.pow(Math.E, pE + (pMPE * Math.log(condition.getPress() - Tp) - condition.getPress() + Tp));
			long l2 = Tt + (long) ((double) (pB - Tt) * pe2);
			if(condition.getTemperature() >= l2) return Phase.LIQUID;
			if(enableLC)
			{
				long l = Tt + (long) (((double) (pLC - Tt) * pe2));
				return l < pLC ? Phase.SOLID : Phase.MESOMORPHIC;
			}
			else
			{
				return Phase.SOLID;
			}
		}
		else
		{
			long l1 = pB - Tt;
			if(condition.getTemperature() >= l1) return Phase.GAS;
			long l2 = pB - Tt;
			if(condition.getTemperature() >= l2) return Phase.LIQUID;
			if(enableLC)
			{
				long l = Tt + (long) (((double) (pLC - Tt) / (double) (STANDARD_PRESS - Tp)) * (condition.getPress() - Tp));
				return l < pLC ? Phase.SOLID : Phase.MESOMORPHIC;
			}
			else
			{
				return Phase.SOLID;
			}
		}
	}
}