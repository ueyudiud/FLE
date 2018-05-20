/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.bio;

import nebula.base.register.IRegisteredNameable;

/**
 * @author ueyudiud
 */
public interface ISpecie extends IRegisteredNameable
{
	IOrder getOrder();
	
	IFamily getFamily();
	
	BioData example();
}
