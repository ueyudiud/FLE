package fle.api.material;

import java.util.HashMap;
import java.util.Map;

import fle.api.util.IChemCondition;
import fle.api.util.SizeUtil;
import fle.api.util.SizeUtil.I;
import fle.api.util.WeightHelper;
import fle.api.util.WeightHelper.Stack;

public class ReactionHandlerStandard extends IReactionHandler
{
	private Stack<Matter>[] recipeMap;
	private Stack<Matter>[] recipeOutput;
	private int e;

	public ReactionHandlerStandard(Stack<Matter>[] output, int energyRequire, Stack<Matter>...stacks)
	{
		recipeOutput = output;
		recipeMap = stacks;
		e = energyRequire;
	}

	@Override
	public boolean match(IChemCondition condition, Map<Matter, Integer> atomMap)
	{
		return SizeUtil.match(recipeMap, atomMap);
	}

	@Override
	public void onInput(IChemCondition condition, Map<Matter, Integer> atomMap)
	{
		Stack<Matter>[] i = SizeUtil.matchInputs(recipeMap, make(atomMap));
		WeightHelper.remove(atomMap, i);
	}

	@Override
	public void onOutput(IChemCondition condition, Map<Matter, Integer> atomMap)
	{
		int i = SizeUtil.matchOutputs(recipeMap, make(atomMap));
		WeightHelper.add(atomMap, i, recipeOutput);
	}

	@Override
	public int getEnergyRequire(IChemCondition condition, Map<Matter, Integer> atomMap)
	{
		return SizeUtil.matchOutputs(recipeMap, make(atomMap)) * e;
	}
}