package fle.api.material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fle.api.util.IChemCondition;
import fle.api.util.WeightHelper;
import fle.api.util.WeightHelper.Stack;

public class MatterReactionRegister
{
	private static final List<ReactionHandler> list = new ArrayList();
	
	public static void registerReactionHandler(ReactionHandler...handlers)
	{
		list.addAll(Arrays.asList(handlers));
	}
	
	public static void getReactionResult(IChemCondition condition, WeightHelper helper)
	{
		for(ReactionHandler handler : list)
		{
			if(handler.doesActive(condition, helper))
			{
				handler.doReactionResult(condition, helper);
			}
		}
	}
	
	public static interface ReactionHandler
	{
		boolean doesActive(IChemCondition condition, WeightHelper<IAtoms> helper);
		
		void doReactionResult(IChemCondition condition, WeightHelper<IAtoms> helper);
	}
}