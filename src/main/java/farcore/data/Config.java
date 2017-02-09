/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.data;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * The configuration of Far Core.
 * @author ueyudiud
 *
 */
public class Config
{
	public static boolean enableWaterFreezeAndIceMeltTempCheck;
	public static boolean splitBrightnessOfSmallBlock;
	public static boolean useJavascriptInResource = true;
	public static boolean breakTreeMultiThread = false;
	
	//These general option is for modder.
	//If want to use Far Core mod but do not want added in these type of block/item.
	public static boolean createRock = true;
	public static boolean createLog = true;
	public static boolean createCrop = true;
	public static boolean createSoil = true;
	public static boolean replaceWater = true;
	
	public static void load(Configuration config)
	{
		enableWaterFreezeAndIceMeltTempCheck = config.getBoolean("enableWaterFreezeAndIceMeltTempCheck", "block", true, "The far core world has season changing, enable this option to freeze water or melt ice by actively temperature.");
		splitBrightnessOfSmallBlock = config.getBoolean("splitBrightnessOfSmallBlock", "render", false, "(WIP config) Split brightness when caculating brightness.");
		//		useJavascriptInResource = config.getBoolean("useJavascriptInResource", "resource", false, "Enable this option to enable use javascript file in resource loading, but make sure lib 'nashorn' is included.");
		breakTreeMultiThread = config.getBoolean("breakTreeMultiThread", "block", false, "(WIP config) Break natural log block with a new thread to check range, this is proved will let server out of sych! Do not suggested to use.");
	}
	
	private static boolean getAndReplaceToDefault(Configuration config, String key, String category, boolean defValue, String comment)
	{
		Property property = config.get(category, key, defValue, comment);
		boolean flag = property.getBoolean();
		property.set(defValue);
		return flag;
	}
}