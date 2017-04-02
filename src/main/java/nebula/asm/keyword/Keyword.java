/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.asm.keyword;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Some helper built-in coded when ASM loaded.
 * <p>
 * So why this class is should be exist?
 * 
 * @author ueyudiud
 */
public final class Keyword
{
	@Finalize
	public static final <R> R apply(@Finalize final Supplier<R> supplier)
	{
		return supplier.get();
	}
	
	/**
	 * Equal to code as :
	 * <p>
	 * <code>
	 * if (arg instanceof X1)<br>
	 *  return f1.apply(arg);<br>
	 * else if (arg instance X2)<br>
	 *  return f2.apply(arg);<br>
	 * ...
	 * </code>
	 * 
	 * @param arg
	 * @param functions
	 * @return
	 * @throws TypeMissmatchException
	 *             When matched object is not belong any type.
	 */
	@Finalize
	public static final <R> R match(Object arg, @Finalize final Function<?, R>... functions)
			throws TypeMissmatchException
	{
		return (R) arg;
	}
	
	public static void match()
	{
		int x = 1;
		String arg = Keyword.<String>match(null,
				(String s)-> s.toString(),
				(Integer i)-> Integer.toString(i + x),
				(Object o)-> "");
	}
	
	private Keyword()
	{
	}
}