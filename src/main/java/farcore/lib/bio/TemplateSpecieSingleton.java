/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.bio;

import java.util.Collection;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import nebula.common.util.L;

/**
 * @author ueyudiud
 */
public abstract class TemplateSpecieSingleton<S extends TemplateSpecieSingleton<S>> implements IFamily<S>, ISpecieSP
{
	protected final IOrder<?, S> order;
	/**
	 * The gene format of this family species,
	 * each of first is the species predicated gene.
	 */
	protected GeneData[] gene;
	protected int[] capabilities;
	
	public TemplateSpecieSingleton(IOrder<?, S> order)
	{
		this.order = order;
	}
	
	@Override
	public IFamily getFamily()
	{
		return this;
	}
	
	@Override
	public BioData example()
	{
		return new BioData(this, "wild", 0, this.setDefaultSpecie(L.castAny(this)), this.capabilities.clone());
	}
	
	@Override
	public BioData getGamete(BioData data, Random rand)
	{
		if ((data.chromosome.length & 0x1) != 0)
		{
			return null;
		}
		final byte[][] s = data.chromosome;
		byte[][] c = new byte[s.length >> 1][];
		for (int i = 0; i < c.length; ++i)
		{
			c[i] = mutateGene(rand.nextBoolean() ? s[i << 1] : s[i << 1 | 1],
					(int) (GeneData.TOTAL / 100 * data.generation * Math.exp(- data.generation / 20.0)), rand);
		}
		ISpecie specie = getSpecie(c);
		int[] cp = data.capabilities.clone();
		for (int i = 0; i < cp.length; ++i)
		{
			cp[i] = (data.capabilities[i] * 8 + rand.nextInt(2000_0)) / 10;
		}
		return new BioData(
				specie,
				getRegisteredName(),
				data.generation, c, this.capabilities);
	}
	
	@Override
	public IOrder getOrder()
	{
		return this.order;
	}
	
	@Override
	public Collection<? extends S> getSpecies()
	{
		return ImmutableList.of((S) this);
	}
	
	@Override
	public S getSpecie(String name)
	{
		return getRegisteredName().equals(name) ? (S) this : null;
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
		return (S) this;
	}
}
