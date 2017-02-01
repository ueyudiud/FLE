/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.crop.dna;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import javax.annotation.Nullable;

import farcore.lib.bio.DNAPair;
import farcore.lib.crop.ICropAccess;
import nebula.common.base.IntegerEntry;
import nebula.common.base.Selector;
import nebula.common.util.L;

/**
 * @author ueyudiud
 */
public final class DNAHCropSimple extends DNAHCrop
{
	public static DNAHCropSimpleBuilder builder(String name)
	{
		return builder(-1, name);
	}
	public static DNAHCropSimpleBuilder builder(int id, String name)
	{
		return new DNAHCropSimpleBuilder(id, name);
	}
	
	private final Selector<Character>[] nativeSelector;
	private final Map<Character, IntegerEntry<Selector<Character>>> mutationSelector;
	
	DNAHCropSimple(String name, Selector<Character>[] ns, Map<Character, IntegerEntry<Selector<Character>>> ms, Function<Character, int[]> e)
	{
		super(name);
		this.nativeSelector = ns;
		this.mutationSelector = ms;
		this.expression = e;
	}
	DNAHCropSimple(int id, String name, Selector<Character>[] ns, Map<Character, IntegerEntry<Selector<Character>>> ms, Function<Character, int[]> e)
	{
		super(id, name);
		this.nativeSelector = ns;
		this.mutationSelector = ms;
		this.expression = e;
	}
	
	@Override
	public DNAPair<ICropAccess> createNative(@Nullable ICropAccess target, Random random)
	{
		return new DNAPair(this, this.nativeSelector[0].next(random), this.nativeSelector[1].next(random));
	}
	
	@Override
	public DNAPair<ICropAccess> createGamete(ICropAccess target, DNAPair<ICropAccess> pair, boolean mutate)
	{
		char select = L.random(target.rng(), new char[]{pair.DNA1, pair.DNA2});
		if(mutate && this.mutationSelector != null && this.mutationSelector.containsKey(select))
		{
			IntegerEntry<Selector<Character>> entry = this.mutationSelector.get(select);
			if(target.rng().nextInt(10000) < entry.getValue())
			{
				select = entry.getKey().apply(target.rng());
			}
		}
		return new DNAPair(this, select);
	}
}