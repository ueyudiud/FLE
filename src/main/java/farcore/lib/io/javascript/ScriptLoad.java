/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.io.javascript;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark on field to let script handler know this is loading target.
 * Please mark this annotation on public fields.
 * @author ueyudiud
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ScriptLoad
{
	
}