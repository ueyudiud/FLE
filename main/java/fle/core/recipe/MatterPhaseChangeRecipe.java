package fle.core.recipe;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import flapi.chem.base.IChemCondition;
import flapi.chem.base.IMolecular;
import flapi.collection.CollectionUtil;
import flapi.collection.abs.IStackList;
import flapi.collection.abs.Stack;
import flapi.material.MatterReactionRegister.ReactionHandler;

@Deprecated
public class MatterPhaseChangeRecipe implements ReactionHandler
{
	private static final HashMap<IMolecular, MatterInfo> map = new HashMap<IMolecular, MatterPhaseChangeRecipe.MatterInfo>();
	private static final Random rand = new Random();
	
	public static final MatterPhaseChangeRecipe instance = new MatterPhaseChangeRecipe();
	
	private MatterPhaseChangeRecipe(){}
	
	public static void register(MatterInfo info)
	{
		map.put(info.i, info);
	}

	@Override
	public boolean doesActive(IChemCondition condition, IStackList<Stack<IMolecular>, IMolecular> helper)
	{
		return true;
	}

	@Override
	public void doReactionResult(IChemCondition condition,
			IStackList<Stack<IMolecular>, IMolecular> helper)
	{
		MatterInfo info;
		Stack<IMolecular>[] stacks = helper.toArray();
		for(Stack<IMolecular> stack : stacks)
		{
			if(stack.get() == null) continue;
			if(map.containsKey(stack.get()))
			{
				info = map.get(stack.get());
				if(info.can(condition))
				{
					double ch = info.chance(condition);
					int l = 0;
					for(int i = 0; i < stack.size(); ++i)
						if(ch > rand.nextDouble())
							++l;
					if(l > 0)
					{
						helper.removeAll(new Stack(stack.get(), l));
						helper.addAll(CollectionUtil.multiply(info.output().clone(), l));
					}
				}
				else info = null;
			}
		}
	}
	
	public static final class MatterInfo
	{
		private IMolecular i;
		private Stack<IMolecular>[] o;
		private int tem;
		private float bE;
		private float tE;
		private boolean requreEn = true;

		public MatterInfo(boolean flag, IMolecular input, int temp, float baseEffect, float temEffect, IMolecular...output)
		{
			this(input, temp, baseEffect, temEffect, output);
			requreEn = flag;
		}
		public MatterInfo(boolean flag, IMolecular input, int temp, float baseEffect, float temEffect, Stack<IMolecular>...output)
		{
			this(input, temp, baseEffect, temEffect, output);
			requreEn = flag;
		}
		public MatterInfo(IMolecular input, int temp, float baseEffect, float temEffect, IMolecular...output)
		{
			i = input;
			Map<IMolecular, Integer> map = new HashMap();
			CollectionUtil.add(map, output);
			o = CollectionUtil.asArray(map);
			tem = temp;
			bE = baseEffect;
			tE = temEffect;
		}
		public MatterInfo(IMolecular input, int temp, float baseEffect, float temEffect, Stack<IMolecular>...output)
		{
			i = input;
			Map<IMolecular, Integer> map = new HashMap();
			CollectionUtil.add(map, output);
			o = CollectionUtil.asArray(map);
			tem = temp;
			bE = baseEffect;
			tE = temEffect;
		}
		
		public boolean can(IChemCondition condition)
		{
			return condition.getTemperature() > tem;
		}
		
		public double chance(IChemCondition condition)
		{
			return bE + tE * (condition.getTemperature() - tem) * (condition.getTemperature() - tem);
		}
		
		public IMolecular atom()
		{
			return i;
		}
		
		public Stack<IMolecular>[] output()
		{
			return o;
		}
		
		public boolean getRequrieEnergy()
		{
			return requreEn;
		}
	}
}