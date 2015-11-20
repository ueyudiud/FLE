package fle.api.material;

import java.util.Map;

import fle.api.enums.EnumAtoms;
import fle.api.util.FleEntry;

public abstract class ElementaryParticle implements IAtoms
{
	@Override
	public double getElementContain(EnumAtoms e)
	{
		return 0;
	}

	@Override
	public Map<IAtoms, Integer> getIonContain(EnumCountLevel level)
	{
		return FleEntry.asMap(new FleEntry(this, 1));
	}

	@Override
	public Map<EnumAtoms, Integer> getElementAtoms()
	{
		return FleEntry.asMap(new FleEntry[0]);
	}

	@Override
	public boolean isRadical()
	{
		return false;
	}
}