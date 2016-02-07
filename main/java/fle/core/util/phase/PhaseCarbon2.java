package fle.core.util.phase;

import farcore.substance.IPhaseCondition;
import farcore.substance.PhaseDiagram;
import farcore.substance.PhaseDiagram.Phase;
import farcore.substance.Substance;
import fle.init.Substances;

public class PhaseCarbon2 implements PhaseDiagram<IPhaseCondition, Phase>
{
	@Override
	public Phase getPhase(IPhaseCondition condition)
	{
		double p = Math.log10(condition.getPressure());
		double t = (double) condition.getTemperature() / 1000D;
		if(t >= 4)
		{
			double x = Math.log(t - 3) * 1.1;
			if(x + 6 < p)
				return Phase.SOLID;
			else if(x + 9 < p)
				return Phase.LIQUID;
			else return Phase.GAS;
		}
		return Phase.SOLID;
	}
}