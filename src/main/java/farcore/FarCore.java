package farcore;

import java.util.ArrayList;
import java.util.List;

import farcore.lib.world.IWorldGenerateReplacer;
import farcore.network.Network;

public class FarCore
{
	/**
	 * The core modid.
	 */
	public static final String ID = "farcore";
	/**
	 * The far asm modid.
	 */
	public static final String OVERRIDE_ID = "faroverride";

	/**
	 * The main modid.
	 */
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
	 * The world generate replacer during world generating.
	 */
	public static final List<IWorldGenerateReplacer> worldGenerateReplacers = new ArrayList();

	/**
	 * The debug mode flag, enable to switch to
	 * debug mode.<br>
	 * The debug mode will give more information
	 * of game.<br>
	 */
	public static boolean debug = false;
}