package fle.core.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import flapi.collection.CollectionUtil;
import flapi.collection.CollectionUtil.FleEntry;
import flapi.plant.TreeInfo;
import fle.core.block.plant.PlantBase;
import fle.core.block.plant.PlantBristleGrass;
import fle.core.block.plant.PlantDandelion;
import fle.core.item.ItemFleSeed;
import fle.core.item.ItemFleSub;
import fle.resource.block.BlockFleLog;
import fle.resource.crop.CropBase;
import fle.resource.crop.CropCotton;
import fle.resource.crop.CropFlax;
import fle.resource.crop.CropMillet;
import fle.resource.crop.CropPotato;
import fle.resource.crop.CropRamie;
import fle.resource.crop.CropSoybean;
import fle.resource.crop.CropSugerCances;
import fle.resource.crop.CropSweetPotato;
import fle.resource.crop.CropWheat;
import fle.resource.tree.TreeBeech;
import fle.resource.tree.TreeCitron;
import fle.resource.tree.TreeSisal;

public class Plants
{
	public static CropBase soybean;
	public static CropBase ramie;
	public static CropBase millet;
	public static CropBase wheat;
	public static CropBase sugar_cances;
	public static CropBase cotton;
	public static CropBase sweet_potato;
	public static CropBase potato;
	public static CropBase flax;
	
	public static PlantBase bristlegrass;
	public static PlantBase dandelion;

	public static TreeInfo citron;
	public static TreeInfo sisal;
	public static TreeInfo beech;
	
	public static void init()
	{
		soybean = new CropSoybean();
		ramie = new CropRamie();
		millet = new CropMillet();
		wheat = new CropWheat();
		sugar_cances = new CropSugerCances();
		cotton = new CropCotton();
		sweet_potato = new CropSweetPotato();
		potato = new CropPotato();
		flax = new CropFlax();
		
		bristlegrass = new PlantBristleGrass();
		dandelion = new PlantDandelion();
		citron = new TreeCitron();
		sisal = new TreeSisal();
		beech = new TreeBeech();
		BlockFleLog.register(citron);
		BlockFleLog.register(sisal);
		BlockFleLog.register(beech);
	}

	public static void postInit()
	{		
		soybean.setSeed(ItemFleSeed.a("soybean")).setHaverstDrop(3, CollectionUtil.asMap(new FleEntry(ItemFleSeed.a("soybean"), 1)));
		ramie.setSeed(ItemFleSeed.a("ramie")).setHaverstDrop(2, CollectionUtil.asMap(new FleEntry(ItemFleSub.a("ramie_fiber"), 1)));
		millet.setSeed(ItemFleSeed.a("millet")).setHaverstDrop(2, CollectionUtil.asMap(new FleEntry(ItemFleSub.a("millet"), 1)));
		wheat.setSeed(ItemFleSeed.a("wheat")).setHaverstDrop(2, CollectionUtil.asMap(new FleEntry(new ItemStack(Items.wheat), 1)));
		sugar_cances.setSeed(ItemFleSeed.a("sugar_cances")).setHaverstDrop(3, CollectionUtil.asMap(new FleEntry(ItemFleSeed.a("sugar_cances"), 1)));
		cotton.setSeed(ItemFleSeed.a("cotton")).setHaverstDrop(2, CollectionUtil.asMap(new FleEntry(ItemFleSub.a("cotton_rough"), 1)));
		sweet_potato.setSeed(ItemFleSeed.a("sweetpotato")).setHaverstDrop(2, CollectionUtil.asMap(new FleEntry(ItemFleSeed.a("sweetpotato"), 1)));
		potato.setSeed(ItemFleSeed.a("potato")).setHaverstDrop(3, CollectionUtil.asMap(new FleEntry(ItemFleSeed.a("potato"), 4), new FleEntry(ItemFleSub.a("sprouted_potato"), 1)));
		flax.setSeed(ItemFleSeed.a("flax")).setHaverstDrop(4, CollectionUtil.asMap(new FleEntry(ItemFleSeed.a("flax"), 4)));
		
		bristlegrass.setHaverstDrop(ItemFleSeed.a("millet"));
		dandelion.setHaverstDrop(ItemFleSub.a("dandelion"));
	}
}