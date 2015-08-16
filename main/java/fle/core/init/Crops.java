package fle.core.init;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import fle.core.block.crop.CropBase;
import fle.core.block.crop.CropRamie;
import fle.core.block.crop.CropSoybean;
import fle.core.item.ItemFleSeed;
import fle.core.item.ItemFleSub;
import fle.core.util.FleEntry;

public class Crops
{
	public static CropBase soybean;
	public static CropBase ramie;
	
	public static void init()
	{
		soybean = new CropSoybean();
		ramie = new CropRamie();
	}

	public static void postInit()
	{
		soybean.setSeed(ItemFleSeed.a("soybean")).setHaverstDrop(3, FleEntry.asMap(new FleEntry(ItemFleSeed.a("soybean"), 1)));
		ramie.setSeed(ItemFleSeed.a("ramie")).setHaverstDrop(3, FleEntry.asMap(new FleEntry(ItemFleSub.a("ramie_fiber"), 1)));
		
		MinecraftForge.addGrassSeed(ItemFleSeed.a("soybean"), 3);
		MinecraftForge.addGrassSeed(ItemFleSeed.a("ramie"), 3);
	}
}