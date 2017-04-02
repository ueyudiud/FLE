/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.asm.keyword;

/**
 * Internal use only.
 * @author ueyudiud
 */
public class KeywordFactory
{
	public static Object a(Object arg)
	{
		if (arg instanceof String)
		{
			return l1((String) arg);
		}
		else if (arg instanceof Comparable)
		{
			return l2((Comparable) arg);
		}
		else throw new TypeMissmatchException(arg.getClass());
	}
	
	private static Object l1(String string)
	{
		return string;
	}
	
	private static Object l2(Comparable comparable)
	{
		return comparable;
	}
}