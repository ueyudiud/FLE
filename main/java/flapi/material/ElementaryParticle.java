package flapi.material;

import java.util.Map;

import farcore.collection.CollectionUtil;
import farcore.collection.CollectionUtil.FleEntry;
import flapi.chem.base.IMolecular;
import flapi.chem.particle.Atoms;

public abstract class ElementaryParticle implements IMolecular
{
	@Override
	public double getElementContain(Atoms e)
	{
		return 0;
	}

	@Override
	public Map<IMolecular, Integer> getIonContain(EnumCountLevel level)
	{
		return CollectionUtil.asMap(new FleEntry(this, 1));
	}

	@Override
	public Map<Atoms, Integer> getElementAtoms()
	{
		return CollectionUtil.asMap(new FleEntry[0]);
	}

	@Override
	public boolean isRadical()
	{
		return false;
	}
}