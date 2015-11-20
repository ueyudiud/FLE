package fle.core.recipe;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fle.api.material.IAtoms;
import fle.api.material.Matter;
import fle.api.material.MatterReactionRegister.ReactionHandler;
import fle.api.util.IChemCondition;
import fle.api.util.IChemCondition.EnumEnviorment;
import fle.api.util.WeightHelper;
import fle.api.util.WeightHelper.Stack;

public class MatterPhaseChangeRecipe implements ReactionHandler
{
	private static final HashMap<IAtoms, MatterInfo> map = new HashMap<IAtoms, MatterPhaseChangeRecipe.MatterInfo>();
	private static final Random rand = new Random();
	
	public static final MatterPhaseChangeRecipe instance = new MatterPhaseChangeRecipe();
	
	private MatterPhaseChangeRecipe(){}
	
	public static void register(MatterInfo info)
	{
		map.put(info.i, info);
	}

	@Override
	public boolean doesActive(IChemCondition condition, WeightHelper helper)
	{
		return true;
	}

	@Override
	public void doReactionResult(IChemCondition condition,
			WeightHelper<IAtoms> helper)
	{
		MatterInfo info;
		Stack<IAtoms>[] stacks = helper.getList();
		for(Stack<IAtoms> stack : stacks)
		{
			if(stack.getObj() == null) continue;
			if(map.containsKey(stack.getObj()))
			{
				info = map.get(stack.getObj());
				if(info.can(condition))
				{
					double ch = info.chance(condition);
					int l = 0;
					for(int i = 0; i < stack.getSize(); ++i)
						if(ch > rand.nextDouble())
							++l;
					if(l > 0)
					{
						helper.remove(stack.getObj(), l);
						helper.add(WeightHelper.multiply(info.output().clone(), l));
					}
				}
				else info = null;
			}
		}
	}
	
	public static final class MatterInfo
	{
		private IAtoms i;
		private Stack<IAtoms>[] o;
		private int tem;
		private float bE;
		private float tE;
		private boolean requreEn = true;

		public MatterInfo(boolean flag, IAtoms input, int temp, float baseEffect, float temEffect, IAtoms...output)
		{
			this(input, temp, baseEffect, temEffect, output);
			requreEn = flag;
		}
		public MatterInfo(IAtoms input, int temp, float baseEffect, float temEffect, IAtoms...output)
		{
			i = input;
			Map<IAtoms, Integer> map = new HashMap();
			WeightHelper.add(map, output);
			o = WeightHelper.asArray(map);
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
		
		public IAtoms atom()
		{
			return i;
		}
		
		public Stack<IAtoms>[] output()
		{
			return o;
		}
	}
}