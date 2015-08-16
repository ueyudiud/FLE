package fle.api.enums;

import fle.api.material.IAtoms;
import fle.api.util.WeightHelper;

public enum EnumIons implements IAtoms
{
	H_1("H", -1),
	H1("H", +1),
	Li1("Li", +1),
	Be2("Be", +2),
	B3("B", +3),
	C2("C", +2),
	C4("C", +4),
	N_3("N", -3),
	N3("N", +3),
	N5("N", +5),
	O_1("O", -1),
	O_2("O", -2),
	F_1("F", -1),
	Na1("Na", +1),
	Mg2("Mg", +2),
	Al3("Al", +3),
	Si4("Si", +4),
	P3("P", +3),
	P5("P", +5),
	S_2("S", -2),
	S("S", 0),
	S4("S", +4),
	S6("S", +6),
	Cl_1("Cl", -1),
	Cl1("Cl", +1),
	Cl5("Cl", +5),
	K1("K", +1),
	Ca2("Ca", +2),
	Sc3("Sc", +3),
	Ti2("Ti", +2),
	Ti4("Ti", +4),
	V5("V", +5),
	Cr2("Cr", +2),
	Cr3("Cr", +3),
	Cr4("Cr", +4),
	Cr6("Cr", +6),
	Mn2("Mn", +2),
	Mn4("Mn", +4),
	Mn6("Mn", +6),
	Mn7("Mn", +7),
	Fe2("Fe", +2),
	Fe3("Fe", +3),
	Co2("Co", +2),
	Co4("Co", +4),
	Ni2("Ni", +2),
	Ni4("Ni", +4),
	Cu1("Cu", +1),
	Cu2("Cu", +2),
	Zn2("Zn", +2),
	Ga3("Ga", +3),
	Ge2("Ge", +2),
	Ge4("Ge", +4),
	As3("As", +3),
	As5("As", +5),
	Se_2("Se", -2),
	Se4("Se", +4),
	Br_1("Br", -1),
	Br3("Br", +3),
	Rb1("Rb", +1),
	Y3("Y", +3),
	Nb3("Nb", +3),
	Nb4("Nb", +4),
	Nb5("Nb", +5),
	Mo3("Mo", +3),
	Mo4("Mo", +4),
	Mo5("Mo", +5),
	Tc4("Tc", +4),
	Pd1("Pd", +1),
	Pd2("Pd", +2),
	Ag1("Ag", +1),
	Cd2("Cd", +2),
	In3("In", +3),
	Sn2("Sn", +2),
	Sn4("Sn", +4),
	Sb3("Sb", +3),
	Sb5("Sb", +5),
	I_1("I", -1),
	I5("I", +5),
	Ba2("Ba", +2),
	Nd2("Nd", +2),
	Nd3("Nd", +3),
	W4("W", +4),
	W6("W", +6),
	Os4("Os", +4),
	Os6("Os", +6),
	Os7("Os", +7),
	Os8("Os", +8),
	Ir3("Ir", +3),
	Ir4("Ir", +4),
	Ir5("Ir", +5),
	Pt2("Pt", +2),
	Pt4("Pt", +4),
	Au3("Au", +3),
	Hg1("Hg", +1),
	Hg2("Hg", +2),
	Pb2("Pb", +2),
	Pb4("Pb", +4),
	Bi3("Bi", +3),
	Bi5("Bi", +5),
	Th4("Th", +4),
	U3("U", +3),
	U5("U", +5),
	Pu3("Pu", +3),
	Ammonium("NH4", -2, true, new EnumIons[]{N_3, H1, H1, H1, H1}),
	Carbonate("CO3", -2, true, new EnumIons[]{C4, O_2, O_2, O_2}),
	Peroxide("O2", -2, true, new EnumIons[]{O_1, O_1}),
	Superoxide("O2", -1, true, new EnumIons[]{O_1, O_1}),
	Hypochlorite("ClO", -1, true, new EnumIons[]{Cl1, O_2}),
	Chlorate("ClO3", -1, true, new EnumIons[]{Cl5, O_2, O_2, O_2}),
	Perchlorate("ClO4", -1, true, new EnumIons[]{Cl5, O_2, O_2, O_2, O_2}),
	Chromate("CrO4", -2, true, new EnumIons[]{Cr6, O_2, O_2, O_2, O_2}),
	Dichromate("Cr2O7", -2, true, new EnumIons[]{Cr6, Cr6, O_2, O_2, O_2, O_2, O_2, O_2, O_2}),
	Permanganate("MnO4", -1, true, new EnumIons[]{Mn7, O_2, O_2, O_2, O_2}),
	Dihydrogen_Phosphate("H2PO4", -1, true, new EnumIons[]{H1, H1, P5, O_2, O_2, O_2, O_2}),
	Monohydrogen_Phosphate("HPO4", -2, true, new EnumIons[]{H1, P5, O_2, O_2, O_2, O_2}),
	Phosphate("PO4", -3, true, new EnumIons[]{P5, O_2, O_2, O_2, O_2}),
	Hydrogen_Carbonate("HCO3", -1, true, new EnumIons[]{H1, C4, O_2, O_2, O_2}),
	Nitrate("NO3", -1, true, new EnumIons[]{N5, O_2, O_2, O_2}),
	Nitrite("NO2", -1, true, new EnumIons[]{N3, O_2, O_2}),
	Hydrogen_Sulfite("HSO3", -1, true, new EnumIons[]{H1, S4, O_2, O_2, O_2}),
	Sulfite("SO3", -1, true, new EnumIons[]{S4, O_2, O_2, O_2}),
	Thiosulfate("S2O3", -2, true, new EnumIons[]{S4, S, O_2, O_2, O_2}),
	Sulfate("SO4", -2, true, new EnumIons[]{S4, O_2, O_2, O_2, O_2}),
	Silicate("SiO4", -4, true, new EnumIons[]{Si4, O_2, O_2, O_2, O_2}),
	Metasilicate("SiO3", -2, true, new EnumIons[]{Si4, O_2, O_2, O_2}),
	Aluminium_Silicate("AlSiO4", -1, true, new EnumIons[]{Al3, Si4, O_2, O_2, O_2, O_2}),
	Hydroxide("OH", -1, true, new EnumIons[]{H1, O_2});
	
