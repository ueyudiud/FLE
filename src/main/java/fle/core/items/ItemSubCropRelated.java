/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.items;

import com.google.common.collect.Maps;

import farcore.data.EnumItem;
import farcore.data.MC;
import fle.core.FLE;
import fle.loader.Crops;
import nebula.client.model.FlexibleItemSubmetaGetterLoader;
import nebula.client.model.FlexibleTextureSet;
import nebula.client.model.NebulaItemModelLoader;
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
	}
	
	public void addSubItem(int id, String name, String localName, IFoodStat stat, IBehavior... behaviors)
	{
		super.addSubItem(id, name, localName, null, stat, behaviors);
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
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		NebulaItemModelLoader.registerModel(this, new ResourceLocation(FLE.MODID, "group/crop_related"));
		FlexibleTextureSet.registerTextureSetApplier(getRegistryName(), () -> Maps.asMap(this.idMap.keySet(), key -> new ResourceLocation(FLE.MODID, "items/group/crop_related/" + key)));
		FlexibleItemSubmetaGetterLoader.registerSubmetaGetter(getRegistryName(), stack -> this.nameMap.get(getBaseDamage(stack)));
	}
}