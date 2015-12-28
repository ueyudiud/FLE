package flapi.chem.base;

import flapi.collection.abs.Stack;

public class ScaleInput
{
	static final IChemistryRequire ANY = new IChemistryRequire()
	{	
		@Override
		public boolean match(IChemCondition condition)
		{
			return true;
		}

		@Override
		public float speed(IChemCondition condition)
		{
			return 1.0F;
		}
	};
	public IChemistryRequire req;
	public final Stack<Matter>[] input;
	
	public ScaleInput(Stack<Matter>...stacks)
	{
		this(ANY, stacks);
	}
	public ScaleInput(IChemistryRequire require, Stack<Matter>...stacks)
	{
		req = require;
		input = stacks;
	}
}