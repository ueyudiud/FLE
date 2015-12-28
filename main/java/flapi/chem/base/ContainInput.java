package flapi.chem.base;

import static flapi.chem.base.ScaleInput.ANY;

import java.util.Map;
import java.util.Map.Entry;

import flapi.collection.CollectionUtil;

public class ContainInput
{
	public IChemistryRequire req;
	public final Map<Matter, int[]> input;
	
	public ContainInput(Entry<Matter, int[]>...stacks)
	{
		this(ANY, stacks);
	}
	public ContainInput(IChemistryRequire require, Entry<Matter, int[]>...stacks)
	{
		req = require;
		input = CollectionUtil.asMap(stacks);
	}
	
	public int[] getScaleRequire(Matter matter)
	{
		return input.containsKey(matter) ? input.get(matter) : new int[]{0, 0};
	}
}