/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.bio;

import java.util.Collection;

import nebula.base.register.IRegisteredNameable;

/**
 * @author ueyudiud
 */
public interface IFamily<S extends ISpecie> extends IRegisteredNameable
{
	IOrder getOrder();
	
	S getSpecie(String name);
	
	S getSpecie(byte[][] data);
	
	Collection<? extends S> getSpecies();
}
