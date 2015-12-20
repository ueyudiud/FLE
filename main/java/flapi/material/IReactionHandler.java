package flapi.material;

import java.util.Map;
import java.util.Map.Entry;

import flapi.util.SizeUtil;
import flapi.util.SizeUtil.I;

public abstract class IReactionHandler
{
	protected IReactionHandler()
	{
		
	}
	
	public abstract boolean match(IChemCondition condition, Map<Matter, Integer> atomMap);
	
	public abstract void onInput(IChemCondition condition, Map<Matter, Integer> atomMap);
	
	public abstract void onOutput(IChemCondition condition, Map<Matter, Integer> atomMap);
	
	public abstract int getEnergyRequire(IChemCondition condition, Map<Matter, Integer> atomMap);
	
	protected I<Matter>[] make(Map<Matter, Integer> ms)
	{
		I<Matter>[] ret = new I[ms.size()];
		int i = 0;
		for(Entry<Matter, Integer> e : ms.entrySet()) ret[i++] = SizeUtil.info(e.getKey(), e.getValue());
		return ret;
	}
}