	final String str1;
	boolean isRadical = false;
	int charge;
	EnumIons[] containIon;
	EnumAtoms[] containAtom;

	EnumIons(String aCfName, int aCharge, boolean aRadical, EnumIons[] aContainIon) 
	{
		isRadical = aRadical;
		str1 = aCfName;
		charge = aCharge;
		containIon = aContainIon;
		containAtom = new EnumAtoms[containIon.length];
		if(aRadical)
		{
			for(int i = 0; i < containIon.length; ++i)
			{
				containAtom[i] = containIon[i].getElementContain();
			}
		}
		else
		{
			containAtom = new EnumAtoms[]{EnumAtoms.valueOf(aCfName)};
		}
	}
	
	EnumAtoms getElementContain() 
	{
		return !isRadical ? EnumAtoms.valueOf(str1) : null;
	}
	
	EnumIons(String aCfName, int aCharge) 
	{
		str1 = aCfName;
		charge = aCharge;
		containAtom = new EnumAtoms[]{EnumAtoms.valueOf(aCfName)};
	}

	@Override
	public String getChemicalFormulaName()
	{
		return str1;
	}

	@Override
	public IAtoms[] getIonContain()
	{
		return isRadical ? containIon.clone() : new IAtoms[]{this};
	}
	
	@Override
	public boolean isRadical() 
	{
		return isRadical;
	}
	
	@Override
	public double getElementContain(EnumAtoms e) 
	{
		return new WeightHelper<EnumAtoms>(containAtom).getContain(e);
	}

	@Override
	public EnumAtoms[] getElementAtoms() 
	{
		return containAtom.clone();
	}
}