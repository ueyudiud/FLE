/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author ueyudiud
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Reinitalizable
{
	
}