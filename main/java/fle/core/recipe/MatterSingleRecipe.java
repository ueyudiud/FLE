package fle.core.recipe;

import java.util.Random;

import flapi.chem.base.IChemCondition;
import flapi.chem.base.IMolecular;
import flapi.chem.base.IChemCondition.EnumOxide;
import flapi.chem.base.IChemCondition.EnumPH;
import flapi.collection.CollectionUtil;
import flapi.collection.abs.IStackList;
import flapi.collection.abs.Stack;
import flapi.material.MatterReactionRegister.ReactionHandler;

@Deprecated
public class MatterSingleRecipe implements ReactionHandler
{
	private static final Random rand = new Random();

	public MatterSingleRecipe(IMolecular aA1, int temNeed, double baseEffect, Stack<IMolecular>...stacks)
	{
		this(aA1, temNeed, EnumOxide.Lowest, 0, 0, 0.0000392, baseEffect, stacks);
	}
	public MatterSingleRecipe(IMolecular aA1, int temNeed, double tempretureEffect, double baseEffect, Stack<IMolecular>...stacks)
	{
		this(aA1, temNeed, EnumOxide.Lowest, 0, 0, tempretureEffect, baseEffect, stacks);
	}
	public MatterSingleRecipe(IMolecular aA1, int temNeed, EnumPH ph1, EnumPH ph2, double phEffect, double tempretureEffect, double baseEffect, Stack<IMolecular>...stacks)
	{
		this(aA1, temNeed, ph1, ph2, phEffect, 0, tempretureEffect, baseEffect, stacks);
	}
	public MatterSingleRecipe(IMolecular aA1, int temNeed, EnumOxide o1, double oxideEffect, double tempretureEffect, double baseEffect, Stack<IMolecular>...stacks)
	{
		this(aA1, temNeed, o1, 0, oxideEffect, tempretureEffect, baseEffect, stacks);
	}
	public MatterSingleRecipe(IMolecular aA1, int temNeed, EnumPH ph1, EnumPH ph2, double phEffect, double oxideEffect, double tempretureEffect, double baseEffect, Stack<IMolecular>...stacks)
	{
		this(aA1, temNeed, ph1, ph2, EnumOxide.Lowest, EnumOxide.Highest, phEffect, oxideEffect, tempretureEffect, baseEffect, stacks);
	}
	public MatterSingleRecipe(IMolecular aA1, int temNeed, EnumOxide o1, double phEffect, double oxideEffect, double tempretureEffect, double baseEffect, Stack<IMolecular>...stacks)
	{
		this(aA1, temNeed, EnumPH.MaxPH, EnumPH.MinPH, o1, EnumOxide.Highest, phEffect, oxideEffect, tempretureEffect, baseEffect, stacks);
	}
	public MatterSingleRecipe(IMolecular aA1, int temNeed, EnumOxide o1, EnumOxide o2, double phEffect, double oxideEffect, double tempretureEffect, double baseEffect, Stack<IMolecular>...stacks)
	{
		this(aA1, temNeed, EnumPH.MaxPH, EnumPH.MinPH, o1, o2, phEffect, oxideEffect, tempretureEffect, baseEffect, stacks);
	}
	public MatterSingleRecipe(IMolecular aA1, int temNeed, EnumPH ph1, EnumPH ph2, EnumOxide o1, EnumOxide o2, double phEffect, double oxideEffect, double tempretureEffect, double baseEffect, Stack<IMolecular>...stacks)
	{
		a1 = aA1;
		tem = temNeed;
		p1 = Math.min(ph1.ordinal(), ph2.ordinal());
		p2 = Math.max(ph1.ordinal(), ph2.ordinal());
		o = Math.max(o1.ordinal(), o2.ordinal());
		l = Math.min(o1.ordinal(), o2.ordinal());
		a = phEffect;
		b = oxideEffect;
		c = baseEffect;
		d = tempretureEffect;
		output = stacks;
	}

	private IMolecular a1;
	private int tem;
	private int p1;
	private int p2;
	private int o;
	private int l;
	private double a;
	private double b;
	private double c;
	private double d;
	private Stack<IMolecular>[] output;

	public double getChance(IChemCondition condition)
	{
		return ((double) (condition.getPHLevel().ordinal() - p1) / (double) (p2 - p1)) * a + (condition.getTemperature() - tem) * d + 
				((double) (condition.getOxideLevel().ordinal() - l) / (double) (o - l)) * b + c;
	}
	
	@Override
	public boolean doesActive(IChemCondition condition, IStackList<Stack<IMolecular>, IMolecular> helper)
	{
		return helper.contain(a1)
				&& condition.getOxideLevel().ordinal() <= o
				&& condition.getPHLevel().ordinal() >= p1
				&& condition.getPHLevel().ordinal() <= p2
				&& condition.getTemperature() >= tem;
	}
	
	public void setScale(int scale)
	{
	}

	@Override
	public void doReactionResult(IChemCondition condition, IStackList<Stack<IMolecular>, IMolecular> helper)
	{
		int size = helper.weight(a1);
		double ch = getChance(condition);
		int l = 0;
		for(int i = 0; i < size; ++i)
			if(rand.nextDouble() < ch) 
				++l;
		helper.removeAll(new Stack(a1, l));
		helper.addAll(CollectionUtil.multiply(output, l));
	}
}