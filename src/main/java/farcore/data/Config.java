package farcore.data;

import net.minecraftforge.common.config.Configuration;

public class Config
{
	public static boolean displayFluidInTab;
	public static boolean multiThreadLight;
	public static boolean enableWaterFreezeAndIceMeltTempCheck;
	
	public static void load(Configuration config)
	{
		displayFluidInTab = config.getBoolean("displayFluidInTab", "general", false, "Enable this option to display all fluid in fluid tab");
		multiThreadLight = config.getBoolean("lightChecking", "multithread", false, "The light checking takes lots of time, enable this option to decrease the effect of light checking (But may use more memory).");
		enableWaterFreezeAndIceMeltTempCheck = config.getBoolean("enableWaterFreezeAndIceMeltTempCheck", "block", true, "The far core world has season changing, enable this option to freeze water or melt ice by actively temperature.");
	}
}