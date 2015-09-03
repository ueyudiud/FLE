package fle.api.material;

import java.util.HashMap;
import java.util.Map;

import fle.api.util.IChemCondition;
import fle.api.util.WeightHelper;
import fle.api.util.WeightHelper.Stack;

public class MatterTemHelper implements IStabilityInfo
{
	private int maxTem;
	private int size;
	private Stack<IAtoms>[] stacks;

	public MatterTemHelper(int aMaxTem, Stack<IAtoms>...aOutput)
	{
		this(aMaxTem, 1, aOutput);
	}
	public MatterTemHelper(int aMaxTem, int sizeRequire, Stack<IAtoms>...aOutput)
	{
		maxTem = aMaxTem;
		size = sizeRequire;
		stacks = aOutput;
	}
	
	public boolean match(IChemCondition condition)
	{
		return condition.getTemperature() > maxTem;
	}
	
	@Override
	public Stack<IAtoms>[] getAtomsOutput(IChemCondition condition,
			Stack<IAtoms> input)
	{
		Map<IAtoms, Integer> map = new HashMap();
		WeightHelper.add(map, (int) Math.floor(input.getSize() / size), stacks.clone());
		return WeightHelper.asArray(map);
	}
}