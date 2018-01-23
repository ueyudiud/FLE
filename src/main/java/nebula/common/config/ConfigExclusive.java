/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.config;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author ueyudiud
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface ConfigExclusive
{
	
}
