/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base.register;

import nebula.common.util.IRegisteredNameable;

/**
 * @author ueyudiud
 */
public interface IRegisterElement<T> extends IRegisteredNameable
{
	void setRegistryName(String name);
	
	Class<T> getTargetClass();
}