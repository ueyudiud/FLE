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
public abstract class IntegratedSpecie<F extends IFamily<?>> implements ISpecieSP
{
	IntegratedFamily<?> family;
	byte dataWhiteList, dataBlackList;
	int[] capabilitiesBasic, capabilitiesRand;
	
	protected Mat material;
	
	public IntegratedSpecie()
	{
	}
	
	@Override
	public final IOrder getOrder()
	{
		return this.family.order;
	}
	
	@Override
	public final F getFamily()
	{
		return (F) this.family;
	}
	
	@Override
	public final BioData example()
	{
		return random(L.random());
	}
	
	public final BioData random(Random rand)
	{
		final int length = this.family.genes.length;
		byte[][] datas = new byte[2][length];
		for (int i = 0; i < length; ++i)
		{
			datas[0][i] = this.family.genes[i].firstObserve(rand);
			datas[1][i] = this.family.genes[i].firstObserve(rand);
		}
		int[] capabilities = new int[this.family.capcount];
		for (int i = 0; i < this.family.capcount; ++i)
		{
			capabilities[i] = this.capabilitiesBasic[i] + L.nextInt(this.capabilitiesRand[i], rand);
		}
		return new BioData(this, "wild", 0, datas, capabilities);
	}
	
	@Override
	public final BioData getGamete(BioData data, Random rand)
	{
		if ((data.chromosome.length & 0x1) != 0)
		{
			return null;
		}
		final byte[][] s = data.chromosome;
		byte[][] c = new byte[s.length >> 1][];
		int ch = (int) (GeneData.TOTAL / 100 * data.generation * Math.exp(- data.generation / 20.0));
		for (int i = 0; i < c.length; ++i)
		{
			byte[] s1 = rand.nextBoolean() ? s[i << 1] : s[i << 1 | 1];
			byte[] r1 = new byte[this.family.genes.length];
			for (int j = 0; i < this.family.genes.length; ++i)
			{
				r1[j] = ch > 0 && rand.nextInt(GeneData.TOTAL) < ch ?
						this.family.genes[j].nextObserve(s1[i], rand) : s1[i];
			}
			c[i] = r1;
		}
		ISpecie specie = this.family.getSpecie(c);
		int[] cp = data.capabilities.clone();
		for (int i = 0; i < cp.length; ++i)
		{
			cp[i] = (data.capabilities[i] * 9 + rand.nextInt(100)) / 10;
		}
		
		return new BioData(
				specie,
				getRegisteredName(),
				data.generation, c, cp);
	}
}
