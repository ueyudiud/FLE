/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base;

import java.util.List;

/**
 * @author ueyudiud
 */
public interface NLists
{
	static <E> List<E> asList(Object...objects)
	{
		return new ArrayListArgument<>(objects);
	}
}