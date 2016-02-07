package fle.core.util.phase;

import farcore.substance.IPhaseCondition;
import farcore.substance.IPhaseSubstanceCondition;
import farcore.substance.Substance;
import fle.init.Substances;

public class PhaseCarbon1 implements IPhaseSubstanceCondition
{
	@Override
	public Substance getPhase(IPhaseCondition condition)
	{
		double p = Math.log10(condition.getPressure());
		double t = condition.getTemperature() / 1000;
		if(t >= 4)
		{
			double x = Math.log(t - 3) * 1.1;
			if(x + 6 < p)
				return Substances.graphite;
			else if(x + 9 < p)
				return Substances.carbonLiquid;
			else return Substances.carbonVapor;
		}
		if(p < 6)
		{
			return Substances.graphite;
		}
		else
		{
			if(t + 6 < p)
			{
				return Substances.diamond;
			}
			return Substances.graphite;
		}
	}
}