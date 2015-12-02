package fle.api.material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fle.api.util.IChemCondition;
import fle.api.util.WeightHelper;

public class MatterReactionRegister
{
	private static final List<IReactionHandler> handlerList = new ArrayList<IReactionHandler>();
	private static final List<ReactionHandler> list = new ArrayList();
	
	/**
	 * Out of date, will use new reaction handler.
	 * @param handlers
	 */
	@Deprecated
	public static void registerReactionHandler(ReactionHandler...handlers)
	{
		list.addAll(Arrays.asList(handlers));
	}
	
	public static void registerHandlers(IReactionHandler...handlers)
	{
		handlerList.addAll(Arrays.asList(handlers));
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
	
	public static IReactionHandler onMatch(IChemCondition condition, Map<Matter, Integer> map)
	{
		for(IReactionHandler handler : handlerList)
		{
			if(handler.match(condition, map))
			{
				handler.onInput(condition, map);
				return handler;
			}
		}
		return null;
	}
	
	public static void onOutput(IReactionHandler handler, IChemCondition condition, Map<Matter, Integer> map)
	{
		handler.onOutput(condition, map);
	}
	
	public static interface ReactionHandler
	{
		boolean doesActive(IChemCondition condition, WeightHelper<IAtoms> helper);
		
		void doReactionResult(IChemCondition condition, WeightHelper<IAtoms> helper);
	}
}