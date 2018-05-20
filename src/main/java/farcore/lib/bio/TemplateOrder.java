/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.bio;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;

/**
 * @author ueyudiud
 */
public abstract class TemplateOrder<F extends IFamily<? extends S>, S extends ISpecie> implements IOrder<F, S>
{
	protected final Map<String, F> families = new HashMap<>();
	protected final Map<String, S> species = new HashMap<>();
	
	public void addFamily(F family)
	{
		this.families.put(family.getRegisteredName(), family);
		for (S s : family.getSpecies())
		{
			this.species.put(s.getRegisteredName(), s);
		}
	}
	
	@Override
	public F getFamily(String name)
	{
		return this.families.get(name);
	}
	
	@Override
	public S getSpecie(String name)
	{
		return this.species.get(name);
	}
	
	@Override
	public Iterator<S> iterator()
	{
		return this.species.values().iterator();
	}
	
	@Override
	public Spliterator<S> spliterator()
	{
		return this.species.values().spliterator();
	}
	
	@Override
	public Collection<? extends F> getFamilies()
	{
		return this.families.values();
	}
}
