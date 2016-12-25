/*
 * copyright© 2016 ueyudiud
 */

package farcore;

import javax.script.ScriptEngineManager;

import farcore.network.Network;

/**
 * The FarCore mod.
 * @author ueyudiud
 */
public class FarCore
{
	/** The minimum forge version far core required. */
	public static final int MIN_FORGE = 2011;
	
	/** The core modid. */
	public static final String ID = "farcore";
	
	/** The far asm modid. */
	public static final String OVERRIDE_ID = "faroverride";
	
	/** The main modid. */
	public static final String MAIN_MOD_ID = "fle";
	
	/**
	 * Use in resource location, this locate will get
	 * resource from code instead of get resource from
	 * real path.
	 */
	public static final String INNER_RENDER = "farinner";
	
	/**
	 * The network instance of far core mod.
	 */
	public static Network network;
	
	/**
	 * The debug mode flag, enable to switch to
	 * debug mode.<br>
	 * The debug mode will give more information
	 * of game.<br>
	 */
	public static boolean debug = false;
	
	/**
	 * The flag of world generation, if switch this option to true,
	 * some block behavior will be changed.<p>
	 * Example:
	 * The ore will try to create tile with material selected.
	 * The crop will try to create tile with native DNA.
	 */
	public static boolean worldGenerationFlag = false;
	
	/**
	 * The script engine. Use to load script from resource pack,
	 * it used by TextureLoader now, but I think it can also make
	 * other uses.<p>
	 * The default runtime environment do not contain some library which is
	 * requirement (Such as nashorn), players need change startup parameters
	 * by themselves, so I don't suggested use script in too many ways...
	 */
	public static final ScriptEngineManager MANAGER = new ScriptEngineManager();
	
	/**
	 * Catching an exception which might cause such serious bug,
	 * the method will throw a new RuntimeException if game is in debuging
	 * mode.
	 * @param exception
	 * @throws RuntimeException
	 */
	public static void catching(Exception exception) throws RuntimeException
	{
		if(debug)
		{
			RuntimeException exception1 = new RuntimeException(exception);
			exception.setStackTrace(new StackTraceElement[0]);
			throw new RuntimeException(exception);
		}
	}
}