package farcore.substance;

import java.util.EnumMap;

public class PhaseSubstanceCondition implements IPhaseSubstanceCondition
{
	private final Substance substance;
	public final EnumMap<Phase, Substance> map = new EnumMap<>(Phase.class);
	
	public PhaseSubstanceCondition(Substance base)
	{
		this.substance = base;
	}
	
	@Override
	public Substance getPhase(IPhaseCondition condition)
	{
		return map.containsKey(substance.getPhase(condition)) ?
				map.get(substance.getPhase(condition)) : substance;
	}
}