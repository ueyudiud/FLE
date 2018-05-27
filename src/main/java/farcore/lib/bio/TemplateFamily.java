/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.bio;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import nebula.common.util.L;

/**
 * @author ueyudiud
 */
public abstract class TemplateFamily<S extends TemplateSpecie> implements IFamily<S>
{
	protected final IOrder<?, S> order;
	protected final String name;
	protected Map<String, S> species = new HashMap<>();
	/**
	 * The gene format of this family species,
	 * each of first is the species predicated gene.
	 */
	protected GeneData[] gene;
	
	public TemplateFamily(IOrder<?, S> order, String name)
	{
		this.order = order;
		this.name = name;
	}
	
	@Override
	public final String getRegisteredName()
	{
		return this.name;
	}
	
	public void addSpecie(S specie)
	{
		this.species.put(specie.getRegisteredName(), specie);
		specie.family = this;
	}
	
	public IOrder<?, S> getOrder()
	{
		return this.order;
	}
	
	@Override
	public Collection<? extends S> getSpecies()
	{
		return this.species.values();
	}
	
	@Override
	public S getSpecie(String name)
	{
		return this.species.get(name);
	}
	
	public byte[] mutateGene(byte[] data, int mChance, Random rand)
	{
		byte[] result = new byte[this.gene.length];
		for (int i = 0; i < this.gene.length; ++i)
		{
			result[i] =
					mChance > 0 && rand.nextInt(GeneData.TOTAL) < mChance ?
							this.gene[i].nextObserve(data[i], rand) : data[i];
		}
		return result;
	}
	
	public final byte[][] setDefaultSpecie(S specie)
	{
		byte[][] data = new byte[2][this.gene.length];
		for (int i = 0; i < 2; ++i)
		{
			for (int j = 0; j < 2; ++j)
			{
				data[i][j] = this.gene[j].firstObserve(L.random());
			}
		}
		setDefaultSpecie(specie, data);
		return data;
	}
	
	protected void setDefaultSpecie(S specie, byte[][] data)
	{
		for (int i = 0; i < data.length; ++i)
		{
			data[i][0] = 0;
		}
	}
	
	public S getSpecie(byte[][] genes)
	{
		return this.species.values().iterator().next();
	}
}
