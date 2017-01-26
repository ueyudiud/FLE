/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.data;

import farcore.util.U.Sides;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * The configuration of Far Core.
 * @author ueyudiud
 *
 */
public class Config
{
	public static boolean displayFluidInTab;
	public static boolean multiThreadLight;
	public static boolean enableWaterFreezeAndIceMeltTempCheck;
	public static boolean splitBrightnessOfSmallBlock;
	public static boolean enableWaterStat;
	public static boolean useJavascriptInResource;
	public static boolean replaceMissingIDToAir;
	
	//These general option is for modder.
	//If want to use Far Core mod but do not want added in these type of block/item.
	public static boolean createRock = true;
	public static boolean createLog = true;
	public static boolean createCrop = true;
	public static boolean createSoil = true;
	public static boolean replaceWater = true;
	
	public static void load(Configuration config)
	{
		displayFluidInTab = config.getBoolean("displayFluidInTab", "general", false, "Enable this option to display all fluid in fluid tab");
		multiThreadLight = config.getBoolean("lightChecking", "multithread", false, "The light checking takes lots of time, enable this option to decrease the effect of light checking (But may use more memory).");
		enableWaterFreezeAndIceMeltTempCheck = config.getBoolean("enableWaterFreezeAndIceMeltTempCheck", "block", true, "The far core world has season changing, enable this option to freeze water or melt ice by actively temperature.");
		splitBrightnessOfSmallBlock = config.getBoolean("splitBrightnessOfSmallBlock", "render", false, "(WIP config) Split brightness when caculating brightness.");
		enableWaterStat = config.getBoolean("waterStat", "gamerule", true, "Enable this option will needed driking water to be alive.");
		useJavascriptInResource = config.getBoolean("useJavascriptInResource", "resource", false, "Enable this option to enable use javascript file in resource loading, but make sure lib 'nashorn' is included.");
		if(Sides.isServer())
		{
			replaceMissingIDToAir = getAndReplaceToDefault(config, "replaceMissingStateToAir", "idfix", false, "Enable this option to replace missing block state in world, uses when server starting, this option will be reset after id is replaced.");
			config.setCategoryRequiresWorldRestart("idfix", true);
		}
	}
	
	private static boolean getAndReplaceToDefault(Configuration config, String key, String category, boolean defValue, String comment)
	{
		Property property = config.get(category, key, defValue, comment);
		boolean flag = property.getBoolean();
		property.set(defValue);
		return flag;
	}
}