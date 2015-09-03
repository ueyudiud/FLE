package fle.api.material;

import java.util.Map;

import fle.api.enums.EnumAtoms;

public interface IAtoms extends IStabilityInfo
{	
	String getChemicalFormulaName();
	
	double getElementContain(EnumAtoms e);

	Map<IAtoms, Integer> getIonContain(EnumCountLevel level);

	Map<EnumAtoms, Integer> getElementAtoms();

	boolean isRadical();
	
	public enum EnumCountLevel
	{
		Atom,
		Ion,
		Matter;
	}
}