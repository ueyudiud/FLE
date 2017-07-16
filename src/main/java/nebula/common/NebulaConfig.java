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
	@Deprecated
	public static boolean multiThreadLight;
	@ConfigProperty(category = "client", defValue = "true")
	@ConfigComment("Disable this option will let quad data be caculated during rendering, this will release some memory, but take more time to rendering.")
	public static boolean storeModelTransformedData;
}