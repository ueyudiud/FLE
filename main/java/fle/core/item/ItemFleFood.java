package fle.core.item;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import flapi.enums.EnumFoodType;
import flapi.item.interfaces.IFoodStat;
import flapi.util.FleLog;
import fle.core.init.IB;
import fle.core.item.behavior.BehaviorBase;
import fle.core.item.behavior.FoodBowl;
import fle.core.item.behavior.FoodJug;
import fle.core.item.behavior.FoodKebab;
import fle.core.item.behavior.FoodStandard;
import fle.core.util.FlePotionEffect;
import fle.core.util.ItemTextureHandler;

public class ItemFleFood extends flapi.item.ItemFleFood
{
	public ItemFleFood init()
	{
		addSubItem(1, "flour", "Flour", "resource/food/1", new FoodStandard(EnumFoodType.Resource, 1, 0.3F));
		addSubItem(2, "flour_wholemeal", "Wholemeal Flour", "resource/food/2", new FoodStandard(EnumFoodType.Resource, 1, 0.2F));
		addSubItem(101, "groats_wheat_wholemeal", "Wholemeal Wheat Groats", "resource/food/101", new FoodStandard(EnumFoodType.Resource, 1, 0.2F));
		addSubItem(102, "groats_millet_wholemeal", "Wholemeal Millet Groats", "resource/food/102", new FoodStandard(EnumFoodType.Resource, 1, 0.2F));
		addSubItem(111, "groats_wheat", "Wheat Groats", "resource/food/111", new FoodStandard(EnumFoodType.Resource, 1, 0.4F));
		addSubItem(112, "groats_millet", "Millet Groats", "resource/food/112", new FoodStandard(EnumFoodType.Resource, 1, 0.4F));
		addSubItem(201, "paste", "Paste", "resource/food/201", new FoodStandard(EnumFoodType.Resource, 1, 0.4F));
		addSubItem(202, "paste_graham", "Graham Paste", "resource/food/202", new FoodStandard(EnumFoodType.Resource, 1, 0.4F));
		addSubItem(1001, "bread", "Bread", "resource/food/1001", new FoodStandard(EnumFoodType.Snack, 6, 1.0F));
		addSubItem(1002, "bread_graham", "Graham Bread", "resource/food/1002", new FoodStandard(EnumFoodType.Snack, 4, 1.2F));
		addSubItem(1201, "millet_congee", "Millet Congee", "resource/food/1201", new FoodBowl(EnumFoodType.Refection, 9, 1.2F));
		addSubItem(1202, "millet_congee_raw", "Raw Millet Congee", "resource/food/1202", new FoodBowl(EnumFoodType.Refection, 3, 1.0F));
		addSubItem(1203, "millet_congee_rough", "Rough Millet Congee", "resource/food/1203", new FoodBowl(EnumFoodType.Refection, 7, 1.1F));
		addSubItem(1204, "millet_congee_rough_raw", "Raw Rough Millet Congee", "resource/food/1204", new FoodBowl(EnumFoodType.Refection, 3, 1.0F));
		addSubItem(5001, "brown_sugar", "Brown Sugar", "resource/food/5001", new FoodStandard(EnumFoodType.Resource, 1, 0.4F));
		addSubItem(5002, "sugar", "White Sugar", "resource/food/5002", new FoodStandard(EnumFoodType.Resource, 1, 0.6F));
		addSubItem(10001, "chicken_kebab_raw", "Raw Chicken Kebab", "resource/food/10001", new FoodKebab(EnumFoodType.Snack, 2, 0.2F));
		addSubItem(10002, "pork_kebab_raw", "Raw Pork Kebab", "resource/food/10002", new FoodKebab(EnumFoodType.Snack, 3, 0.2F));
		addSubItem(10011, "chicken_kebab", "Chicken Kebab", "resource/food/10011", new FoodKebab(EnumFoodType.Snack, 3, 0.6F));
		addSubItem(10012, "pork_kebab", "Pork Kebab", "resource/food/10012", new FoodKebab(EnumFoodType.Snack, 4, 0.7F));
		addSubItem(20001, "citron", "Citron", "resource/food/20001", new FoodStandard(EnumFoodType.Resource, 1, 0.0F));
		addSubItem(30001, "jug_argil_wheat", "Argil Jug With Wheat Alcohol", "clay/1003/alcohol_wheat", new FoodJug(EnumFoodType.Drink, new PotionEffect(FlePotionEffect.drunk.id, 1000, 1)));
		return this;
	}

	public static ItemStack a(String name)
	{
		return a(name, 1);
	}
	public static ItemStack a(String name, int size)
	{
		try
		{
			int meta = ((ItemFleFood) IB.food).itemBehaviors.serial(name);
			ItemStack ret = new ItemStack(IB.food, size, meta);
			IB.food.setDamage(ret, meta);
			return ret;
		}
		catch(Throwable e)
		{
			//Use a null item.
			FleLog.getLogger().warn("Fle: some mod use empty item id " + name + ", please check your fle-addon "
					+ "had already update, or report this bug to mod editer.");
			return null; //Return null.
		}
	}
	
	public ItemFleFood(String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
	}
	
	public final ItemFleFood addSubItem(int aMetaValue, String name, String aLocalized, String aLocate, IFoodStat<flapi.item.ItemFleFood> aFoodBehavior)
	{
		addSubItem(aMetaValue, name, aLocalized, new ItemTextureHandler(aLocate), new BehaviorBase(), aFoodBehavior);
		return this;
	}
}