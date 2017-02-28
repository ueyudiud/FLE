/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.bio;

import nebula.common.util.IRegisteredNameable;

/**
 * @author ueyudiud
 */
public interface ISpecie<B extends IBiology> extends IRegisteredNameable
{
	IFamily<B> getFamily();
	
	GeneticMaterial createNativeGeneticMaterial();
	
	GeneticMaterial createGameteGeneticMaterial(B biology, GeneticMaterial gm);
	
	void expressTrait(B biology, GeneticMaterial gm);
}