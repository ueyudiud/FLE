package fle.core.init;

import static net.minecraftforge.common.ChestGenHooks.BONUS_CHEST;
import static net.minecraftforge.common.ChestGenHooks.DUNGEON_CHEST;
import static net.minecraftforge.common.ChestGenHooks.MINESHAFT_CORRIDOR;
import static net.minecraftforge.common.ChestGenHooks.PYRAMID_DESERT_CHEST;
import static net.minecraftforge.common.ChestGenHooks.PYRAMID_JUNGLE_CHEST;
import static net.minecraftforge.common.ChestGenHooks.PYRAMID_JUNGLE_DISPENSER;
import static net.minecraftforge.common.ChestGenHooks.STRONGHOLD_CORRIDOR;
import static net.minecraftforge.common.ChestGenHooks.STRONGHOLD_CROSSING;
import static net.minecraftforge.common.ChestGenHooks.STRONGHOLD_LIBRARY;
import static net.minecraftforge.common.ChestGenHooks.VILLAGE_BLACKSMITH;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.FishingHooks;
import fle.api.util.FleLog;
import fle.core.util.FlePotionEffect;
import fle.core.util.Util;

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
				Util.overrideField(ChestGenHooks.class, Arrays.asList("field_145985_p", "contents"), hook, new ArrayList(), true);
				ChestGenHooks.addItem(chest, new WeightedRandomChestContent(new ItemStack(Items.bread), 1, 3, 10));
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