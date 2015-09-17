package fle.api.material;

import static fle.api.enums.CompoundType.Alloy;
import static fle.api.enums.CompoundType.Ionic;
import static fle.api.enums.CompoundType.Mix;
import static fle.api.enums.CompoundType.Molecular;
import static fle.api.enums.EnumAtoms.*;
import static fle.api.enums.EnumIons.*;

import java.util.HashMap;
import java.util.Map;

import fle.api.enums.CompoundType;
import fle.api.enums.EnumAtoms;
import fle.api.util.FleEntry;
import fle.api.util.IChemCondition;
import fle.api.util.Register;
import fle.api.util.WeightHelper;
import fle.api.util.WeightHelper.Stack;

public class Matter implements IAtoms
{	
	private static final Register<Matter> matter = new Register<Matter>();

	public static Matter forMatter(String name, CompoundType type, AtomStack...atomStacks)
	{
		if(matter.contain(name))
		{
			return getMatterFromName(name);
		}
		Matter matter = new Matter(atomStacks);
		matter.ct = type;
		return matter;
	}
	public static Matter getMatterFromName(String name)
	{
		return matter.get(name);
	}
	public static final Matter mNH3 = forMatter("NH3", Molecular, new AtomStack(N_3, 1), new AtomStack(H1, 3)).setMatterTemHelper(new MatterTemHelper(73, new Stack[0]));
	public static final Matter mH2O = forMatter("H2O", Molecular, new AtomStack(H1, 2), new AtomStack(O_2)).setMatterTemHelper(new MatterTemHelper(373, new Stack[0]));
	public static final Matter mCO = forMatter("CO", Molecular, new AtomStack(C2), new AtomStack(O_2));
	public static final Matter mCO2 = forMatter("CO2", Molecular, new AtomStack(C4), new AtomStack(O_2, 2)).setMatterTemHelper(new MatterTemHelper(219, new Stack[0]));
	public static final Matter mSO2 = forMatter("SO2", Molecular, new AtomStack(S4), new AtomStack(O_2, 2));
	public static final Matter mH2S = forMatter("H2S", Molecular, new AtomStack(H1, 2), new AtomStack(S_2));
	public static final Matter mHF = forMatter("HF", Molecular, new AtomStack(H1), new AtomStack(F_1));
	public static final Matter mHCl = forMatter("HCl", Molecular, new AtomStack(H1), new AtomStack(Cl_1));
	public static final Matter mHBr = forMatter("HBr", Molecular, new AtomStack(H1), new AtomStack(Br_1));
	public static final Matter mHI = forMatter("HI", Molecular, new AtomStack(H1), new AtomStack(I_1));
	
	public static final Matter mSiO2 = forMatter("SiO2", Molecular, new AtomStack(Si4, 1), new AtomStack(O_2, 2));
	
	public static final Matter mH2CO3 = forMatter("H2CO3", Molecular, new AtomStack(H1), new AtomStack(Hydrogen_Carbonate)).setMatterTemHelper(new MatterTemHelper(250, new Stack(mH2O), new Stack(mCO2)));
	public static final Matter mH3PO4 = forMatter("H3PO4", Molecular, new AtomStack(H1), new AtomStack(Dihydrogen_Phosphate));
	public static final Matter mH2SO3 = forMatter("H2SO3", Molecular, new AtomStack(H1), new AtomStack(Hydrogen_Sulfite));
	public static final Matter mH2SO4 = forMatter("H2SO4", Molecular, new AtomStack(H1, 2), new AtomStack(Sulfate));

	public static final Matter mN2 = forMatter("N2", Molecular, new AtomStack(N, 2));
	public static final Matter mO2 = forMatter("O2", Molecular, new AtomStack(O, 2));
	public static final Matter mAir = forMatter("MixAir", Mix, new AtomStack(mN2, 4), new AtomStack(mO2));
	
