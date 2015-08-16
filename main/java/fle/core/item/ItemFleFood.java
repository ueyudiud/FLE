package fle.core.item;

import fle.api.enums.EnumFoodType;
import fle.api.item.IFoodStat;
import fle.core.item.behavior.BehaviorBase;
import fle.core.item.behavior.FoodBowl;
import fle.core.item.behavior.FoodStandard;
import fle.core.util.TextureLocation;

public class ItemFleFood extends fle.api.item.ItemFleFood
{
	public ItemFleFood init()
	{
		addSubItem(0, "flour", "food/flour", new FoodStandard(EnumFoodType.Resource, 1, 0.3F));
		addSubItem(1, "flour_wholemeal", "food/wholemeal_flour", new FoodStandard(EnumFoodType.Resource, 1, 0.2F));
		addSubItem(101, "groats_wheat_wholemeal", "food/wholemeal_wheat_groats", new FoodStandard(EnumFoodType.Resource, 1, 0.6F));
		addSubItem(102, "groats_millet_wholemeal", "food/wholemeal_millet_groats", new FoodStandard(EnumFoodType.Resource, 1, 0.6F));
		addSubItem(111, "groats_wheat", "food/wheat_groats", new FoodStandard(EnumFoodType.Resource, 1, 0.7F));
		addSubItem(112, "groats_millet", "food/millet_groats", new FoodStandard(EnumFoodType.Resource, 1, 0.7F));
		addSubItem(201, "paste", "food/paste", new FoodStandard(EnumFoodType.Resource, 1, 0.4F));
		addSubItem(202, "paste_graham", "food/graham_paste", new FoodStandard(EnumFoodType.Resource, 1, 0.4F));
		addSubItem(1001, "bread", "food/bread", new FoodStandard(EnumFoodType.Snack, 6, 1.0F));
		addSubItem(1002, "bread_graham", "food/graham_bread", new FoodStandard(EnumFoodType.Snack, 4, 1.2F));
		addSubItem(1201, "millet_congee", "food/millet_congee", new FoodBowl(EnumFoodType.Refection, 9, 1.2F));
		addSubItem(1202, "millet_congee_raw", "food/millet_congee_raw", new FoodBowl(EnumFoodType.Refection, 3, 1.0F));
		addSubItem(1203, "millet_congee_rough", "food/millet_congee_rough", new FoodBowl(EnumFoodType.Refection, 7, 1.1F));
		addSubItem(1204, "millet_congee_rough_raw", "food/millet_congee_rough_raw", new FoodBowl(EnumFoodType.Refection, 3, 1.0F));
		return this;
	}
	
	public ItemFleFood(String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
	}
	
	public final ItemFleFood addSubItem(int aMetaValue, String name, String aLocate, IFoodStat<fle.api.item.ItemFleFood> aFoodBehavior)
	{
		addSubItem(aMetaValue, name, new TextureLocation(aLocate), new BehaviorBase(), aFoodBehavior);
		return this;
	}
}