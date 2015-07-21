package fla.core.init;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import fla.api.util.FlaValue;
import fla.core.FlaCrops;
import fla.core.block.crop.CropRamie;
import fla.core.block.crop.CropSoybean;
import fla.core.item.ItemFlaSeed;
import fla.core.item.ItemSub;

public class C 
{
	public static void init()
	{
		FlaCrops.soybean = new CropSoybean();
		FlaCrops.ramie = new CropRamie();
		ItemFlaSeed.registerCrop(FlaCrops.soybean, FlaValue.TEXT_FILE_NAME + ":crop/soybean");
		ItemFlaSeed.registerCrop(FlaCrops.ramie, FlaValue.TEXT_FILE_NAME + ":crop/ramie_seed");
	}

	public static void postInit()
	{
		Map<ItemStack, Integer> dropMap = new HashMap();
		dropMap.put(ItemFlaSeed.a("soybean"), 1);
		FlaCrops.soybean.setSeed(ItemFlaSeed.a("soybean")).setHaverstDrop(3, dropMap);
		dropMap = new HashMap();
		
		dropMap.put(ItemSub.a("ramie_fiber"), 1);
		FlaCrops.ramie.setSeed(ItemFlaSeed.a("ramie")).setHaverstDrop(3, dropMap);
		
		MinecraftForge.addGrassSeed(ItemFlaSeed.a("soybean"), 3);
		MinecraftForge.addGrassSeed(ItemFlaSeed.a("ramie"), 3);
	}
}
