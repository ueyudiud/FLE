package fle.load;

import farcore.enums.EnumItem;
import fle.core.achievement.AchievementFle;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;

public class Achievements
{
	public static Achievement openCrafting;
	
	public static void init()
	{
		openCrafting = new AchievementFle("openCrafting", 0, 0, EnumItem.debug.instance(), AchievementList.openInventory, false);
		
	}
}