package fle.api.enums;

import static fle.api.util.SubTag.ATOM_gas;
import static fle.api.util.SubTag.ATOM_liquid;
import static fle.api.util.SubTag.ATOM_metal;
import static fle.api.util.SubTag.ATOM_nonmetal;
import static fle.api.util.SubTag.ATOM_soild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fle.api.material.IAtoms;
import fle.api.material.Matter;
import fle.api.material.Matter.AtomStack;
import fle.api.util.FleEntry;
import fle.api.util.ISubTagContainer;
import fle.api.util.SubTag;

public enum EnumAtoms implements IAtoms, ISubTagContainer
{
	Nt("Neutron"),
	H("Hydrogen", ATOM_nonmetal, ATOM_gas),
	He("Helium", ATOM_nonmetal, ATOM_gas),
	Li("Lithium"),
	Be("Beryllium"),
	B("Boron", ATOM_nonmetal),
	C("Carbon", ATOM_nonmetal),
	N("Nitrogen", ATOM_nonmetal, ATOM_gas),
	O("Oxygen", ATOM_nonmetal, ATOM_gas),
	F("Fluorine", ATOM_nonmetal, ATOM_gas),
	Ne("Neon", ATOM_nonmetal, ATOM_gas),
	Na("Sodium"),
	Mg("Magnesium"),
	Al("Aluminium"),
	Si("Silicon", ATOM_nonmetal),
	P("Phosphorus", ATOM_nonmetal),
	S("Sulfur", ATOM_nonmetal),
	Cl("Chlorine", ATOM_nonmetal, ATOM_gas),
	Ar("Argon", ATOM_nonmetal, ATOM_gas),
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
	Ge("Germanium", ATOM_nonmetal),
	As("Arsenic", ATOM_nonmetal),
	Se("Selenium", ATOM_nonmetal),
	Br("Bromine", ATOM_nonmetal, ATOM_liquid),
	Kr("Krypton", ATOM_gas),
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
	Te("Tellurium", ATOM_nonmetal),
	I("Iodine", ATOM_nonmetal),
	Xe("Xenon", ATOM_nonmetal, ATOM_gas),
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
	Hg("Mercury", ATOM_liquid),
	Tl("Thallium"),
	Pb("Lead"),
	Bi("Bismuth"),
	Po("Polonium"),
	At("Astatine", ATOM_nonmetal),
	Rn("Radon", ATOM_nonmetal, ATOM_gas),
	Fr("Francium"),
	Ra("Radium"),
	Ac("Actinium"),
	Th("Thorium"),
	Pa("Protactinium"),
	U("Uranium"),
	Np("Neptunium"),
	Pu("Plutonium"),
	Am("Americium"),
	Cm("Curium"),
	Bk("Berkelium"),
	Cf("Californium"),
	Es("Einsteinium"),
	Fm("Fermium"),
	Md("Mendelevium"),
	No("Nobelium"),
	Lr("Lawrencium"),
	Rf("Rutherfordium"),
	Db("Dubnium"),
	Sg("Seaborgium"),
	Bh("Bohrium"),
	Hs("Hassium"),
	Mt("Meitnerium"),
	Ds("Darmstadtium"),
	Rg("Roentgenium"),
	Cn("Copernicium"),
	Uut("Ununtrium"),
	Fl("Flerovium"),
	Uup("Ununpentium"),
	Lv("Livermorium"),
	Uus("Ununseptium"),
	Uuo("Ununoctium", ATOM_gas);
	
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
		Am.setRadition();
		Cm.setRadition();
		Bk.setRadition();
		Cf.setRadition();
		Es.setRadition();
		Fm.setRadition();
		Md.setRadition();
		No.setRadition();
		Lr.setRadition();
		Rf.setRadition();
		Db.setRadition();
		Sg.setRadition();
		Bh.setRadition();
		Hs.setRadition();
		Mt.setRadition();
		Ds.setRadition();
		Rg.setRadition();
		Cn.setRadition();
		Uut.setRadition();
		Fl.setRadition();
		Uup.setRadition();
		Lv.setRadition();
		Uus.setRadition();
		Uuo.setRadition();
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
		Am.setPoiont(1449, 2880);
		Cm.setPoiont(1613, 3383);
		Bk.setPoiont(1259, 2900);
		Cf.setPoiont(1173, 1743);
		Es.setPoiont(1133, 1269);
		Fm.setPoiont(1125, 3000);
		Md.setPoiont(1100, 3000);
		No.setPoiont(1100, 3000);
		Lr.setPoiont(1900, 3000);
		Rf.setPoiont(2400, 5800);
		Db.setPoiont(1000, 3000);
		Sg.setPoiont(1000, 3000);
		Bh.setPoiont(1000, 3000);
		Hs.setPoiont(1000, 3000);
		Mt.setPoiont(1000, 3000);
		Ds.setPoiont(1000, 3000);
		Rg.setPoiont(1000, 3000);
		Cn.setPoiont(1000, 3000);
		Uut.setPoiont(1000, 3000);
		Fl.setPoiont(1000, 3000);
		Uup.setPoiont(1000, 3000);
		Lv.setPoiont(1000, 3000);
		Uus.setPoiont(1000, 3000);
		Uuo.setPoiont(1000, 3000);
	}
	
	private String name;
	
	EnumAtoms(String aName, SubTag...tags) 
	{
		name = aName;
		add(tags);
		if(!contain(ATOM_nonmetal))
		{
			add(ATOM_metal);
		}
		if(!(contain(ATOM_gas) || contain(ATOM_liquid)))
		{
			add(ATOM_soild);
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
	
	public Matter asMatter()
	{
		return Matter.forMatter(name(), contain(ATOM_metal) ? CompoundType.Alloy : CompoundType.Molecular, new AtomStack(this));
	}
}