/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.block.property;

import net.minecraft.block.properties.IProperty;

public interface IStandardProperty<T extends Comparable<T>> extends IProperty<T>
{
	T instance();
}