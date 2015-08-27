package fle.api.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fle.api.material.IAtoms;
import fle.api.util.ISubTagContainer;
import fle.api.util.SubTag;

public enum EnumAtoms implements IAtoms, ISubTagContainer
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
	public EnumAtoms[] getIonContain() 
	{
		return new EnumAtoms[]{this};
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
	public EnumAtoms[] getElementAtoms() 
	{
		return new EnumAtoms[]{this};
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
}