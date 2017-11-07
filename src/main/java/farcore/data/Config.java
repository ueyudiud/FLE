/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.data;

import nebula.common.config.ConfigComment;
import nebula.common.config.ConfigProperty;

/**
 * The configuration of Far Core.
 * 
 * @author ueyudiud
 */
@nebula.common.config.Config("farcore")
public class Config
{
	@ConfigProperty(category = "block", defValue = "true")
	@ConfigComment("The Far Core world has season changing, enable this option to freeze water or melt ice by actively temperature.")
	public static boolean	enableWaterFreezeAndIceMeltTempCheck;
	@ConfigProperty(category = "block", defValue = "true")
	@ConfigComment("Enable this option and the water block can be evaporated in high temperature.")
	public static boolean	enableWaterEvaporation;
	@ConfigProperty(category = "render", defValue = "false")
	@ConfigComment("(WIP config) Split brightness when caculating brightness.")
	public static boolean	splitBrightnessOfSmallBlock;
	@Deprecated
	// @ConfigProperty(category = "block", defValue = "false")
	// @ConfigComment("(WIP config) Break natural log block with a new thread to
	// check range, this is proved will let server out of sych! Do not suggested
	// to use.")
	public static boolean	breakTreeMultiThread;
	@ConfigProperty(category = "block", defValue = "false")
	@ConfigComment("The leaves will drop items when decaying when enable this option, But make sure you can accept lower fps beacuase large amount of dropped " + "items from tree leaves.")
	public static boolean	droppingWhenDecay;
	@ConfigProperty(category = "block", defValue = "true")
	@ConfigComment("Enable this option and you can 'climb' the vine as a ladder, just like vanilla vine in Minecraft.")
	public static boolean	useVineAsLadder;
}
