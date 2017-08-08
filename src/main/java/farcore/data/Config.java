/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.data;

import nebula.common.config.ConfigComment;
import nebula.common.config.ConfigProperty;

/**
 * The configuration of Far Core.
 * @author ueyudiud
 *
 */
@nebula.common.config.Config("farcore")
public class Config
{
	@ConfigProperty(category = "block", defValue ="true")
	@ConfigComment("The far core world has season changing, enable this option to freeze water or melt ice by actively temperature.")
	public static boolean enableWaterFreezeAndIceMeltTempCheck;
	@ConfigProperty(category = "render", defValue ="false")
	@ConfigComment("(WIP config) Split brightness when caculating brightness.")
	public static boolean splitBrightnessOfSmallBlock;
	@Deprecated
	public static final boolean useJavascriptInResource = true;
	@ConfigProperty(category = "block", defValue = "false")
	@ConfigComment("(WIP config) Break natural log block with a new thread to check range, this is proved will let server out of sych! Do not suggested to use.")
	public static boolean breakTreeMultiThread;
	
	//The useful lib are already move to Nebula mod, so you needn't use FarCore if you only want use helper.
	//	public static boolean createRock;
	//	public static boolean createLog;
	//	public static boolean createCrop;
	//	public static boolean createSoil;
	//	public static boolean replaceWater;
}