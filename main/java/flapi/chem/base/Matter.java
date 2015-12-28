package flapi.chem.base;

import static flapi.chem.particle.Atoms.H;
import static flapi.chem.particle.Atoms.N;
import static flapi.chem.particle.Atoms.O;
import static flapi.chem.particle.Ions.Arsenate;
import static flapi.chem.particle.Ions.As2;
import static flapi.chem.particle.Ions.As3;
import static flapi.chem.particle.Ions.As5;
import static flapi.chem.particle.Ions.As_1;
import static flapi.chem.particle.Ions.As_2;
import static flapi.chem.particle.Ions.Br_1;
import static flapi.chem.particle.Ions.C2;
import static flapi.chem.particle.Ions.C4;
import static flapi.chem.particle.Ions.Carbonate;
import static flapi.chem.particle.Ions.Cl_1;
import static flapi.chem.particle.Ions.Co2;
import static flapi.chem.particle.Ions.Cu1;
import static flapi.chem.particle.Ions.Cu2;
import static flapi.chem.particle.Ions.Dihydrogen_Phosphate;
import static flapi.chem.particle.Ions.F_1;
import static flapi.chem.particle.Ions.Fe2;
import static flapi.chem.particle.Ions.Fe3;
import static flapi.chem.particle.Ions.H1;
import static flapi.chem.particle.Ions.Hydrogen_Carbonate;
import static flapi.chem.particle.Ions.Hydrogen_Sulfite;
import static flapi.chem.particle.Ions.Hydroxide;
import static flapi.chem.particle.Ions.I_1;
import static flapi.chem.particle.Ions.N_3;
import static flapi.chem.particle.Ions.Ni2;
import static flapi.chem.particle.Ions.O_2;
import static flapi.chem.particle.Ions.Pb2;
import static flapi.chem.particle.Ions.S2;
import static flapi.chem.particle.Ions.S4;
import static flapi.chem.particle.Ions.S6;
import static flapi.chem.particle.Ions.S_2;
import static flapi.chem.particle.Ions.Sb3;
import static flapi.chem.particle.Ions.Si4;
import static flapi.chem.particle.Ions.Sn4;
import static flapi.chem.particle.Ions.Sulfate;
import static flapi.chem.particle.Ions.Zn2;
import static flapi.enums.CompoundType.Ionic;
import static flapi.enums.CompoundType.Mix;
import static flapi.enums.CompoundType.Molecular;

import java.util.HashMap;
import java.util.Map;

import flapi.chem.particle.Atoms;
import flapi.collection.ArrayStandardStackList;
import flapi.collection.CollectionUtil;
import flapi.collection.CollectionUtil.FleEntry;
import flapi.collection.Register;
import flapi.collection.abs.IStackList;
import flapi.collection.abs.Stack;
import flapi.enums.CompoundType;
import flapi.util.io.JsonInput;

public class Matter implements IMolecular
{	
	private static final Register<Matter> matter = new Register<Matter>();

	public static Matter forMatter(String name, CompoundType type, AtomStack...atomStacks)
	{
		if(matter.contain(getSaveName(name)))
		{
			return getMatterFromName(name);
		}
		try
		{
			return new Matter(name, type, atomStacks);
		}
		catch(Throwable e)
		{
			return null;
		}
	}
	public static Matter forMatter(Matter matter, AtomStack...atomStacks)
	{
		if(Matter.matter.contain(matter.getSaveName()))
		{
			return getMatterFromName(matter.toString());
		}
		try
		{
			return matter;
		}
		catch(Throwable e)
		{
			return null;
		}
	}
	public static Matter getMatterFromName(String name)
	{
		return matter.get(getSaveName(name));
	}
	
	public static final Matter mNH3 = forMatter("Ammonia", Molecular, new AtomStack(N_3, 1), new AtomStack(H1, 3)).setColor(0xA8A8FF);
	public static final Matter mH2O = forMatter("Water", Molecular, new AtomStack(H1, 2), new AtomStack(O_2)).setColor(0x51B3FF);
	public static final Matter mCO = forMatter("Carbon Monoxide", Molecular, new AtomStack(C2), new AtomStack(O_2)).setColor(0xEEEFE6);
	public static final Matter mCO2 = forMatter("Carbon Dioxide", Molecular, new AtomStack(C4), new AtomStack(O_2, 2)).setColor(0xDDFFF1);
	public static final Matter mSO2 = forMatter("Sulphur Dioxide", Molecular, new AtomStack(S4), new AtomStack(O_2, 2)).setColor(0xE3E8A9);
	public static final Matter mSO3 = forMatter("Sulfur Trioxide", Molecular, new AtomStack(S6), new AtomStack(O_2, 3)).setColor(0xE3E8A9);
	public static final Matter mH2S = forMatter("Hydrogen Sulfide", Molecular, new AtomStack(H1, 2), new AtomStack(S_2)).setColor(0xFFF200);
	public static final Matter mHF = forMatter("Hydrogen Fluoride", Molecular, new AtomStack(H1), new AtomStack(F_1)).setColor(0xF9FFB2);
	public static final Matter mHCl = forMatter("Hydrogen Chloride", Molecular, new AtomStack(H1), new AtomStack(Cl_1)).setColor(0xE4FFB2);
	public static final Matter mHBr = forMatter("Hydrogen Bromide", Molecular, new AtomStack(H1), new AtomStack(Br_1)).setColor(0xD3884E);
	public static final Matter mHI = forMatter("Hydrogen Iodide", Molecular, new AtomStack(H1), new AtomStack(I_1)).setColor(0xA342B2);
	
