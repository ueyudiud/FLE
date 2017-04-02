/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.asm.keyword;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ueyudiud
 */
/**
 * Mark for use field will not initialize but replaced by local code.
 * 
 * @author ueyudiud
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
public @interface Finalize
{
	
}