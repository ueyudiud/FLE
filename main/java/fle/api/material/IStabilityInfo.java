package fle.api.material;

import fle.api.util.IChemCondition;
import fle.api.util.WeightHelper.Stack;

public interface IStabilityInfo
{
	Stack<IAtoms>[] getAtomsOutput(IChemCondition condition, Stack<IAtoms> input);
}