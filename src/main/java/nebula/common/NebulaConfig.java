/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common;

import nebula.common.config.ConfigComment;
import nebula.common.config.ConfigProperty;

/**
 * @author ueyudiud
 */
@nebula.common.config.Config("nebula")
public class NebulaConfig
{
	//Common
	@ConfigProperty(category = "common", defValue = "false")
	@ConfigComment("Enable this option to display all fluid in fluid tab")
	public static boolean displayFluidInTab;
	@ConfigProperty(category = "common", defValue = "true")
	@ConfigComment("Enable this option will needed driking water to be alive.")
	public static boolean enableWaterStat;
	//Client
	@ConfigProperty(category = "client", defValue = "false")
	@ConfigComment("The light checking takes lots of time, enable this option to decrease the effect of light checking (But may use more memory).")
	public static boolean multiThreadLight;
}