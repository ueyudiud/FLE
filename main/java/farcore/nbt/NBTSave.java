package farcore.nbt;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Set a field or class type, register to NBT and it will load when save to NBT.
 * 
 * @author ueyudiud
 * 		
 */
@Target({FIELD, TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NBTSave
{
	String name();
	
	boolean save() default true;
	
	@Deprecated
	String[] valuesInput() default { };
}