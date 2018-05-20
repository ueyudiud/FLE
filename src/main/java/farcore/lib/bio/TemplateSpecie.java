/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.bio;

import java.util.Random;

import farcore.lib.material.Mat;
import nebula.common.util.L;

/**
 * @author ueyudiud
 */
public abstract class TemplateSpecie implements ISpecieSP
{
	protected /*final*/ TemplateFamily<?> family;
	protected Mat material;
	protected int[] capabilities;
	
	public TemplateSpecie()
	{
		
	}
	
	@Override
	public String getRegisteredName()
	{
		return this.material.name;
	}
	
	@Override
	public IFamily getFamily()
	{
		return this.family;
	}
	
	@Override
	public IOrder getOrder()
	{
		return this.family.order;
	}
	
	@Override
	public BioData example()
	{
		return new BioData(this, "wild", 0, this.family.setDefaultSpecie(L.castAny(this)), this.capabilities.clone());
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
			c[i] = this.family.mutateGene(rand.nextBoolean() ? s[i << 1] : s[i << 1 | 1],
					(int) (GeneData.TOTAL / 100 * data.generation * Math.exp(- data.generation / 20.0)), rand);
		}
		ISpecie specie = this.family.getSpecie(c);
		int[] cp = data.capabilities.clone();
		for (int i = 0; i < cp.length; ++i)
		{
			cp[i] = (data.capabilities[i] * 8 + rand.nextInt(2000_0)) / 10;
		}
		return new BioData(
				specie,
				getRegisteredName(),
				data.generation, c, cp);
	}
}
