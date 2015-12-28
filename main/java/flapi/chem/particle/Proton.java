package flapi.chem.particle;

import java.util.Map;

import flapi.chem.base.IMolecular;
import flapi.collection.CollectionUtil;
import flapi.collection.CollectionUtil.FleEntry;
import flapi.material.ElementaryParticle;

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
		return level != EnumCountLevel.Atom ? CollectionUtil.asMap(new FleEntry(Atoms.H, 1)) : super.getIonContain(level);
	}
	
	@Override
	public double getElementContain(Atoms e)
	{
		return e == Atoms.H ? 1 : 0;
	}
}