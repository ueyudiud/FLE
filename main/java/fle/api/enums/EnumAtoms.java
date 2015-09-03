package fle.api.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fle.api.material.IAtoms;
import fle.api.material.IStabilityInfo;
import fle.api.material.Matter;
import fle.api.material.Matter.AtomStack;
import fle.api.util.FleEntry;
import fle.api.util.IChemCondition;
import fle.api.util.ISubTagContainer;
import fle.api.util.SubTag;
import fle.api.util.WeightHelper.Stack;

public enum EnumAtoms implements IAtoms, ISubTagContainer, IStabilityInfo
{
	Nt("Neutron"),
	H("Hydrogen", SubTag.ATOM_nonmetal),
	He("Helium", SubTag.ATOM_nonmetal),
	Li("Lithium"),
	Be("Beryllium"),
	B("Boron", SubTag.ATOM_nonmetal),
	C("Carbon", SubTag.ATOM_nonmetal),
	N("Nitrogen", SubTag.ATOM_nonmetal),
	O("Oxygen", SubTag.ATOM_nonmetal),
	F("Fluorine", SubTag.ATOM_nonmetal),
	Ne("Neon", SubTag.ATOM_nonmetal),
	Na("Sodium"),
	Mg("Magnesium"),
	Al("Aluminium"),
	Si("Silicon", SubTag.ATOM_nonmetal),
	P("Phosphorus", SubTag.ATOM_nonmetal),
	S("Sulfur", SubTag.ATOM_nonmetal),
	Cl("Chlorine", SubTag.ATOM_nonmetal),
	Ar("Argon", SubTag.ATOM_nonmetal),
	K("Potassium"),
	Ca("Calcium"),
	Sc("Scandium"),
	Ti("Titanium"),
	V("Vanadium"),
	Cr("Chromium"),
	Mn("Manganese"),
	Fe("Iron"),
	Co("Cobalt"),
	Ni("Nickel"),
	Cu("Copper"),
	Zn("Zinc"),
	Ga("Gallium"),
	Ge("Germanium", SubTag.ATOM_nonmetal),
	As("Arsenic", SubTag.ATOM_nonmetal),
	Se("Selenium", SubTag.ATOM_nonmetal),
	Br("Bromine", SubTag.ATOM_nonmetal),
	Kr("Krypton"),
	Rb("Rubidium"),
	Sr("Strontium"),
	Y("Yttrium"),
	Zr("Zirconium"),
	Nb("Niobium"),
	Mo("Molybdenum"),
	Tc("Technetium"),
	Ru("Ruthenium"),
	Rh("Rhodium"),
	Pd("Palladium"),
	Ag("Silver"),
	Cd("Cadmium"),
	In("Indium"),
	Sn("Tin"),
	Sb("Antimony"),
	Te("Tellurium", SubTag.ATOM_nonmetal),
	I("Iodine", SubTag.ATOM_nonmetal),
	Xe("Xenon", SubTag.ATOM_nonmetal),
	Cs("Caesium"),
	Ba("Barium"),
	La("Lanthanum"),
	Ce("Cerium"),
	Pr("Praseodymium"),
	Nd("Neodymium"),
	Pm("Promethium"),
	Sm("Samarium"),
	Eu("Europium"),
	Gd("Gadolinium"),
	Tb("Terbium"),
	Dy("Dysprosium"),
	Ho("Holmium"),
	Er("Erbium"),
	Tm("Thulium"),
	Yb("Ytterbium"),
	Lu("Lutetium"),
	Hf("Hafnium"),
	Ta("Tantalum"),
	W("Tungsten"),
	Re("Rhenium"),
	Os("Osmium"),
	Ir("Iridium"),
	Pt("Platinum"),
	Au("Gold"),
	Hg("Mercury"),
	Tl("Thallium"),
	Pb("Lead"),
	Bi("Bismuth"),
	Po("Polonium"),
	At("Astatine", SubTag.ATOM_nonmetal),
	Rn("Radon", SubTag.ATOM_nonmetal),
	Fr("Francium"),
	Ra("Radium"),
	Ac("Actinium"),
	Th("Thorium"),
	Pa("Protactinium"),
	U("Uranium"),
	Np("Neptunium"),
	Pu("Plutonium");
	
