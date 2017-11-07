/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore;

import nebula.Log;
import nebula.Nebula;

/**
 * The FarCore mod, here are some basic mod field and methods. To get mod
 * instance, see {@link FarCoreSetup}
 * 
 * @author ueyudiud
 */
public class FarCore
{
	/** The core modid. */
	public static final String ID = "farcore";
	
	/** The main modid. */
	public static final String MAIN_MOD_ID = "fle";
	
	/**
	 * Use in resource location, this locate will get resource from code instead
	 * of get resource from real path.
	 */
	public static final String INNER_RENDER = "farinner";
	
	/**
	 * The flag of world generation, if switch this option to true, some block
	 * behavior will be changed.
	 * <p>
	 * Example: The ore will try to create tile with material selected. The crop
	 * will try to create tile with native DNA.
	 */
	public static boolean worldGenerationFlag = false;
	
	/**
	 * Catching an exception which might cause such serious bug, the method will
	 * throw a new RuntimeException if game is in debugging mode.
	 * 
	 * @param exception the caught exception, it will only be <tt>throw</tt>
	 *            when debugging mode enabled.
	 * @throws RuntimeException if debugging mode enabled.
	 * @see nebula.Nebula#debug
	 */
	public static void catching(Exception exception) throws RuntimeException
	{
		if (Nebula.debug)
		{
			exception.setStackTrace(new StackTraceElement[0]);
			throw new RuntimeException(exception);
		}
		else
		{
			Log.catching(exception);
		}
	}
}
