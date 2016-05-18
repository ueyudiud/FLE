package fle.core.item.resource;

import static farcore.util.NutritionInfo.make;

import farcore.enums.EnumFoodType;
import farcore.enums.EnumItem;
import farcore.enums.EnumNutrition;
import farcore.interfaces.item.IFoodStat;
import farcore.interfaces.item.IItemInfo;
import fle.api.item.ItemSmeltableFood;
import fle.api.item.food.FoodStatKebab;
import fle.api.item.food.FoodStatStandard;
import net.minecraft.item.ItemStack;

public class ItemFleSmeltableFood extends ItemSmeltableFood
{
	public ItemFleSmeltableFood()
	{
		super("smeltable.food");
		EnumItem.food_smeltable.set(new ItemStack(this));
		init();
	}

	private void init()
	{
		addSubItem(201, "beef_kebab_raw", "Raw Beef Kebab", "beef_kebab_raw", new FoodStatKebab(make().apply(30, 3F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 0.7F).apply(EnumNutrition.Fat, 0.05F)));
		addSubItem(202, "chicken_kebab_raw", "Raw Chicken Kebab", "chicken_kebab_raw", new FoodStatKebab(make().apply(20, 3F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 0.7F).apply(EnumNutrition.Fat, 0.05F)));
		addSubItem(203, "fish_kebab_raw", "Raw Fish Kebab", "fish_kebab_raw", new FoodStatKebab(make().apply(20, 3F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 0.7F).apply(EnumNutrition.Fat, 0.05F)));
		addSubItem(204, "pork_kebab_raw", "Raw Pork Kebab", "pork_kebab_raw", new FoodStatKebab(make().apply(30, 3F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 0.7F).apply(EnumNutrition.Fat, 0.05F)));
		addSubItem(205, "squid_kebab_raw", "Raw Squid Kebab", "squid_kebab_raw", new FoodStatKebab(make().apply(18, 3F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 0.7F).apply(EnumNutrition.Fat, 0.05F)));
		addSubItem(301, "beef_kebab", "Beef Kebab", "beef_kebab", new FoodStatKebab(make().apply(100, 150F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 1.0F).apply(EnumNutrition.Fat, 0.125F)));
		addSubItem(302, "chicken_kebab", "Chicken Kebab", "chicken_kebab", new FoodStatKebab(make().apply(70, 75F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 1.0F).apply(EnumNutrition.Fat, 0.075F)));
		addSubItem(303, "fish_kebab", "Fish Kebab", "fish_kebab", new FoodStatKebab(make().apply(70, 38F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 0.875F).apply(EnumNutrition.Fat, 0.05F)));
		addSubItem(304, "pork_kebab", "Pork Kebab", "pork_kebab", new FoodStatKebab(make().apply(100, 112F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 1.0F).apply(EnumNutrition.Fat, 0.25F)));
		addSubItem(305, "squid_kebab", "Squid Kebab", "squid_kebab", new FoodStatKebab(make().apply(60, 30F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 0.8F).apply(EnumNutrition.Fat, 0.05F)));
	}
		
	@Override
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, String iconName, IFoodStat stat)
	{
		super.addSubItem(id, name, local, itemInfo, "fle:resource/food/gamma/" + iconName, stat);
	}
}