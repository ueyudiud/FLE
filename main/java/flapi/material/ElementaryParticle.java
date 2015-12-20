package flapi.material;

import java.util.Map;

import flapi.collection.CollectionUtil;
import flapi.collection.CollectionUtil.FleEntry;
import flapi.enums.EnumAtoms;

public abstract class ElementaryParticle implements IMolecular
{
	@Override
	public double getElementContain(EnumAtoms e)
	{
		return 0;
	}

	@Override
	public Map<IMolecular, Integer> getIonContain(EnumCountLevel level)
	{
		return CollectionUtil.asMap(new FleEntry(this, 1));
	}

	@Override
	public Map<EnumAtoms, Integer> getElementAtoms()
	{
		return CollectionUtil.asMap(new FleEntry[0]);
	}

	@Override
	public boolean isRadical()
	{
		return false;
	}
}