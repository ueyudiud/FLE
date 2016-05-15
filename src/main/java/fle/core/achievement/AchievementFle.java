package fle.core.achievement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

public class AchievementFle extends Achievement
{
	private static final List<Achievement> achievements = new ArrayList();
	private static final int achievementBaseX = -4;
	private static final int achievementBaseY = -5;
	  
	public AchievementFle(String id, int x, int y,
			ItemStack display, Achievement requirement, boolean special)
	{
		super("fle." + id, id, achievementBaseX + x, achievementBaseY + y, display, requirement);
		if(special)
		{
			setSpecial();
		}
		registerStat();
		achievements.add(this);
	}
}