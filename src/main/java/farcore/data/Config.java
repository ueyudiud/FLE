package farcore.data;

import net.minecraftforge.common.config.Configuration;

public class Config
{
	public static boolean displayFluidInTab;
	
	public static void load(Configuration config)
	{
		displayFluidInTab = config.getBoolean("displayFluidInTab", "general", false, "Enable this option to display all fluid in fluid tab");
	}
}