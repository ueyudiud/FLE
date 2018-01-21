/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.bio;

import java.util.function.LongPredicate;

import nebula.base.A;
import nebula.base.Judgable;

/**
 * @author ueyudiud
 */
public class GMPredicate
{
	private static final LongPredicate[] VALUES;
	
	static
	{
		VALUES = new LongPredicate[128];
		for (int i = 0; i < 64; ++i)
		{
			final long i1 = 1L << i;
			VALUES[i] = value -> (value & i1) != 0;
		}
		for (int i = 0; i < 64; ++i)
		{
			final long i1 = 1L << i;
			VALUES[i + 64] = value -> (value & i1) == 0;
		}
	}
	
	public static Judgable<GeneticMaterial> explicit(int type)
	{
		byte t = (byte) type;
		return gm -> A.or(gm.coders, VALUES[t]);
	}
	
	public static Judgable<GeneticMaterial> implicit(int type)
	{
		byte t = (byte) (type | 0x80);
		return gm -> A.and(gm.coders, VALUES[t]);
	}
}
