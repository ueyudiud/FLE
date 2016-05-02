package farcore.util;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.config.Configuration;

public class V
{
	public static boolean debug;
	/**
	 * This option to mark if you use world manager in FarCore,
	 * enable this option if use it in your mod.<br>
	 * Default value : disable.
	 */
	public static boolean disableWM = true;

	public static String matrixSolver;
	
	@SideOnly(Side.CLIENT)
	public static IIcon voidBlockIcon;
	@SideOnly(Side.CLIENT)
	public static IIcon voidItemIcon;
	public static int fallingBlockEntityID;
	public static int treeScanRange;
	public static boolean spawnWaterBySnow;
	
	public static void init(Configuration config)
	{
		debug = config.getBoolean("debug", "general", false, "Get more informationin  debug mode when enable this option.");
		matrixSolver = config.getString("matrixSolver", "general", "Gaussian", 
				"Which algorithms is used for solving matrix.", 
				new String[]{"QR", "Gaussian"});
		fallingBlockEntityID = config.getInt("fallingBlockExtended", "entity", 1, 0, 256, "");
		treeScanRange = config.getInt("treeScanRange", "general", 10, 5, 32, "To select a scan range, too small may cause can not,"
				+ "cut a tree in once, too big will use more memory.");
		spawnWaterBySnow = config.getBoolean("spawnWaterWhenMeltingSnow", "general", false, "If enable this option, the water will spawn when snow is melting.");
	}
}