package fle.api.material;

import fle.api.enums.EnumAtoms;

public interface IAtoms
{
	String getChemicalFormulaName();
	
	double getElementContain(EnumAtoms e);

	IAtoms[] getIonContain();

	EnumAtoms[] getElementAtoms();

	boolean isRadical();
}