	public static final Matter mFeS = forMatter("FeS", Ionic, new AtomStack(Fe2, 1), new AtomStack(S_2, 1));
	
	public static final Matter mCuO = forMatter("CuO", Ionic, new AtomStack(Cu2, 1), new AtomStack(O_2, 1));
	public static final Matter mCuS = forMatter("CuS", Ionic, new AtomStack(Cu2, 1), new AtomStack(S_2, 1));
	public static final Matter mCu2O = forMatter("Cu2O", Ionic, new AtomStack(Cu1, 2), new AtomStack(O_2, 1));
	public static final Matter mCu2S = forMatter("Cu2S", Ionic, new AtomStack(Cu1, 2), new AtomStack(S_2, 1));
	public static final Matter mCuCO3 = forMatter("CuCO3", Ionic, new AtomStack(Cu2, 1), new AtomStack(Carbonate, 1)).setMatterTemHelper(new MatterTemHelper(650, new Stack(mCuO), new Stack(mCO2)));
	public static final Matter mCu_OH2 = forMatter("Cu(OH)2", Ionic, new AtomStack(Cu2, 1), new AtomStack(Hydroxide, 2)).setMatterTemHelper(new MatterTemHelper(500, new Stack(mCuO), new Stack(mH2O)));
	public static final Matter mCu_OH2_CO3 = forMatter("Cu(OH)2CO3", Ionic, new AtomStack(mCu_OH2), new AtomStack(mCuCO3)).setMatterTemHelper(new MatterTemHelper(500, 2, new Stack(mCu_OH2), new Stack(mCuCO3)));
	public static final Matter mCu_OH2_2CO3 = forMatter("Cu(OH)2(CuCO3)2", Ionic, new AtomStack(mCu_OH2), new AtomStack(mCuCO3, 2)).setMatterTemHelper(new MatterTemHelper(500, 3, new Stack(mCu_OH2), new Stack(mCuCO3, 2)));
	public static final Matter mSb2S3 = forMatter("Sb2S3", Ionic, new AtomStack(Sb3, 2), new AtomStack(S_2, 3));
	public static final Matter mAs2S3 = forMatter("As2S3", Molecular, new AtomStack(As3, 2), new AtomStack(S_2, 3));
	public static final Matter mAs4S4 = forMatter("As4S4", Molecular, new AtomStack(As2, 4), new AtomStack(S_2, 4));
	public static final Matter mAs4S6 = forMatter("As4S6", Molecular, new AtomStack(mAs2S3, 2));
	public static final Matter mNiAs = forMatter("NiAs", Molecular, new AtomStack(Ni2), new AtomStack(As_2));
	public static final Matter mFeAsS = forMatter("FeAsS", Ionic, new AtomStack(Fe3), new AtomStack(As_1), new AtomStack(S_2));
	public static final Matter mFeAsO4 = forMatter("FeAsO4", Ionic, new AtomStack(Fe3), new AtomStack(Arsenate));
	public static final Matter mFeAsO4_2H2O = forMatter("FeAsO4(H2O)2", Mix, new AtomStack(mFeAsO4), new AtomStack(mH2O, 2));
	public static final Matter mCo3_AsO4_2 = forMatter("Co3(AsO4)2", Ionic, new AtomStack(Co2, 3), new AtomStack(Arsenate, 2));
	public static final Matter mCo3_AsO4_2_8H2O = forMatter("Co3(AsO4)2(H2O)8", Mix, new AtomStack(mCo3_AsO4_2), new AtomStack(mH2O, 8));
	
