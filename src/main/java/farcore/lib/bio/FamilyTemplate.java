/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.bio;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import nebula.base.Ety;
import nebula.base.INode;
import nebula.base.Judgable;
import nebula.base.Node;

/**
 * @author ueyudiud
 */
public class FamilyTemplate<T extends ISpecie<H>, H extends IBiology> implements IFamily<H>
{
	protected final String									name;
	protected T												baseType;
	protected Set<T>										list	= new HashSet<>();
	protected INode<Entry<Judgable<GeneticMaterial>, T>>	judgables;
	
	public FamilyTemplate(String name)
	{
		this.name = name;
	}
	
	public FamilyTemplate(T specie)
	{
		this(specie.getRegisteredName());
		this.baseType = specie;
		this.list.add(specie);
	}
	
	public FamilyTemplate<T, H> addBaseSpecie(T specie)
	{
		this.baseType = specie;
		return this;
	}
	
	public FamilyTemplate<T, H> addJudgable(Judgable<GeneticMaterial> judgable, T specie)
	{
		if (this.judgables == null)
		{
			this.judgables = Node.first(new Ety<>(judgable, specie));
		}
		else
		{
			this.judgables.addLast(new Ety<>(judgable, specie));
		}
		return this;
	}
	
	public FamilyTemplate<T, H> addSpecies(T...species)
	{
		this.list.addAll(Arrays.asList(species));
		return this;
	}
	
	@Override
	public final String getRegisteredName()
	{
		return this.name;
	}
	
	@Override
	public Collection<T> getSpecies()
	{
		return this.list;
	}
	
	@Override
	public T getSpecieFromGM(GeneticMaterial gm)
	{
		INode<Entry<Judgable<GeneticMaterial>, T>> entry;
		return this.judgables == null ? this.baseType : (entry = this.judgables.find(ety -> ety.getKey().isTrue(gm))) != null ? entry.value().getValue() : this.baseType;
	}
}
