/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.bio;

import java.util.Random;

import javax.annotation.Nullable;

import nebula.common.util.L;

/**
 * @author ueyudiud
 */
public interface IBiology
{
	static Random random(@Nullable IBiology biology)
	{
		return biology == null ? L.random() : biology.rng();
	}
	
	default Random rng()
	{
		return L.random();
	}
	
	default <T extends IBiology> IFamily<T> getFamily()
	{
		return (IFamily<T>) getSpecie().getFamily();
	}
	
	<T extends IBiology> ISpecie<T> getSpecie();
	
	GeneticMaterial getGeneticMaterial();
}