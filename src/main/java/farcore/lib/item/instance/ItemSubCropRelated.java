/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.item.instance;

import farcore.FarCore;
import farcore.data.EnumItem;
import farcore.data.EnumNutrition;
import farcore.data.M;
import farcore.data.MC;
import farcore.lib.item.ItemSubEdible;
import farcore.lib.item.behavior.FoodStatBase;
import farcore.lib.item.behavior.IFoodStat;
import farcore.lib.model.item.FarCoreItemModelLoader;
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
		super(FarCore.ID, "crop.related");
		initalize();
		EnumItem.crop_related.set(this);
	}

	protected void initalize()
	{
		final IFoodStat NULL = null;
		addSubItem(401, "bristlegrass", "Bristlegrass", null, NULL);
		addSubItem(402, "dandelion", "Dandelion", null, NULL);
		addSubItem(403, "rugosa", "A.Rugosa", null, NULL);
		addSubItem(404, "honeysuckle", "Honeysuckle", null, NULL);
		addSubItem(405, "hemlock", "Hemlock", null, NULL);
		addSubItem(406, "nigrum", "S.Nigrum", null, NULL);
		addSubItem(407, "elegans", "G.Elegans", null, NULL);
		addSubItem(408, "fly_agaric", "Fly Agaric", null, new FoodStatBase(1.0F, 0.5F, 0F).setAlwaysEdible().setEffect(new PotionEffect(MobEffects.BLINDNESS, 1000)));
		addSubItem(409, "belladonna", "Belladonna", null, NULL);
		addSubItem(410, "mentha", "Mentha", null, NULL);
		addSubItem(411, "dill", "Dill", null, NULL);
		addSubItem(412, "basil", "Basil", null, NULL);
		addSubItem(414, "white_snakeroot", "White Snakeroot", null, NULL);
		addSubItem(601, "slime_juvenile", "Slime Juvenile", null, NULL);
		addSubItem(1001, "wheat", "Wheat", null, NULL);
		addSubItem(1002, "millet", "Millet", null, NULL);
		addSubItem(1003, "ramie_fiber", "Ramie Fiber", null, NULL);
		addSubItem(1004, "cotton", "Cotton", null, NULL);
		addSubItem(1005, "wild_cabbage_leaf", "Wild Cabbage Leaf", null, new FoodStatBase(0F, 0.5F, 1F).setAlwaysEdible());
		addSubItem(1006, "cabbage", "Cabbage", null, new FoodStatBase(2.0F, 0F, 3F).setNutrition(EnumNutrition.VEGETABLE, 4.0F));
		addSubItem(1007, "cauliflower", "Cauliflower", null, new FoodStatBase(2.5F, 0.0F, 1.5F).setNutrition(EnumNutrition.VEGETABLE, 4.0F));
		addSubItem(1008, "purple_cabbage", "Purple Cabbage", null, new FoodStatBase(2.0F, 0F, 3F).setNutrition(EnumNutrition.VEGETABLE, 5.0F));
	}

	@Override
	public void postInitalizedItems()
	{
		super.postInitalizedItems();
		MC.crop.registerOre(M.wheat, new ItemStack(this, 1, 1001));
		MC.crop.registerOre(M.millet, new ItemStack(this, 1, 1002));
		MC.crop.registerOre(M.ramie, new ItemStack(this, 1, 1003));
		MC.crop.registerOre(M.cotton, new ItemStack(this, 1, 1004));
		MC.crop.registerOre(M.cabbage, new ItemStack(this, 1, 1006));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		FarCoreItemModelLoader.registerModel(this, new ResourceLocation(FarCore.ID, "group/crop.related"));
		FarCoreItemModelLoader.registerMultiIconProvider(getRegistryName(), idMap.keySet(), key -> new ResourceLocation(FarCore.ID, "items/group/crop_related/" + key));
		FarCoreItemModelLoader.registerSubmetaProvider(getRegistryName(), nameMap, this);
	}
}