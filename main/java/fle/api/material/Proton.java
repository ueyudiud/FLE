package fle.api.material;

import java.util.Map;

import fle.api.enums.EnumAtoms;
import fle.api.enums.EnumIons;
import fle.api.util.FleEntry;

public class Proton extends ElementaryParticle
{
	public Proton p = new Proton();

	private Proton() { }
	
	@Override
	public String getChemicalFormulaName()
	{
		return "p";
	}
	
	@Override
	public Map<IAtoms, Integer> getIonContain(EnumCountLevel level)
	{
		return level != EnumCountLevel.Atom ? FleEntry.asMap(new FleEntry(EnumAtoms.H, 1)) : super.getIonContain(level);
	}
	
	@Override
	public double getElementContain(EnumAtoms e)
	{
		return e == EnumAtoms.H ? 1 : 0;
	}
}