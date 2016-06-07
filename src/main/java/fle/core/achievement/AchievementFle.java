package fle.core.achievement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import farcore.util.U;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AchievementFle extends Achievement
{
	private static final HashMap<String, List<Achievement>> achievements = new HashMap();
	private static final int achievementBaseX = -4;
	private static final int achievementBaseY = -5;
	
	public static void registerAchievementPage(String name, String tag)
	{
		AchievementPage.registerAchievementPage(new AchievementPage(name, U.Lang.cast(achievements.get(tag), Achievement.class)));
	}
	
	public AchievementFle(String tag, String id, int x, int y,
			ItemStack display, Achievement requirement, boolean special)
	{
		super("fle." + tag + "." + id, id, achievementBaseX + x, achievementBaseY + y, display, requirement);
		if(special)
		{
			setSpecial();
		}
		registerStat();
		if(!achievements.containsKey(tag))
		{
			achievements.put(tag, new ArrayList());
		}
		achievements.get(tag).add(this);
	}
}