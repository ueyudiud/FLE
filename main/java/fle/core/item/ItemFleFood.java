package fle.core.item;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import fle.api.enums.EnumFoodType;
import fle.api.item.IFoodStat;
import fle.api.util.FleLog;
import fle.core.init.IB;
import fle.core.item.behavior.BehaviorBase;
import fle.core.item.behavior.FoodBowl;
import fle.core.item.behavior.FoodStandard;
import fle.core.util.TextureLocation;

public class ItemFleFood extends fle.api.item.ItemFleFood
{
	public ItemFleFood init()
	{
		addSubItem(0, "flour", "Flour", "food/flour", new FoodStandard(EnumFoodType.Resource, 1, 0.3F));
		addSubItem(1, "flour_wholemeal", "Wholemeal Flour", "food/wholemeal_flour", new FoodStandard(EnumFoodType.Resource, 1, 0.2F));
		addSubItem(101, "groats_wheat_wholemeal", "Wholemeal Wheat Groats", "food/wholemeal_wheat_groats", new FoodStandard(EnumFoodType.Resource, 1, 0.2F));
		addSubItem(102, "groats_millet_wholemeal", "Wholemeal Millet Groats", "food/wholemeal_millet_groats", new FoodStandard(EnumFoodType.Resource, 1, 0.2F));
		addSubItem(111, "groats_wheat", "Wheat Groats", "food/wheat_groats", new FoodStandard(EnumFoodType.Resource, 1, 0.4F));
		addSubItem(112, "groats_millet", "Millet Groats", "food/millet_groats", new FoodStandard(EnumFoodType.Resource, 1, 0.4F));
		addSubItem(201, "paste", "Paste", "food/paste", new FoodStandard(EnumFoodType.Resource, 1, 0.4F));
		addSubItem(202, "paste_graham", "Graham Paste", "food/graham_paste", new FoodStandard(EnumFoodType.Resource, 1, 0.4F));
		addSubItem(1001, "bread", "Bread", "food/bread", new FoodStandard(EnumFoodType.Snack, 6, 1.0F));
		addSubItem(1002, "bread_graham", "Graham Bread", "food/graham_bread", new FoodStandard(EnumFoodType.Snack, 4, 1.2F));
		addSubItem(1201, "millet_congee", "Millet Congee", "food/millet_congee", new FoodBowl(EnumFoodType.Refection, 9, 1.2F));
		addSubItem(1202, "millet_congee_raw", "Raw Millet Congee", "food/millet_congee_raw", new FoodBowl(EnumFoodType.Refection, 3, 1.0F));
		addSubItem(1203, "millet_congee_rough", "Rough Millet Congee", "food/millet_congee_rough", new FoodBowl(EnumFoodType.Refection, 7, 1.1F));
		addSubItem(1204, "millet_congee_rough_raw", "Raw Rough Millet Congee", "food/millet_congee_rough_raw", new FoodBowl(EnumFoodType.Refection, 3, 1.0F));
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
			IB.subItem.setDamage(ret, meta);
			return ret;
		}
		catch(Throwable e)
		{
			//Use a null item.
			FleLog.getLogger().catching(new RuntimeException("Fle: some mod use empty item id " + name + ", please check your fle-addon "
					+ "had already update, or report this bug to mod editer."));
			return null; //Return null.
		}
	}
	
	public ItemFleFood(String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
	}
	
	public final ItemFleFood addSubItem(int aMetaValue, String name, String aLocalized, String aLocate, IFoodStat<fle.api.item.ItemFleFood> aFoodBehavior)
	{
		addSubItem(aMetaValue, name, aLocalized, new TextureLocation(aLocate), new BehaviorBase(), aFoodBehavior);
		return this;
	}
}