package flapi.material;

import java.util.HashMap;
import java.util.Map;

import flapi.collection.ArrayStandardStackList;
import flapi.collection.CollectionUtil;
import flapi.collection.CollectionUtil.FleEntry;
import flapi.collection.Register;
import flapi.collection.abs.IStackList;
import flapi.collection.abs.Stack;
import flapi.enums.CompoundType;
import flapi.enums.EnumAtoms;
import flapi.util.io.JsonInput;
import static flapi.enums.CompoundType.*;
import static flapi.enums.EnumAtoms.*;
import static flapi.enums.EnumIons.*;

public class Matter implements IMolecular
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
	public static Matter forMatter(Matter matter, AtomStack...atomStacks)
	{
		if(Matter.matter.contain(matter.toString()))
		{
			return getMatterFromName(matter.toString());
		}
		try
		{
			Matter m1 = (Matter) matter.clone();
			Matter.matter.register(matter, m1.toString());
			return m1;
		}
		catch(Throwable e)
		{
			return null;
		}
	}
	public static Matter getMatterFromName(String name)
	{
		return matter.get(name);
	}
	public static final Matter mNH3 = forMatter("NH3", Molecular, new AtomStack(N_3, 1), new AtomStack(H1, 3)).setColor(0xA8A8FF);
	public static final Matter mH2O = forMatter("H2O", Molecular, new AtomStack(H1, 2), new AtomStack(O_2)).setColor(0x51B3FF);
	public static final Matter mCO = forMatter("CO", Molecular, new AtomStack(C2), new AtomStack(O_2)).setColor(0xEEEFE6);
	public static final Matter mCO2 = forMatter("CO2", Molecular, new AtomStack(C4), new AtomStack(O_2, 2)).setColor(0xDDFFF1);
	public static final Matter mSO2 = forMatter("SO2", Molecular, new AtomStack(S4), new AtomStack(O_2, 2)).setColor(0xE3E8A9);
	public static final Matter mSO3 = forMatter("SO3", Molecular, new AtomStack(S6), new AtomStack(O_2, 3)).setColor(0xE3E8A9);
	public static final Matter mH2S = forMatter("H2S", Molecular, new AtomStack(H1, 2), new AtomStack(S_2)).setColor(0xFFF200);
	public static final Matter mHF = forMatter("HF", Molecular, new AtomStack(H1), new AtomStack(F_1)).setColor(0xF9FFB2);
	public static final Matter mHCl = forMatter("HCl", Molecular, new AtomStack(H1), new AtomStack(Cl_1)).setColor(0xE4FFB2);
	public static final Matter mHBr = forMatter("HBr", Molecular, new AtomStack(H1), new AtomStack(Br_1)).setColor(0xD3884E);
	public static final Matter mHI = forMatter("HI", Molecular, new AtomStack(H1), new AtomStack(I_1)).setColor(0xA342B2);
	
	public static final Matter mSiO2 = forMatter("SiO2", Molecular, new AtomStack(Si4, 1), new AtomStack(O_2, 2)).setColor(0xAFAFAF);
	
	public static final Matter mH2CO3 = forMatter("H2CO3", Molecular, new AtomStack(H1), new AtomStack(Hydrogen_Carbonate));
	public static final Matter mH3PO4 = forMatter("H3PO4", Molecular, new AtomStack(H1), new AtomStack(Dihydrogen_Phosphate));
	public static final Matter mH2SO3 = forMatter("H2SO3", Molecular, new AtomStack(H1), new AtomStack(Hydrogen_Sulfite));
	public static final Matter mH2SO4 = forMatter("H2SO4", Molecular, new AtomStack(H1, 2), new AtomStack(Sulfate));

	public static final Matter mN2 = forMatter("N2", Molecular, new AtomStack(N, 2)).setColor(0xEDFFFF);
	public static final Matter mO2 = forMatter("O2", Molecular, new AtomStack(O, 2));
	public static final Matter mH2 = forMatter("H2", Molecular, new AtomStack(H, 2));
	public static final Matter mAir = forMatter("MixAir", Mix, new AtomStack(mN2, 4), new AtomStack(mO2));

	public static final Matter mSO = forMatter("SO", Molecular, new AtomStack(S2), new AtomStack(O_2));
	
	
	public static final Matter mCuO = forMatter("CuO", Ionic, new AtomStack(Cu2, 1), new AtomStack(O_2, 1)).setColor(0x006EDD);
	public static final Matter mCuS = forMatter("CuS", Ionic, new AtomStack(Cu2, 1), new AtomStack(S_2, 1)).setColor(0xE07300);
	public static final Matter mCu2O = forMatter("Cu2O", Ionic, new AtomStack(Cu1, 2), new AtomStack(O_2, 1)).setColor(0xE23400);
	public static final Matter mCu2S = forMatter("Cu2S", Ionic, new AtomStack(Cu1, 2), new AtomStack(S_2, 1)).setColor(0xE5C18E);
	public static final Matter mCuCO3 = forMatter("CuCO3", Ionic, new AtomStack(Cu2, 1), new AtomStack(Carbonate, 1));
	public static final Matter mCu_OH2 = forMatter("Cu(OH)2", Ionic, new AtomStack(Cu2, 1), new AtomStack(Hydroxide, 2)).setColor(0x0062E2);
	public static final Matter mCu_OH2_CO3 = forMatter("Cu(OH)2CO3", Ionic, new AtomStack(mCu_OH2), new AtomStack(mCuCO3)).setColor(0x097500);
	public static final Matter mCu_OH2_2CO3 = forMatter("Cu(OH)2(CuCO3)2", Ionic, new AtomStack(mCu_OH2), new AtomStack(mCuCO3, 2)).setColor(0x00288E);
	public static final Matter mSb2S3 = forMatter("Sb2S3", Ionic, new AtomStack(Sb3, 2), new AtomStack(S_2, 3));
	public static final Matter mAs2S3 = forMatter("As2S3", Molecular, new AtomStack(As3, 2), new AtomStack(S_2, 3)).setColor(0x6D483B);
	public static final Matter mAs4S4 = forMatter("As4S4", Molecular, new AtomStack(As2, 4), new AtomStack(S_2, 4));
	public static final Matter mAs4S6 = forMatter("As4S6", Molecular, new AtomStack(mAs2S3, 2)).setColor(0x6D483B);
	public static final Matter mNiAs = forMatter("NiAs", Molecular, new AtomStack(Ni2), new AtomStack(As_2));
	public static final Matter mFeO = forMatter("FeO", Ionic, new AtomStack(Fe2), new AtomStack(O));
	public static final Matter mFeS = forMatter("FeS", Ionic, new AtomStack(Fe2, 1), new AtomStack(S_2, 1)).setColor(0xA0A300);
	public static final Matter mFe2O3 = forMatter("Fe2O3", Ionic, new AtomStack(Fe3, 2), new AtomStack(O, 3));
	public static final Matter mFe3O4 = forMatter("Fe2O3FeO", Ionic, new AtomStack(mFe2O3), new AtomStack(mFeO));
	public static final Matter mFeAsS = forMatter("FeAsS", Ionic, new AtomStack(Fe3), new AtomStack(As_1), new AtomStack(S_2)).setColor(0x6B1112);
	public static final Matter mFeAsO4 = forMatter("FeAsO4", Ionic, new AtomStack(Fe3), new AtomStack(Arsenate));
	public static final Matter mFeAsO4_2H2O = forMatter("FeAsO4(H2O)2", Mix, new AtomStack(mFeAsO4), new AtomStack(mH2O, 2));
	public static final Matter mCo3_AsO4_2 = forMatter("Co3(AsO4)2", Ionic, new AtomStack(Co2, 3), new AtomStack(Arsenate, 2)).setColor(0xDB90DB);
	public static final Matter mCo3_AsO4_2_8H2O = forMatter("Co3(AsO4)2(H2O)8", Mix, new AtomStack(mCo3_AsO4_2), new AtomStack(mH2O, 8)).setColor(0xDD56DD);
	public static final Matter mPbO = forMatter("PbO", Ionic, new AtomStack(Pb2), new AtomStack(O_2));
	public static final Matter mPbS = forMatter("PbS", Ionic, new AtomStack(Pb2), new AtomStack(S_2)).setColor(0xD6D4D4);
	public static final Matter mSnO2 = forMatter("SnO2", Ionic, new AtomStack(Sn4), new AtomStack(O_2, 2)).setColor(0xE8E8E8);
	public static final Matter mSnS2 = forMatter("SnS2", Ionic, new AtomStack(Sn4), new AtomStack(S_2, 2));
	public static final Matter mCu2FeSnS4 = forMatter("Cu2SSnS2FeS", Ionic, new AtomStack(mCu2S), new AtomStack(mSnS2), new AtomStack(mFeS));
	public static final Matter mZnS = forMatter("ZnS", Ionic, new AtomStack(Zn2), new AtomStack(S_2)).setColor(0xE5E2C3);
	
	public static final Matter mCuFeS2 = forMatter("CuFeS2", Ionic, new AtomStack(Cu2), new AtomStack(Fe2), new AtomStack(S_2, 2)).setColor(0xB2AB6B);
	public static final Matter mCu3AsS4 = forMatter("Cu3AsS4", Ionic, new AtomStack(Cu1, 3), new AtomStack(As5), new AtomStack(S_2, 4));
	public static final Matter mCu5FeS4 = forMatter("Cu5FeS4", Ionic, new AtomStack(Cu1, 5), new AtomStack(Fe3), new AtomStack(S_2, 4)).setColor(0xB4AA6B);
	public static final Matter mCu10Fe2Sb4S13 = forMatter("Cu10Fe2Sb4S13", Ionic, new AtomStack(Cu1, 10), new AtomStack(Fe2, 2), new AtomStack(Sb3, 4), new AtomStack(S_2, 13));
	
	protected CompoundType ct;
	protected AtomStack[] ions;
	private int color = 0xFFFFFF;
	
	public Matter(CompoundType aCt, AtomStack...aIons) 
	{
		ions = aIons;
		ct = aCt;
	}
	private Matter(AtomStack...aIons) 
	{
		ions = aIons;
		matter.register(this, getChemName());
	}
	
	public Matter setColor(int color)
	{
		this.color = color;
		return this;
	}
	
	public int getColor()
	{
		return color;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof IMolecular ? getChemName().equals(((IMolecular) obj).getChemName()) : false;
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
	
	public static class AtomStack extends Stack<IMolecular>
	{
		public AtomStack(IMolecular aIon) 
		{
			this(aIon, 1);
		}
		public AtomStack(IMolecular aIon, int aSize) 
		{
			super(aIon, aSize);
		}
		
		@Override
		public String toString() 
		{
			return size() == 1 ? get().getChemName() : get().isRadical() ? "(" + get().getChemName() + ")" + String.valueOf(size()) : get().getChemName() + String.valueOf(size());
		}
		
		@Override
		public AtomStack copy()
		{
			return new AtomStack(get(), size());
		}
		
		public static Stack<IMolecular>[] readMatterStackFromJson(JsonInput stream)
		{
			Stack<IMolecular>[] ret = new Stack[stream.size()];
			for(int i = 0; i < stream.size(); ++i)
			{
				JsonInput s1 = stream.sub(i);
				ret[i] = new Stack<IMolecular>(Matter.getMatterFromName(s1.getString("atoms", "null")), s1.getInteger("size", 1));
			}
			return ret;
		}
	}

	@Override
	public String getChemName() 
	{
		return toString();
	}

	@Override
	public Map<IMolecular, Integer> getIonContain(EnumCountLevel level) 
	{
		if(level != EnumCountLevel.Matter)
		{
			Map<IMolecular, Integer> ret = new HashMap();
			for(int i = 0; i < ions.length; ++i)
			{
				Map<IMolecular, Integer> map = ions[i].get().getIonContain(level);
				for(IMolecular ion : map.keySet())
				{
					CollectionUtil.add(ret, new Stack(ion, ions[i].size() * map.get(ion)));
				}
			}
			return ret;
		}
		else
		{
			if(ct != CompoundType.Mix && ct != CompoundType.Alloy)
			{
				return CollectionUtil.asMap(new FleEntry(this, 1));
			}
			else
			{
				Map<IMolecular, Integer> ret = new HashMap();
				for(int i = 0; i < ions.length; ++i)
				{
					Map<IMolecular, Integer> map = ions[i].get().getIonContain(level);
					for(IMolecular ion : map.keySet())
					{
						CollectionUtil.add(ret, new Stack(ion, ions[i].size() * map.get(ion)));
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
	
	private IStackList<Stack<EnumAtoms>, EnumAtoms> wh;
	private IStackList<Stack<IMolecular>, IMolecular> iwh1, iwh2, iwh3;
	
	private void initCaculater()
	{
		int i;
		Map<EnumAtoms, Integer> ea = new HashMap();
		Map<IMolecular, Integer> ea1 = new HashMap();
		Map<IMolecular, Integer> ea2 = new HashMap();
		Map<IMolecular, Integer> ea3 = new HashMap();
		for(i = 0; i < ions.length; ++i)
		{
			for(int j = 0; j < ions[i].size(); ++j)
			{
				CollectionUtil.add(ea, ions[i].get().getElementAtoms());
				CollectionUtil.add(ea1, ions[i].get().getIonContain(EnumCountLevel.Atom));
				CollectionUtil.add(ea2, ions[i].get().getIonContain(EnumCountLevel.Ion));
				CollectionUtil.add(ea3, ions[i].get().getIonContain(EnumCountLevel.Matter));
			}
		}
		wh = new ArrayStandardStackList<EnumAtoms>(ea);
		iwh1 = new ArrayStandardStackList<IMolecular>(ea1);
		iwh2 = new ArrayStandardStackList<IMolecular>(ea2);
		iwh3 = new ArrayStandardStackList<IMolecular>(ea3);
		flag = true;
	}

	public IStackList<Stack<IMolecular>, IMolecular> getIonHelper(EnumCountLevel level)
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

	public IStackList<Stack<EnumAtoms>, EnumAtoms> getHelper()
	{
		if(!flag) initCaculater();
		return wh;
	}
	
	public double getIonContain(EnumCountLevel level, IMolecular e) 
	{
		if(!flag) initCaculater();
		switch(level.ordinal())
		{
		case 0 : return iwh1.scale(e);
		case 1 : return iwh2.scale(e);
		case 2 : return iwh3.scale(e);
		}
		return 0F;
	}
	
	@Override
	public double getElementContain(EnumAtoms e) 
	{
		if(wh == null) initCaculater();
		return wh.scale(e);
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
				CollectionUtil.add(ret, new Stack(ion, ions[i].size() * map.get(ion)));
			}
		}
		return ret;
	}
}