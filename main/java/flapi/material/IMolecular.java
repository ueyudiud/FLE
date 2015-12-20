package flapi.material;

import java.util.Map;

import flapi.enums.EnumAtoms;

public interface IMolecular
{	
	String getChemName();
	
	double getElementContain(EnumAtoms e);

	Map<IMolecular, Integer> getIonContain(EnumCountLevel level);

	Map<EnumAtoms, Integer> getElementAtoms();

	boolean isRadical();
	
	public enum EnumCountLevel
	{
		Atom,
		Ion,
		Matter;
	}
}