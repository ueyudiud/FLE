/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.bio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * @author ueyudiud
 */
public class IntegratedFamily<S extends IntegratedSpecie> implements IFamily<S>
{
	public static final byte
	L = 0,
	K = 1,
	I = 2,
	E = 3;
	
	protected final IOrder order;
	protected final int capcount;
	protected final int predicate;
	protected final String name;
	protected final Map<String, S> species;
	protected final GeneData[] genes;
	protected final S def;
	protected final IntegratedSpecie[] checkQue;
	
	public static abstract class Builder<F extends IntegratedFamily<S>, S extends IntegratedSpecie>
	{
		protected final IOrder order;
		protected final int capcount;
		protected final String name;
		protected S def;
		protected int predicate = 0;
		
		protected ImmutableMap.Builder<String, S> builder = ImmutableMap.builder();
		protected List<S> list = new ArrayList<>();
		protected List<GeneData> genes = new ArrayList<>();
		
		protected Builder(IOrder order, int capcount, String name)
		{
			this.order = order;
			this.capcount = capcount;
			this.name = name;
		}
		
		public Builder<F, S> setPredicate(int predicate)
		{
			this.predicate = predicate;
			return this;
		}
		
		public Builder<F, S> addDefSpecie(S species, int[] capDef, int[] capRand)
		{
			assert this.capcount == capDef.length && this.capcount == capRand.length;
			this.def = species;
			this.builder.put(species.getRegisteredName(), species);
			species.capabilitiesBasic = capDef;
			species.capabilitiesRand = capRand;
			return this;
		}
		
		public Builder<F, S> addSpecie(S species, int white, int black, int[] capDef, int[] capRand)
		{
			assert this.capcount == capDef.length && this.capcount == capRand.length;
			this.builder.put(species.getRegisteredName(), species);
			this.list.add(species);
			species.dataWhiteList = (byte) white;
			species.dataBlackList = (byte) black;
			species.capabilitiesBasic = capDef;
			species.capabilitiesRand = capRand;
			return this;
		}
		
		public Builder<F, S> addGene(int def, int[][] map)
		{
			int[] a = new int[map.length];
			a[def] = GeneData.TOTAL;
			return addGene(a, map);
		}
		
		public Builder<F, S> addGene(int[] def, int[][] map)
		{
			this.genes.add(new GeneData(def, map));
			return this;
		}
		
		public final F build()
		{
			assert this.def != null;
			this.list.add(this.def);
			F family = build(this.order, this.capcount, this.predicate, this.name,
					this.builder.build(), this.genes.toArray(new GeneData[this.genes.size()]),
					this.def, this.list.toArray(new IntegratedSpecie[this.list.size()]));
			for (S s : this.list)
			{
				s.family = family;
			}
			return family;
		}
		
		protected abstract F build(IOrder order, int capcount, int predicate, String name, Map<String, S> species, GeneData[] genes, S def, IntegratedSpecie[] checkQue);
	}
	
	public IntegratedFamily(IOrder order, int capcount, int predicate, String name, Map<String, S> species, GeneData[] genes, S def, IntegratedSpecie[] checkQue)
	{
		this.order = order;
		this.capcount = capcount;
		this.predicate = predicate;
		this.name = name;
		this.species = species;
		this.def = def;
		this.checkQue = checkQue;
		this.genes = genes;
	}
	
	@Override
	public final String getRegisteredName()
	{
		return this.name;
	}
	
	@Override
	public IOrder<?, ? extends ISpecie> getOrder()
	{
		return this.order;
	}
	
	@Override
	public S getSpecie(String name)
	{
		return this.species.get(name);
	}
	
	@Override
	public S getSpecie(byte[][] data)
	{
		{
			label: for (IntegratedSpecie s : this.checkQue)
			{
				for (int i = 0; i < this.predicate; ++i)
				{
					boolean flag = false;
					for (byte[] element : data)
					{
						if (element[i] == E)
						{
							flag = true;
							break;
						}
					}
					if (flag ? (s.dataBlackList & 1 << i) != 0 : (s.dataWhiteList & 1 << i) != 0)
					{
						continue label;
					}
				}
				return (S) s;
			}
		}
		return this.def;
	}
	
	@Override
	public Collection<? extends S> getSpecies()
	{
		return this.species.values();
	}
}