	public static final Matter mCuFeS2 = forMatter("CuFeS2", Ionic, new AtomStack(Cu2), new AtomStack(Fe2), new AtomStack(S_2, 2));
	public static final Matter mCu3AsS4 = forMatter("Cu3AsS4", Ionic, new AtomStack(Cu1, 3), new AtomStack(As5), new AtomStack(S_2, 4));
	public static final Matter mCu5FeS4 = forMatter("Cu5FeS4", Ionic, new AtomStack(Cu1, 5), new AtomStack(Fe3), new AtomStack(S_2, 4));
	public static final Matter mCu10Fe2Sb4S13 = forMatter("Cu10Fe2Sb4S13", Ionic, new AtomStack(Cu1, 10), new AtomStack(Fe2, 2), new AtomStack(Sb3, 4), new AtomStack(S_2, 13));
	
	protected CompoundType ct;
	protected AtomStack[] ions;
	protected MatterTemHelper mh;

	public Matter(CompoundType aCt, AtomStack...aIons) 
	{
		ions = aIons;
		ct = aCt;
	}
	private Matter(AtomStack...aIons) 
	{
		ions = aIons;
		matter.register(this, getChemicalFormulaName());
	}
	
	protected Matter setMatterTemHelper(MatterTemHelper mth)
	{
		mh = mth;
		return this;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof IAtoms ? ((IAtoms) obj).getChemicalFormulaName().equals(getChemicalFormulaName()) : false;
	}
	
	@Override
	public String toString()
	{
		if(ions.length == 1)
		{
			return ions[0].toString();
		}
		String s = "";
		for(int i = 0; i < ions.length; ++i) 
		{
			s += ions[i].toString();
		}
		return s;
	}
	
	public static class AtomStack extends Stack<IAtoms>
	{
		public AtomStack(IAtoms aIon) 
		{
			this(aIon, 1);
		}
		public AtomStack(IAtoms aIon, int aSize) 
		{
			super(aIon, aSize);
		}
		
		public IAtoms get()
		{
			return super.getObj();
		}
		
		public int size()
		{
			return super.getSize();
		}
		
		@Override
		public String toString() 
		{
			return size() == 1 ? get().getChemicalFormulaName() : get().isRadical() ? "(" + get().getChemicalFormulaName() + ")" + String.valueOf(size()) : get().getChemicalFormulaName() + String.valueOf(size());
		}
		
		@Override
		public AtomStack copy()
		{
			return new AtomStack(getObj(), getSize());
		}
	}

	@Override
	public String getChemicalFormulaName() 
	{
		return toString();
	}

	@Override
	public Map<IAtoms, Integer> getIonContain(EnumCountLevel level) 
	{
		if(level != EnumCountLevel.Matter)
		{
			Map<IAtoms, Integer> ret = new HashMap();
			for(int i = 0; i < ions.length; ++i)
			{
				Map<IAtoms, Integer> map = ions[i].get().getIonContain(level);
				for(IAtoms ion : map.keySet())
				{
					WeightHelper.add(ret, new Stack(ion, ions[i].size() * map.get(ion)));
				}
			}
			return ret;
		}
		else
		{
			if(ct != CompoundType.Mix && ct != CompoundType.Alloy)
			{
				return FleEntry.asMap(new FleEntry(this, 1));
			}
			else
			{
				Map<IAtoms, Integer> ret = new HashMap();
				for(int i = 0; i < ions.length; ++i)
				{
					Map<IAtoms, Integer> map = ions[i].get().getIonContain(level);
					for(IAtoms ion : map.keySet())
					{
						WeightHelper.add(ret, new Stack(ion, ions[i].size() * map.get(ion)));
					}
				}
				return ret;
			}
		}
	}

	@Override
	public boolean isRadical()
	{
		return ions.length == 1 ? ions[0].get().isRadical() : true;
	}
	
	private boolean flag = false;
	
	private WeightHelper<EnumAtoms> wh;
	private WeightHelper<IAtoms> iwh1;
	private WeightHelper<IAtoms> iwh2;
	private WeightHelper<IAtoms> iwh3;
	