	public static final Matter mSiO2 = forMatter("Silica", Molecular, new AtomStack(Si4, 1), new AtomStack(O_2, 2)).setColor(0xAFAFAF);
	
	public static final Matter mH2CO3 = forMatter("Carbonic Acid", Molecular, new AtomStack(H1), new AtomStack(Hydrogen_Carbonate));
	public static final Matter mH3PO4 = forMatter("Phosphoric Acid", Molecular, new AtomStack(H1), new AtomStack(Dihydrogen_Phosphate));
	public static final Matter mH2SO3 = forMatter("Sulphurous Acid", Molecular, new AtomStack(H1), new AtomStack(Hydrogen_Sulfite));
	public static final Matter mH2SO4 = forMatter("Sulphuric Acid", Molecular, new AtomStack(H1, 2), new AtomStack(Sulfate));

	public static final Matter mN2 = forMatter("Nitrogen", Molecular, new AtomStack(N, 2)).setColor(0xEDFFFF);
	public static final Matter mO2 = forMatter("Oxigen", Molecular, new AtomStack(O, 2));
	public static final Matter mH2 = forMatter("Hydrogen", Molecular, new AtomStack(H, 2));
	public static final Matter mAir = forMatter("Air", Mix, new AtomStack(mN2, 4), new AtomStack(mO2));

	public static final Matter mSO = forMatter("Sulfur Monoxide", Molecular, new AtomStack(S2), new AtomStack(O_2));
	
	public static final Matter mCuO = forMatter("Cupric Oxide", Ionic, new AtomStack(Cu2, 1), new AtomStack(O_2, 1)).setColor(0x006EDD);
	public static final Matter mCuS = forMatter("Copper Sulfide", Ionic, new AtomStack(Cu2, 1), new AtomStack(S_2, 1)).setColor(0xE07300);
	public static final Matter mCu2O = forMatter("Cuprous Oxide", Ionic, new AtomStack(Cu1, 2), new AtomStack(O_2, 1)).setColor(0xE23400);
	public static final Matter mCu2S = forMatter("Cuprous Sulfide", Ionic, new AtomStack(Cu1, 2), new AtomStack(S_2, 1)).setColor(0xE5C18E);
	public static final Matter mCuCO3 = forMatter("Copper Carbonate", Ionic, new AtomStack(Cu2, 1), new AtomStack(Carbonate, 1));
	public static final Matter mCu_OH2 = forMatter("Cupric Hydroxide", Ionic, new AtomStack(Cu2, 1), new AtomStack(Hydroxide, 2)).setColor(0x0062E2);
	public static final Matter mCu_OH2_CO3 = forMatter("Verdigris", Ionic, new AtomStack(mCu_OH2), new AtomStack(mCuCO3)).setColor(0x097500);
	public static final Matter mCu_OH2_2CO3 = forMatter("Covellite", Ionic, new AtomStack(mCu_OH2), new AtomStack(mCuCO3, 2)).setColor(0x00288E);
	public static final Matter mSb2S3 = forMatter("Antimonous Trisulfide", Ionic, new AtomStack(Sb3, 2), new AtomStack(S_2, 3));
	public static final Matter mAs2S3 = forMatter("Arsenic Trisulfide", Molecular, new AtomStack(As3, 2), new AtomStack(S_2, 3)).setColor(0x6D483B);
	public static final Matter mAs4S4 = forMatter("Realgar", Molecular, new AtomStack(As2, 4), new AtomStack(S_2, 4));
	public static final Matter mAs4S6 = forMatter("Orpiment", Molecular, new AtomStack(mAs2S3, 2)).setColor(0x6D483B);
	public static final Matter mNiAs = forMatter("Nickel Arsenide", Molecular, new AtomStack(Ni2), new AtomStack(As_2));
	public static final Matter mFeO = forMatter("Ferrous Oxide", Ionic, new AtomStack(Fe2), new AtomStack(O));
	public static final Matter mFeS = forMatter("Ferrous Sulfide", Ionic, new AtomStack(Fe2, 1), new AtomStack(S_2, 1)).setColor(0xA0A300);
	public static final Matter mFe2O3 = forMatter("Ferric Oxide", Ionic, new AtomStack(Fe3, 2), new AtomStack(O, 3));
	public static final Matter mFe3O4 = forMatter("Ferriferrous Oxide", Ionic, new AtomStack(mFe2O3), new AtomStack(mFeO));
	public static final Matter mFeAsS = forMatter("Arsenopyrite", Ionic, new AtomStack(Fe3), new AtomStack(As_1), new AtomStack(S_2)).setColor(0x6B1112);
	public static final Matter mFeAsO4 = forMatter("Ferric Arsenate", Ionic, new AtomStack(Fe3), new AtomStack(Arsenate));
	public static final Matter mFeAsO4_2H2O = forMatter("Scorodite", Mix, new AtomStack(mFeAsO4), new AtomStack(mH2O, 2));
	public static final Matter mCo3_AsO4_2 = forMatter("Cobaltous Arsenate", Ionic, new AtomStack(Co2, 3), new AtomStack(Arsenate, 2)).setColor(0xDB90DB);
	public static final Matter mCo3_AsO4_2_8H2O = forMatter("Erythrite", Mix, new AtomStack(mCo3_AsO4_2), new AtomStack(mH2O, 8)).setColor(0xDD56DD);
	public static final Matter mPbO = forMatter("Lead Monoxide", Ionic, new AtomStack(Pb2), new AtomStack(O_2));
	public static final Matter mPbS = forMatter("Lead Monsulfide", Ionic, new AtomStack(Pb2), new AtomStack(S_2)).setColor(0xD6D4D4);
	public static final Matter mSnO2 = forMatter("Tin Dioxide", Ionic, new AtomStack(Sn4), new AtomStack(O_2, 2)).setColor(0xE8E8E8);
	public static final Matter mSnS2 = forMatter("Tin Disulfide", Ionic, new AtomStack(Sn4), new AtomStack(S_2, 2));
	public static final Matter mCu2FeSnS4 = forMatter("Stannite", Ionic, new AtomStack(mCu2S), new AtomStack(mSnS2), new AtomStack(mFeS));
	public static final Matter mZnS = forMatter("Zinc Sulfide", Ionic, new AtomStack(Zn2), new AtomStack(S_2)).setColor(0xE5E2C3);
	
