/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.config;

import java.lang.reflect.Field;

import net.minecraftforge.common.config.Configuration;

/**
 * @author ueyudiud
 */
public interface TypeAdapter<T>
{
	void injectProperty(Object arg, Field field, Configuration config, String category, String name, String defValue, String comments) throws Exception;
}
