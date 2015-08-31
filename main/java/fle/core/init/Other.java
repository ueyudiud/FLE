package fle.core.init;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import fle.api.material.MaterialAbstract;
import fle.api.util.FleLog;
import fle.core.util.FlePotionEffect;
import fle.core.util.Util;
import net.minecraft.potion.Potion;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.FishingHooks;
import static net.minecraftforge.common.ChestGenHooks.*;

public class Other
{	
	private static final String[] chests = {BONUS_CHEST, DUNGEON_CHEST, MINESHAFT_CORRIDOR, PYRAMID_DESERT_CHEST, PYRAMID_JUNGLE_CHEST, PYRAMID_JUNGLE_DISPENSER, STRONGHOLD_CORRIDOR, STRONGHOLD_CROSSING, STRONGHOLD_LIBRARY, VILLAGE_BLACKSMITH};
	
	public static void init()
	{
		FlePotionEffect.init();
		try
		{
			for(String chest : chests)
			{
				ChestGenHooks hook = ChestGenHooks.getInfo(chest);
				Util.overrideField(ChestGenHooks.class, Arrays.asList(chest), hook, new ArrayList(), true);
			}
		}
		catch(Throwable e)
		{
			FleLog.getLogger().warn("Fle fail to remove chest generate.");
		}
		try
		{
			Util.overrideStaticField(FishingHooks.class, Arrays.asList("fish"), new ArrayList(), true);
			Util.overrideStaticField(FishingHooks.class, Arrays.asList("junk"), new ArrayList(), true);
			Util.overrideStaticField(FishingHooks.class, Arrays.asList("treasure"), new ArrayList(), true);
		}
		catch(Throwable e)
		{
			FleLog.getLogger().warn("Fle fail to remove village trans.");
		}
	}
}