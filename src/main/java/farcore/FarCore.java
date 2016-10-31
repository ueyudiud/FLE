//copyright© 2016 by ueyudiud
package farcore;

import farcore.network.Network;
import net.minecraftforge.fml.common.SidedProxy;

public class FarCore
{
	/** The minimum forge version far core required. */
	public static final int minForge = 2011;

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
	 * The sided proxy of far core.
	 */
	@SidedProxy(serverSide = "farcore.FarCoreSetup$Proxy", clientSide = "farcore.FarCoreSetup$ClientProxy")
	public static FarCoreSetup.Proxy proxy;
	
	/**
	 * The flag of world generation, if switch this option to true,
	 * some block behavior will be changed.<p>
	 * Example:
	 * The ore will try to create tile with material selected.
	 * The crop will try to create tile with native dna.
	 */
	public static boolean worldGenerationFlag = false;
}