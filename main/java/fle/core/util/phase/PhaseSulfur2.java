package fle.core.util.phase;

import farcore.substance.IPhaseCondition;
import farcore.substance.PhaseDiagram;
import farcore.substance.PhaseDiagram.Phase;
import fle.init.Substances;

public class PhaseSulfur2 implements PhaseDiagram<IPhaseCondition, Phase>
{
	@Override
	public Phase getPhase(IPhaseCondition condition)
	{
		double p = Math.log10(condition.getPressure());
		double t = (double) condition.getTemperature() / 100;
		if(t < 3.94)
		{
			return Phase.SOLID;
		}
		else if(t > 5.26)
		{
			if(p / (t - 5.26) < 2.54)
			{
				return Phase.GAS;
			}
		}
		if(t < 4.27)
		{
			double k = (p - 5.15461) / (t - 4.27);
			if(k > 15.66 && k < 24.62)
			{
				return Phase.SOLID;
			}
		}
		if(p / (t - 4.06) > 24.62)
		{
			return Phase.SOLID;
		}
		else
		{
			return Phase.LIQUID;
		}
	}
}