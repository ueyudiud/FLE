package flapi.material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import farcore.collection.abs.IStackList;
import farcore.collection.abs.Stack;
import flapi.chem.base.IChemCondition;
import flapi.chem.base.IMolecular;
import flapi.chem.base.Matter;

@Deprecated
public class MatterReactionRegister
{
	private static final List<IReactionHandler> handlerList = new ArrayList<IReactionHandler>();
	private static final List<ReactionHandler> list = new ArrayList();
	
	/**
	 * Out of date, will use new reaction handler.
	 * @param handlers
	 */
	public static void registerReactionHandler(ReactionHandler...handlers)
	{
		list.addAll(Arrays.asList(handlers));
	}
	
	public static void registerHandlers(IReactionHandler...handlers)
	{
		handlerList.addAll(Arrays.asList(handlers));
	}
	
	public static void getReactionResult(IChemCondition condition, IStackList<Stack<IMolecular>, IMolecular> helper)
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
		boolean doesActive(IChemCondition condition, IStackList<Stack<IMolecular>, IMolecular> helper);
		
		void doReactionResult(IChemCondition condition, IStackList<Stack<IMolecular>, IMolecular> helper);
	}
}