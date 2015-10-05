package fle.core.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import fle.api.util.FleEntry;
import fle.core.block.crop.CropBase;
import fle.core.block.crop.CropCotton;
import fle.core.block.crop.CropMillet;
import fle.core.block.crop.CropRamie;
import fle.core.block.crop.CropSoybean;
import fle.core.block.crop.CropSugerCances;
import fle.core.block.crop.CropWheat;
import fle.core.item.ItemFleSeed;
import fle.core.item.ItemFleSub;

public class Crops
{
	public static CropBase soybean;
	public static CropBase ramie;
	public static CropBase millet;
	public static CropBase wheat;
	public static CropBase suger_cances;
	public static CropBase cotton;
	
	public static void init()
	{
		soybean = new CropSoybean();
		ramie = new CropRamie();
		millet = new CropMillet();
		wheat = new CropWheat();
		suger_cances = new CropSugerCances();
		cotton = new CropCotton();
	}

	public static void postInit()
	{
		soybean.setSeed(ItemFleSeed.a("soybean")).setHaverstDrop(3, FleEntry.asMap(new FleEntry(ItemFleSeed.a("soybean"), 1)));
		ramie.setSeed(ItemFleSeed.a("ramie")).setHaverstDrop(2, FleEntry.asMap(new FleEntry(ItemFleSub.a("ramie_fiber"), 1)));
		millet.setSeed(ItemFleSeed.a("millet")).setHaverstDrop(1, FleEntry.asMap(new FleEntry(ItemFleSub.a("millet"), 1)));
		wheat.setSeed(ItemFleSeed.a("wheat")).setHaverstDrop(2, FleEntry.asMap(new FleEntry(new ItemStack(Items.wheat), 1)));
		suger_cances.setSeed(ItemFleSeed.a("suger_cances")).setHaverstDrop(3, FleEntry.asMap(new FleEntry(ItemFleSeed.a("suger_cances"), 1)));
		cotton.setSeed(ItemFleSeed.a("cotton")).setHaverstDrop(2, FleEntry.asMap(new FleEntry(ItemFleSub.a("cotton_rough"), 1)));
		
	}
}