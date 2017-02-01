/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common;

import net.minecraftforge.common.config.Configuration;

/**
 * @author ueyudiud
 */
public class NebulaConfig
{
	public static void init(Configuration config)
	{
		displayFluidInTab = config.getBoolean("displayFluidInTab", "common", false, "Enable this option to display all fluid in fluid tab");
		multiThreadLight = config.getBoolean("lightChecking", "client", false, "The light checking takes lots of time, enable this option to decrease the effect of light checking (But may use more memory).");
		enableWaterStat = config.getBoolean("waterStat", "common", true, "Enable this option will needed driking water to be alive.");
	}
	
	public static boolean displayFluidInTab;
	public static boolean multiThreadLight;
	public static boolean enableWaterStat;
}