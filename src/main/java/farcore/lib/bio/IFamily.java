/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.bio;

import java.util.Collection;

import nebula.common.util.IRegisteredNameable;

/**
 * @author ueyudiud
 */
public interface IFamily<B extends IBiology> extends IRegisteredNameable
{
	Collection<? extends ISpecie<B>> getSpecies();
	
	ISpecie<B> getSpecieFromGM(GeneticMaterial gm);
	
	default void expressTrait(B biology, GeneticMaterial gm)
	{
		biology.getSpecie().expressTrait(biology, gm);
	}
}
