/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.util;

import javax.annotation.Nullable;

/**
 * @author ueyudiud
 */
@FunctionalInterface
public interface Executable<T>
{
	static Executable<?> NO_EXECUTION = target -> {};
	
	static <T> void execute(@Nullable Executable<T> executable, T target)
	{
		if(executable != null)
		{
			executable.execute(target);
		}
	}
	
	/**
	 * Use to execute selected action.
	 */
	void execute(T target);
}