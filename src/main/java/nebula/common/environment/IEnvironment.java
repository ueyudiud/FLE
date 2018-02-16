/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.environment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nebula.base.IPropertyMap;
import nebula.common.world.ICoord;

public interface IEnvironment extends ICoord
{
	long worldTime();
	
	float biomeTemperature();
	
	/**
	 * Get property of environment.
	 * 
	 * @param property
	 * @return <code>null</null> if property is not present.
	 */
	@Nullable
	<T> T getValue(@Nonnull IPropertyMap.IProperty<T> property);
}
