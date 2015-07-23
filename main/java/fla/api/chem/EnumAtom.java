package fla.api.chem;

public enum EnumAtom implements IAtom
{
	Nt("Neutron"),
	H("Hydrogen"),
	He("Helium"),
	Li("Lithium"),
	Be("Beryllium"),
	B("Boron"),
	C("Carbon"),
	N("Nitrogen"),
	O("Oxygen"),
	F("Fluorine"),
	Ne("Neon"),
	Na("Sodium"),
	Mg("Magnesium"),
	Al("Aluminium"),
	Si("Silicon"),
	P("Phosphorus"),
	S("Sulfur"),
	Cl("Chlorine"),
	Ar("Argon"),
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
	Ge("Germanium"),
	As("Arsenic"),
	Se("Selenium"),
	Br("Bromine"),
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
	Te("Tellurium"),
	I("Iodine"),
	Xe("Xenon"),
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
	At("Astatine"),
	Rn("Radon"),
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
	}
	
	private String name;
	
	EnumAtom(String aName) 
	{
		name = aName;
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
	public EnumAtom[] getIonContain() 
	{
		return new EnumAtom[]{this};
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
	
	boolean radition = false;
	
	private EnumAtom setRadition()
	{
		radition = true;
		return this;
	}
}