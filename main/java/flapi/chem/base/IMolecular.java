package flapi.chem.base;

import java.util.Map;

import flapi.chem.particle.Atoms;

public interface IMolecular
{	
	String getChemName();
	
	double getElementContain(Atoms e);

	Map<IMolecular, Integer> getIonContain(EnumCountLevel level);

	Map<Atoms, Integer> getElementAtoms();

	boolean isRadical();
	
	public enum EnumCountLevel
	{
		Atom,
		Ion,
		Matter;
	}
}