package fle.core.recipe;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fle.api.enums.EnumAtoms;
import fle.api.enums.EnumIons;
import fle.api.material.Electron;
import fle.api.material.IAtoms;
import fle.api.material.Matter;
import fle.api.material.MatterDictionary;
import fle.api.material.MatterReactionRegister;
import fle.api.material.MatterReactionRegister.ReactionHandler;
import fle.api.recipe.ItemBaseStack;
import fle.api.util.IChemCondition;
import fle.api.util.MolCaculator;
import fle.api.util.WeightHelper;
import fle.api.util.IChemCondition.EnumOxide;
import fle.api.util.IChemCondition.EnumPH;
import fle.api.util.WeightHelper.Stack;
import fle.core.init.IB;
import fle.core.init.Materials;
import fle.core.item.ItemFleSub;
import fle.core.item.ItemOre;
import fle.core.recipe.MatterPhaseChangeRecipe.MatterInfo;
import fle.core.recipe.matter.ReactionRecipe1;
import fle.core.recipe.matter.ReactionRecipe2;

public class MatterReactionRecipe implements ReactionHandler
{
	public static void init()
	{
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.NativeCopper)), EnumAtoms.Cu.asMatter(), MolCaculator.asMol(88, 1, 1) , (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 400000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Enargite)),     Matter.mCu3AsS4,         MolCaculator.asMol(88, 8, 1) , (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 400000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Cuprite)),      Matter.mCu2O,            MolCaculator.asMol(88, 3, 1) , (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 400000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Chalcocite)),   Matter.mCu2S,            MolCaculator.asMol(88, 3, 1) , (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 400000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Covellite)),    Matter.mCuS,             MolCaculator.asMol(88, 2, 1) , (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 400000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Tenorite)),     Matter.mCuO,             MolCaculator.asMol(88, 2, 1) , (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 400000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Orpiment)),     Matter.mAs2S3,           MolCaculator.asMol(88, 5, 1) , 681, 60000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Malachite)),    Matter.mCu_OH2_CO3,      MolCaculator.asMol(88, 10, 1), (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 400000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Azurite)),      Matter.mCu_OH2_2CO3,     MolCaculator.asMol(88, 14, 1), (int) Materials.NativeCopper.getPropertyInfo().getMeltingPoint(), 400000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Gelenite)),     Matter.mPbS,             MolCaculator.asMol(88, 2, 1) , (int) Materials.Lead.getPropertyInfo().getMeltingPoint(), 400000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Cassiterite)),  Matter.mSnO2,            MolCaculator.asMol(88, 3, 1) , (int) Materials.Tin.getPropertyInfo().getMeltingPoint(), 400000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemOre.a(Materials.Stannite)),     Matter.mCu2FeSnS4,       MolCaculator.asMol(88, 8, 1) , 718, 20000);
		for(EnumAtoms atom : EnumAtoms.values())
		{
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_" + atom.name().toLowerCase())),  atom.asMatter(), MolCaculator.asMol(75, 1, 1), atom.meltingPoint, atom.meltingPoint * 1000);
		}
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_cu_as_0")),     Materials.CuAs.getMatter(), MolCaculator.asMol(75, 1, 1), 684, 200000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_cu_as_1")),     Materials.CuAs2.getMatter(), MolCaculator.asMol(75, 1, 1), 573, 200000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_cu_pb_0")),     Materials.CuPb.getMatter(), MolCaculator.asMol(75, 1, 1), 671, 200000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_cu_pb_1")),     Materials.CuPb2.getMatter(), MolCaculator.asMol(75, 1, 1), 628, 200000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_cu_sn_0")),     Materials.CuSn.getMatter(), MolCaculator.asMol(75, 1, 1), 648, 200000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_cu_sn_1")),     Materials.CuSn2.getMatter(), MolCaculator.asMol(75, 1, 1), 629, 200000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("ingot_cu_pb_sn")),    Materials.CuSnPb.getMatter(), MolCaculator.asMol(75, 1, 1), 629, 200000);
		MatterDictionary.registerMatter(new ItemBaseStack(ItemFleSub.a("charred_log")),       EnumAtoms.C.asMatter(), MolCaculator.asMol(1000, 5, 1), 350, 6400);
		
		MatterDictionary.registerFluid(IB.copper,   EnumAtoms.Cu.asMatter());
		MatterDictionary.registerFluid(IB.lead,     EnumAtoms.Pb.asMatter());
		MatterDictionary.registerFluid(IB.zinc,     EnumAtoms.Zn.asMatter());
		MatterDictionary.registerFluid(IB.tin,      EnumAtoms.Sn.asMatter());
		MatterDictionary.registerFluid(IB.cu_as_0,  Materials.CuAs.getMatter());
		MatterDictionary.registerFluid(IB.cu_as_1,  Materials.CuAs2.getMatter());
		MatterDictionary.registerFluid(IB.cu_pb_0,  Materials.CuPb.getMatter());
		MatterDictionary.registerFluid(IB.cu_pb_1,  Materials.CuPb2.getMatter());
		MatterDictionary.registerFluid(IB.cu_sn_0,  Materials.CuSn.getMatter());
		MatterDictionary.registerFluid(IB.cu_sn_1,  Materials.CuSn2.getMatter());
		MatterDictionary.registerFluid(IB.cu_pb_sn, Materials.CuSnPb.getMatter());

		ReactionRecipe1.addRecipes();
		ReactionRecipe2.addRecipes();
		
		MatterPhaseChangeRecipe.register(new MatterInfo(Matter.mNH3, 185, 0.0048F, 0.0058F, new IAtoms[0]));
		MatterPhaseChangeRecipe.register(new MatterInfo(Matter.mHCl, 371, 0.0018F, 0.0088F, new IAtoms[0]));
		
		MatterReactionRegister.registerReactionHandler(
				new MatterSingleRecipe(Matter.mCu10Fe2Sb4S13, 500, 0.00018, 0.04128, 
						new Stack(EnumIons.Cu1.asMatter(), 10), 
						new Stack(EnumIons.Fe2.asMatter(), 2),
						new Stack(EnumIons.Sb3.asMatter(), 4),
						new Stack(EnumIons.S_2.asMatter(), 13)),
				new MatterSingleRecipe(Matter.mCu2FeSnS4, 500, 0.00018, 0.04128, 
						new Stack(Matter.mCu2S), 
						new Stack(Matter.mFeS),
						new Stack(Matter.mSnS2)),
				new MatterSingleRecipe(Matter.mCu5FeS4, 500, 0.00018, 0.04128, 
						new Stack(Matter.mCu2S, 2), 
						new Stack(EnumIons.Cu1.asMatter(), 5),
						new Stack(EnumIons.Fe3.asMatter()),
						new Stack(EnumIons.S_2.asMatter(), 2)),
				new MatterSingleRecipe(Matter.mCu3AsS4, 500, 0.00018, 0.04128, 
						new Stack(Matter.mCu2S), 
						new Stack(EnumIons.Cu1.asMatter(), 3),
						new Stack(EnumIons.As3.asMatter()),
						new Stack(EnumIons.S_2.asMatter(), 2)),
				new MatterSingleRecipe(Matter.mAs4S6, 514, 0.00012, 0.004176, 
						new Stack(Matter.mAs2S3, 2)),
				new MatterSingleRecipe(Matter.mAs2S3, 514, 0.00012, 0.004176, 
						new Stack(EnumIons.As3.asMatter(), 2),
						new Stack(EnumIons.S_2.asMatter(), 3)),
				new MatterReactionRecipe(EnumIons.O_2.asMatter(), EnumIons.H1.asMatter(), 15, EnumPH.MinPH, EnumPH.Super_Alkali, 0.091, 0.00032, 0.0004049, 
						new Stack(EnumIons.Hydroxide)),
				new MatterReactionRecipe(EnumIons.Hydroxide.asMatter(), EnumIons.H1.asMatter(), 48, EnumPH.MinPH, EnumPH.Weak_Alkali, 0.0814, 0.00032, 0.7004049, 
						new Stack(Matter.mH2O)),
				new MatterReactionRecipe(Matter.mH2O, EnumIons.H1.asMatter(), 91, EnumPH.MinPH, EnumPH.Weak_Acid, 0.0714, 0.00072, 0.7004049, 
						new Stack(EnumIons.Hydronium.asMatter())),						
				new MatterReactionRecipe(EnumIons.O_2.asMatter(), Matter.mH2O, 15, EnumPH.MinPH, EnumPH.Super_Alkali, 0.091, 0.00032, 0.0004049, 
						new Stack(EnumIons.Hydroxide, 2)),
				new MatterReactionRecipe(EnumIons.Hydroxide.asMatter(), EnumIons.Hydronium.asMatter(), 48, EnumPH.MinPH, EnumPH.Weak_Alkali, 0.0814, 0.00032, 0.0004049, 
						new Stack(Matter.mH2O, 2)),
				MatterPhaseChangeRecipe.instance);
	}
	
	private static final Random rand = new Random();

	private IAtoms a1;
	private IAtoms a2;
	private int tem;
	private int p1;
	private int p2;
	private int o1;
	private int o2;
	private int scale1 = 1;
	private int scale2 = 1;
	private double a;
	private double b;
	private double c;
	private double d;
	private Stack<IAtoms>[] output;

	public MatterReactionRecipe(IAtoms aA1, IAtoms aA2, int temNeed, double baseEffect, Stack<IAtoms>...stacks)
	{
		this(aA1, aA2, temNeed, EnumOxide.Highest, EnumOxide.Lowest, 0, 0, 0.0000392, baseEffect, stacks);
	}
	public MatterReactionRecipe(IAtoms aA1, IAtoms aA2, int temNeed, double tempretureEffect, double baseEffect, Stack<IAtoms>...stacks)
	{
		this(aA1, aA2, temNeed, EnumOxide.Highest, EnumOxide.Lowest, 0, 0, tempretureEffect, baseEffect, stacks);
	}
	public MatterReactionRecipe(IAtoms aA1, IAtoms aA2, int temNeed, EnumPH ph1, EnumPH ph2, double phEffect, double tempretureEffect, double baseEffect, Stack<IAtoms>...stacks)
	{
		this(aA1, aA2, temNeed, ph1, ph2, phEffect, 0, tempretureEffect, baseEffect, stacks);
	}
	public MatterReactionRecipe(IAtoms aA1, IAtoms aA2, int temNeed, EnumOxide o1, double oxideEffect, double tempretureEffect, double baseEffect, Stack<IAtoms>...stacks)
	{
		this(aA1, aA2, temNeed, o1, EnumOxide.Lowest, 0, oxideEffect, tempretureEffect, baseEffect, stacks);
	}
	public MatterReactionRecipe(IAtoms aA1, IAtoms aA2, int temNeed, EnumPH ph1, EnumPH ph2, double phEffect, double oxideEffect, double tempretureEffect, double baseEffect, Stack<IAtoms>...stacks)
	{
		this(aA1, aA2, temNeed, ph1, ph2, EnumOxide.Highest, EnumOxide.Lowest, phEffect, oxideEffect, tempretureEffect, baseEffect, stacks);
	}
	public MatterReactionRecipe(IAtoms aA1, IAtoms aA2, int temNeed, EnumOxide o1, EnumOxide o2, double phEffect, double oxideEffect, double tempretureEffect, double baseEffect, Stack<IAtoms>...stacks)
	{
		this(aA1, aA2, temNeed, EnumPH.MaxPH, EnumPH.MinPH, o1, o2, phEffect, oxideEffect, tempretureEffect, baseEffect, stacks);
	}
	/**
	 * 
	 * @param aA1
	 * @param aA2
	 * @param temNeed
	 * @param ph1
	 * @param ph2
	 * @param oa
	 * @param ob
	 * @param phEffect Higher value will cause ion active more usefully in alkali environment.
	 * @param oxideEffect Higher value will cause ion active more usefully in low oxide level environment.
	 * @param tempretureEffect Higher value will cause ion active more usefully in high temperature.
	 * @param baseEffect Higher value will cause ion active more usefully.
	 * @param stacks
	 */
	public MatterReactionRecipe(IAtoms aA1, IAtoms aA2, int temNeed, EnumPH ph1, EnumPH ph2, EnumOxide oa, EnumOxide ob, double phEffect, double oxideEffect, double tempretureEffect, double baseEffect, Stack<IAtoms>...stacks)
	{
		a1 = aA1;
		a2 = aA2;
		tem = temNeed;
		p1 = Math.min(ph1.ordinal(), ph2.ordinal());
		p2 = Math.max(ph1.ordinal(), ph2.ordinal());
		o1 = Math.min(oa.ordinal(), ob.ordinal());
		o2 = Math.max(oa.ordinal(), ob.ordinal());
		a = phEffect;
		b = oxideEffect;
		c = baseEffect;
		d = tempretureEffect;
		output = stacks;
	}
	
	public double getChance(IChemCondition condition)
	{
		return ((double) (condition.getPHLevel().ordinal() - p1) / (double) (p2 - p1)) * a + (condition.getTemperature() - tem) * d + 
				((double) (condition.getOxideLevel().ordinal() - o1) / (double) (o2 - o1)) * b + c;
	}
	
	@Override
	public boolean doesActive(IChemCondition condition, WeightHelper<IAtoms> helper)
	{
		return helper.contain(new Stack(a1, scale1)) && helper.contain(new Stack(a2, scale2))
				&& condition.getOxideLevel().ordinal() <= o2
				&& condition.getOxideLevel().ordinal() >= o1
				&& condition.getPHLevel().ordinal() >= p1
				&& condition.getPHLevel().ordinal() <= p2
				&& condition.getTemperature() >= tem;
	}
	
	public MatterReactionRecipe setIonScale(int a1, int a2)
	{
		scale1 = a1;
		scale2 = a2;
		return this;
	}

	@Override
	public void doReactionResult(IChemCondition condition,
			WeightHelper<IAtoms> helper)
	{
		Map<IAtoms, Integer> map = new HashMap<IAtoms, Integer>();
		int i1;
		int i2;
		int j = 0;
		int s1 = helper.getSize(a1);
		int s2 = helper.getSize(a2);
		i1 = i2 = Math.min(s1 / scale1, s2 / scale2);
		double ch = 1 - Math.pow(1 - getChance(condition), Math.sqrt((s1 * s1 + s2 * s2) / 2));
		for(int i = 0; i < i1; ++i)
			if(ch > rand.nextDouble())
				++j;
		i1 = i2 = j;
		i1 *= scale1;
		i2 *= scale2;
		if(j > 0)
		{
			helper.remove(a1, i1);
			helper.remove(a2, i2);
			helper.add(WeightHelper.multiply(output, j));
		}
	}
}