	public static final Matter mCuFeS2 = forMatter("Chalcopyrite", Ionic, new AtomStack(Cu2), new AtomStack(Fe2), new AtomStack(S_2, 2)).setColor(0xB2AB6B);
	public static final Matter mCu3AsS4 = forMatter("Enargite", Ionic, new AtomStack(Cu1, 3), new AtomStack(As5), new AtomStack(S_2, 4));
	public static final Matter mCu5FeS4 = forMatter("Bornite", Ionic, new AtomStack(Cu1, 5), new AtomStack(Fe3), new AtomStack(S_2, 4)).setColor(0xB4AA6B);
	public static final Matter mCu10Fe2Sb4S13 = forMatter("Tetrahedrite", Ionic, new AtomStack(Cu1, 10), new AtomStack(Fe2, 2), new AtomStack(Sb3, 4), new AtomStack(S_2, 13));
	
	protected final String name;
	protected CompoundType ct;
	protected AtomStack[] ions;
	private int color = 0xFFFFFF;
	
	private Matter(String name, CompoundType aCt, AtomStack...aIons) 
	{
		this.name = name;
		ions = aIons;
		ct = aCt;
		matter.register(this, getSaveName());
	}
	private Matter(String name, AtomStack...aIons) 
	{
		this.name = name;
		ions = aIons;
		matter.register(this, getSaveName());
	}
	
	public String getName()
	{
		return name;
	}

	public String getSaveName()
	{
		return getSaveName(name);
	}
	static String getSaveName(String name)
	{
		return name.toLowerCase().replaceAll(" ", "_");
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
		return obj instanceof Matter ? getName().equals(((Matter) obj).getName()) : false;
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
	
	private IStackList<Stack<Atoms>, Atoms> wh;
	private IStackList<Stack<IMolecular>, IMolecular> iwh1, iwh2, iwh3;
	
	private void initCaculater()
	{
		int i;
		Map<Atoms, Integer> ea = new HashMap();
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
		wh = new ArrayStandardStackList<Atoms>(ea);
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

	public IStackList<Stack<Atoms>, Atoms> getHelper()
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
	public double getElementContain(Atoms e) 
	{
		if(wh == null) initCaculater();
		return wh.scale(e);
	}

	@Override
	public Map<Atoms, Integer> getElementAtoms() 
	{
		Map<Atoms, Integer> ret = new HashMap();
		for(int i = 0; i < ions.length; ++i)
		{
			Map<Atoms, Integer> map = ions[i].get().getElementAtoms();
			for(Atoms ion : map.keySet())
			{
				CollectionUtil.add(ret, new Stack(ion, ions[i].size() * map.get(ion)));
			}
		}
		return ret;
	}
}