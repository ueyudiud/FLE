package fle.core.util.phase;

import farcore.substance.IPhaseCondition;
import farcore.substance.IPhaseSubstanceCondition;
import farcore.substance.Substance;
import fle.init.Substances;

public class PhaseSulfur1 implements IPhaseSubstanceCondition
{
	boolean flag;
	
	public PhaseSulfur1(boolean flag)
	{
		this.flag = flag;
	}
	
	@Override
	public Substance getPhase(IPhaseCondition condition)
	{
		/**
		 * Information:
		 * α-S β-S g-S point : 368.46K 0.0001kPa
		 * β-S l-S g-S point : 392.15K 0.0004kPa
		 * α-S β-S l-S point : 426.85K 140000kPa
		 */
		double p = Math.log10(condition.getPressure());
		double t = (double) condition.getTemperature() / 100;
		if(t < 3.94)
		{
			return flag ? Substances.sulfur : Substances.rhombicSulfur;
		}
		else if(t > 5.26)
		{
			if(p / (t - 5.26) < 2.54)
			{
				return Substances.disulfur;
			}
		}
		if(t < 4.27)
		{
			double k = (p - 5.15461) / (t - 4.27);
			if(k > 15.66 && k < 24.62)
			{
				return flag ? Substances.sulfur : Substances.monoclinicSulfur;
			}
		}
		if(p / (t - 4.06) > 24.62)
		{
			return flag ? Substances.sulfur : Substances.rhombicSulfur;
		}
		else
		{
			return Substances.nacreousSulfur;
		}
	}
}