	static
	{
		Tc.setRadition();
		Po.setRadition();
		At.setRadition();
		Rn.setRadition();
		Fr.setRadition();
		Ra.setRadition();
		Ac.setRadition();
		Th.setRadition();
		Pa.setRadition();
		U.setRadition();
		Np.setRadition();
		Pu.setRadition();
		H.setPoiont(14, 20);
		He.setPoiont(1, 4);
		Li.setPoiont(453, 1560);
		Be.setPoiont(1560, 2742);
		B.setPoiont(2349, 4200);
		C.setPoiont(3800, 4300);
		N.setPoiont(63, 77);
		O.setPoiont(54, 59);
		F.setPoiont(53, 85);
		Ne.setPoiont(24, 27);
		Na.setPoiont(370, 1156);
		Mg.setPoiont(923, 1363);
		Al.setPoiont(933, 2792);
		Si.setPoiont(1687, 3538);
		P.setPoiont(317, 550);
		S.setPoiont(388, 717);
		Cl.setPoiont(171, 239);
		Ar.setPoiont(83, 87);
		K.setPoiont(336, 1032);
		Ca.setPoiont(1115, 1757);
		Sc.setPoiont(1814, 3109);
		Ti.setPoiont(1941, 3560);
		V.setPoiont(2183, 3680);
		Cr.setPoiont(2180, 2944);
		Mn.setPoiont(1519, 2334);
		Fe.setPoiont(1811, 3134);
		Co.setPoiont(1768, 3200);
		Ni.setPoiont(1728, 3186);
		Cu.setPoiont(1357, 2835);
		Zn.setPoiont(692, 1180);
		Ga.setPoiont(302, 2477);
		Ge.setPoiont(1211, 3106);
		As.setPoiont(887, 1090);
		Se.setPoiont(453, 958);
		Br.setPoiont(265, 332);
		Kr.setPoiont(115, 119);
		Rb.setPoiont(312, 961);
		Sr.setPoiont(1050, 1655);
		Y.setPoiont(1799, 3609);
		Zr.setPoiont(2128, 4682);
		Nb.setPoiont(2750, 5017);
		Mo.setPoiont(2896, 4912);
		Tc.setPoiont(2430, 4538);
		Ru.setPoiont(2607, 4423);
		Rh.setPoiont(2237, 3968);
		Pd.setPoiont(1828, 3236);
		Ag.setPoiont(1234, 2435);
		Cd.setPoiont(594, 1040);
		In.setPoiont(429, 2345);
		Sn.setPoiont(505, 2875);
		Sb.setPoiont(903, 1860);
		Te.setPoiont(722, 1261);
		I.setPoiont(386, 457);
		Xe.setPoiont(161, 165);
		Cs.setPoiont(301, 944);
		Ba.setPoiont(1000, 2170);
		La.setPoiont(1193, 3737);
		Ce.setPoiont(1068, 3716);
		Pr.setPoiont(1208, 3793);
		Nd.setPoiont(1297, 3347);
		Pm.setPoiont(1315, 3273);
		Sm.setPoiont(1345, 2067);
		Eu.setPoiont(1099, 1802);
		Gd.setPoiont(1585, 3546);
		Tb.setPoiont(1629, 3503);
		Dy.setPoiont(1680, 2840);
		Ho.setPoiont(1734, 2993);
		Er.setPoiont(1802, 3141);
		Tm.setPoiont(1818, 2223);
		Yb.setPoiont(1097, 1469);
		Lu.setPoiont(1925, 3675);
		Hf.setPoiont(2506, 4876);
		Ta.setPoiont(3290, 5731);
		W.setPoiont(3695, 5828);
		Rh.setPoiont(3459, 5869);
		Os.setPoiont(3306, 5285);
		Ir.setPoiont(2719, 4701);
		Pt.setPoiont(2041, 4098);
		Au.setPoiont(1337, 3129);
		Hg.setPoiont(234, 629);
		Tl.setPoiont(577, 1746);
		Pb.setPoiont(600, 2022);
		Bi.setPoiont(544, 1837);
		Po.setPoiont(527, 1235);
		At.setPoiont(575, 610);
		Rn.setPoiont(202, 211);
		Fr.setPoiont(300, 950);
		Ra.setPoiont(973, 2010);
		Ac.setPoiont(1323, 3471);
		Th.setPoiont(2115, 5061);
		Pa.setPoiont(1841, 4300);
		U.setPoiont(1405, 4404);
		Np.setPoiont(917, 4273);
		Pu.setPoiont(912, 3501);
	}
	
	private String name;
	
	EnumAtoms(String aName, SubTag...tags) 
	{
		name = aName;
		add(tags);
		if(!contain(SubTag.ATOM_nonmetal))
		{
			add(SubTag.ATOM_metal);
		}
	}
	
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getChemicalFormulaName() 
	{
		return name();
	}

	@Override
	public Map<IAtoms, Integer> getIonContain(EnumCountLevel level) 
	{
		return FleEntry.asMap(new FleEntry(this, 1));
	}

	@Override
	public boolean isRadical() 
	{
		return false;
	}
	
	public boolean hasRadition() 
	{
		return radition;
	}

	@Override
	public double getElementContain(EnumAtoms e) 
	{
		return e == this ? 1D : 0D;
	}

	@Override
	public Map<EnumAtoms, Integer> getElementAtoms() 
	{
		return FleEntry.asMap(new FleEntry(this, 1));
	}
	
	boolean radition = false;
	
	private EnumAtoms setRadition()
	{
		radition = true;
		return this;
	}
	
	List<SubTag> tagList = new ArrayList();

	@Override
	public boolean contain(SubTag tag)
	{
		return tagList.contains(tag);
	}

	@Override
	public void add(SubTag... tag)
	{
		tagList.addAll(Arrays.asList(tag));
	}

	@Override
	public void remove(SubTag tag)
	{
		tagList.remove(tag);
	}

	public int boilingPoint = Integer.MAX_VALUE;
	public int meltingPoint = 0;

	void setPoiont(int m, int b)
	{
		boilingPoint = b;
		meltingPoint = m;
	}
	
	@Override
	public Stack<IAtoms>[] getAtomsOutput(IChemCondition condition,
			Stack<IAtoms> input)
	{
		Stack<IAtoms> i = input.copy();
		if(input.getObj() instanceof EnumAtoms)
		{
			i = new Stack<IAtoms>(((EnumAtoms) input.getObj()).asMatter(), input.getSize());
		}
		if(condition.getTemperature() > boilingPoint)
			if(condition.getTemperature() >= boilingPoint + 100)
				return new Stack[0];
			else
				i.setSize((int) ((float) i.getSize() * (double) (condition.getTemperature() - boilingPoint) / 100D));
		return new Stack[]{i};
	}

	public Matter asMatter()
	{
		return Matter.forMatter(name(), contain(SubTag.ATOM_metal) ? CompoundType.Alloy : CompoundType.Molecular, new AtomStack(this));
	}
}