	private void initCaculater()
	{
		int i;
		Map<EnumAtoms, Integer> ea = new HashMap();
		Map<IAtoms, Integer> ea1 = new HashMap();
		Map<IAtoms, Integer> ea2 = new HashMap();
		Map<IAtoms, Integer> ea3 = new HashMap();
		int r = 0;
		for(i = 0; i < ions.length; ++i)
		{
			for(int j = 0; j < ions[i].size(); ++j)
			{
				WeightHelper.add(ea, ions[i].get().getElementAtoms());
				WeightHelper.add(ea1, ions[i].get().getIonContain(EnumCountLevel.Atom));
				WeightHelper.add(ea2, ions[i].get().getIonContain(EnumCountLevel.Ion));
				WeightHelper.add(ea3, ions[i].get().getIonContain(EnumCountLevel.Matter));
				++r;
			}
		}
		wh = new WeightHelper<EnumAtoms>(ea);
		iwh1 = new WeightHelper<IAtoms>(ea1);
		iwh2 = new WeightHelper<IAtoms>(ea2);
		iwh3 = new WeightHelper<IAtoms>(ea3);
		flag = true;
	}

	public WeightHelper<IAtoms> getIonHelper(EnumCountLevel level)
	{
		if(!flag) initCaculater();
		switch(level.ordinal())
		{
		case 0 : return iwh1;
		case 1 : return iwh2;
		case 2 : return iwh3;
		}
		return null;
	}

	public WeightHelper<EnumAtoms> getHelper()
	{
		if(!flag) initCaculater();
		return wh;
	}
	
	public double getIonContain(EnumCountLevel level, IAtoms e) 
	{
		if(!flag) initCaculater();
		switch(level.ordinal())
		{
		case 0 : return iwh1.getContain(e);
		case 1 : return iwh2.getContain(e);
		case 2 : return iwh3.getContain(e);
		}
		return 0F;
	}
	
	@Override
	public double getElementContain(EnumAtoms e) 
	{
		if(wh == null) initCaculater();
		return wh.getContain(e);
	}

	@Override
	public Map<EnumAtoms, Integer> getElementAtoms() 
	{
		Map<EnumAtoms, Integer> ret = new HashMap();
		for(int i = 0; i < ions.length; ++i)
		{
			Map<EnumAtoms, Integer> map = ions[i].get().getElementAtoms();
			for(EnumAtoms ion : map.keySet())
			{
				WeightHelper.add(ret, new Stack(ion, ions[i].size() * map.get(ion)));
			}
		}
		return ret;
	}

	@Override
	public Stack<IAtoms>[] getAtomsOutput(IChemCondition condition,
			Stack<IAtoms> input)
	{
		if(mh != null)
		{
			if(mh.match(condition))
			{
				return mh.getAtomsOutput(condition, input);
			}
		}
		if(ct == Ionic)
		{
			Map<IAtoms, Integer> ret = new HashMap();
			for(AtomStack stack : ions)
			{
				WeightHelper.add(ret, stack.get().getAtomsOutput(condition, new AtomStack(stack.get(), stack.getSize() * input.getSize() / getHelper().allWeight())));
			}
			return WeightHelper.asArray(ret);
		}
		else if(ct == Alloy || ct == Mix)
		{
			Map<IAtoms, Integer> ret = new HashMap();
			for(AtomStack stack : ions)
			{
				WeightHelper.add(ret, stack.get().getAtomsOutput(condition, new AtomStack(stack.get(), stack.getSize() * input.getSize() / getHelper().allWeight())));
			}
			return WeightHelper.asArray(ret);
		}
		else if(ct == Molecular)
		{
			Map<IAtoms, Integer> ret = new HashMap();
			for(AtomStack stack : ions)
			{
				WeightHelper.add(ret, stack.get().getAtomsOutput(condition, new AtomStack(stack.get(), stack.getSize() * input.getSize() / getHelper().allWeight())));
			}
			return WeightHelper.asArray(ret);
		}
		return new Stack[0];
	}
}