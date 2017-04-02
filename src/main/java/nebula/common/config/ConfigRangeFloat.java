/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ueyudiud
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigRangeFloat
{
	double min() default Double.MAX_VALUE;
	
	double max() default Double.MIN_VALUE;
}