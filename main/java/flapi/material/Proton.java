package flapi.material;

import java.util.Map;

import flapi.collection.CollectionUtil;
import flapi.collection.CollectionUtil.FleEntry;
import flapi.enums.EnumAtoms;

public class Proton extends ElementaryParticle
{
	public Proton p = new Proton();

	private Proton() { }
	
	@Override
	public String getChemName()
	{
		return "p";
	}
	
	@Override
	public Map<IMolecular, Integer> getIonContain(EnumCountLevel level)
	{
		return level != EnumCountLevel.Atom ? CollectionUtil.asMap(new FleEntry(EnumAtoms.H, 1)) : super.getIonContain(level);
	}
	
	@Override
	public double getElementContain(EnumAtoms e)
	{
		return e == EnumAtoms.H ? 1 : 0;
	}
}