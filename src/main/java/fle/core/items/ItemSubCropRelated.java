/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.items;

import com.google.common.collect.Maps;

import farcore.data.EnumItem;
import farcore.data.M;
import farcore.data.MC;
import fle.core.FLE;
import fle.loader.Crops;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.common.foodstat.EnumNutrition;
import nebula.common.item.FoodStatBase;
import nebula.common.item.IBehavior;
import nebula.common.item.IFoodStat;
import nebula.common.item.ItemSubEdible;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class ItemSubCropRelated extends ItemSubEdible
{
	public ItemSubCropRelated()
	{
		super(FLE.MODID, "crop_related");
		initalize();
		EnumItem.crop_related.set(this);
	}
	
	protected void initalize()
	{
		final IFoodStat NFS = null;
		addSubItem(201, "vine", "Vine", NFS);
		addSubItem(202, "grass", "Grass Stems", NFS);
		addSubItem(203, "broadleaf", "Broadleaf", NFS);
		addSubItem(204, "coniferous", "Coniferous", NFS);
		addSubItem(401, "bristlegrass", "Bristlegrass", NFS);
		addSubItem(402, "dandelion", "Dandelion", NFS);
		addSubItem(403, "rugosa", "A.Rugosa", NFS);
		addSubItem(404, "honeysuckle", "Honeysuckle", NFS);
		addSubItem(405, "hemlock", "Hemlock", NFS);
		addSubItem(406, "nigrum", "S.Nigrum", NFS);
		addSubItem(407, "elegans", "G.Elegans", NFS);
		addSubItem(408, "fly_agaric", "Fly Agaric", new FoodStatBase(1.0F, 0.5F, 0F).setAlwaysEdible().setEffect(new PotionEffect(MobEffects.BLINDNESS, 1000)));
		addSubItem(409, "belladonna", "Belladonna", NFS);
		addSubItem(410, "mentha", "Mentha", NFS);
		addSubItem(411, "dill", "Dill", NFS);
		addSubItem(412, "basil", "Basil", NFS);
		addSubItem(413, "white_snakeroot", "White Snakeroot", NFS);
		addSubItem(601, "slime_juvenile", "Slime Juvenile", NFS);
		addSubItem(1001, "wheat", "Wheat", NFS);
		addSubItem(1002, "millet", "Millet", NFS);
		addSubItem(1003, "ramie_fiber", "Ramie Fiber", NFS);
		addSubItem(1004, "cotton", "Cotton", NFS);
		addSubItem(1005, "wild_cabbage_leaf", "Wild Cabbage Leaf", new FoodStatBase(0.0F, 0.5F, 1.0F).setAlwaysEdible());
		addSubItem(1006, "cabbage", "Cabbage", new FoodStatBase(2.0F, 0.0F, 3.0F).setNutrition(EnumNutrition.VEGETABLE, 4.0F));
		addSubItem(1007, "cauliflower", "Cauliflower", new FoodStatBase(2.5F, 0.0F, 1.5F).setNutrition(EnumNutrition.VEGETABLE, 4.0F));
		addSubItem(1008, "purple_cabbage", "Purple Cabbage", new FoodStatBase(2.0F, 0.0F, 3.0F).setNutrition(EnumNutrition.VEGETABLE, 5.0F));
		addSubItem(1009, "soybean_pod", "Soybean Pod", new FoodStatBase(2.0F, 0.5F, 1.0F).setNutrition(EnumNutrition.VEGETABLE, 1.0F).setNutrition(EnumNutrition.PROTEIN, 2.0F));
		addSubItem(1010, "brussels_sprouts", "Brussels Sprouts", new FoodStatBase(2.0F, 0.0F, 2.0F).setNutrition(EnumNutrition.VEGETABLE, 4.0F));
		addSubItem(1011, "rutabaga", "Rutabaga", new FoodStatBase(2.0F, 0.0F, 1.5F).setNutrition(EnumNutrition.VEGETABLE, 4.0F));
		addSubItem(1012, "citrus", "Citrus", new FoodStatBase(1.0F, 0.5F, 2.0F).setNutrition(EnumNutrition.FRUIT, 3.0F));
		addSubItem(1013, "bitter_orange", "Bitter Orange", new FoodStatBase(1.0F, 0.5F, 2.5F).setNutrition(EnumNutrition.FRUIT, 3.0F));
		addSubItem(1014, "lemon", "Lemon", new FoodStatBase(1.0F, 0.5F, 2.0F).setNutrition(EnumNutrition.FRUIT, 4.0F));
		addSubItem(1015, "tangerine", "Tangerine", new FoodStatBase(1.0F, 0.5F, 2.0F).setNutrition(EnumNutrition.FRUIT, 4.0F));
		addSubItem(1016, "pomelo", "Pomelo", new FoodStatBase(1.0F, 0.5F, 2.0F).setNutrition(EnumNutrition.FRUIT, 4.0F));
		addSubItem(1017, "lime", "Lime", new FoodStatBase(1.0F, 0.5F, 2.0F).setNutrition(EnumNutrition.FRUIT, 4.0F));
		addSubItem(1018, "orange", "Orange", new FoodStatBase(1.0F, 0.5F, 3.0F).setNutrition(EnumNutrition.FRUIT, 4.5F));
		addSubItem(1019, "grapefruit", "Grapefruit", new FoodStatBase(1.0F, 0.5F, 2.5F).setNutrition(EnumNutrition.FRUIT, 4.5F));
		addSubItem(1020, "apple", "Apple", new FoodStatBase(1.0F, 0.5F, 1.0F).setNutrition(EnumNutrition.FRUIT, 2.5F));
		addSubItem(1021, "green_apple", "Green Apple", new FoodStatBase(1.0F, 0.5F, 1.0F).setNutrition(EnumNutrition.FRUIT, 2.5F));
		addSubItem(1022, "wild_apple", "Wild Apple", new FoodStatBase(0.5F, 0.0F, 1.0F).setNutrition(EnumNutrition.FRUIT, 1.5F));
		addSubItem(1101, "acorn", "Acorn");
		addSubItem(1501, "parsley", "Parsley", NFS);
		addSubItem(1502, "sage", "Sage", NFS);
		addSubItem(1503, "rosemary", "Rosemary", NFS);
		addSubItem(1504, "thyme", "Thyme", NFS);
		addSubItem(1505, "star_anise", "Star Anise", NFS);
		addSubItem(1506, "clove", "Clove", NFS);
		addSubItem(1507, "black_pepper", "Black Pepper", NFS);
		addSubItem(1508, "green_pepper", "Green Pepper", NFS);
		addSubItem(1509, "ginger", "Ginger", NFS);
		addSubItem(1510, "turmeric_rhizome", "Turmeric Rhizome", NFS);
		addSubItem(1511, "nutmeg", "Nutmeg", NFS);
		addSubItem(1512, "cinnamon", "Cinnamon", NFS);
		addSubItem(1513, "fennel_seed", "Fennel Seed", NFS);
		addSubItem(1514, "cumin_seed", "Cumin Seed", NFS);
		addSubItem(1515, "prickly_ash", "Prickly Ash", NFS);
		addSubItem(2001, "plant_waste", "Plant Waste", NFS);
	}
	
	@Override
	public void addSubItem(int id, String name, String localName, IFoodStat stat, IBehavior...behaviors)
	{
		super.addSubItem(id, name, localName, stat, behaviors);
	}
	
	@Override
	public void postInitalizedItems()
	{
		super.postInitalizedItems();
		MC.crop.registerOre(Crops.wheat, new ItemStack(this, 1, 1001));
		MC.crop.registerOre(Crops.millet, new ItemStack(this, 1, 1002));
		MC.crop.registerOre(Crops.ramie, new ItemStack(this, 1, 1003));
		MC.crop.registerOre(Crops.cotton, new ItemStack(this, 1, 1004));
		MC.crop.registerOre(Crops.cabbage, new ItemStack(this, 1, 1006));
		MC.crop.registerOre(Crops.soybean, new ItemStack(this, 1, 1009));
		MC.crop.registerOre(Crops.rutabaga, new ItemStack(this, 1, 1011));
		MC.crop.registerOre(M.citrus, new ItemStack(this, 1, 1012));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		NebulaModelLoader.registerModel(this, new ResourceLocation(FLE.MODID, "group/crop_related"));
		NebulaModelLoader.registerTextureSet(getRegistryName(), () -> Maps.asMap(this.idMap.keySet(), key -> new ResourceLocation(FLE.MODID, "items/group/crop_related/" + key)));
		NebulaModelLoader.registerItemMetaGenerator(getRegistryName(), stack -> this.nameMap.get(getBaseDamage(stack)));
	}
}
