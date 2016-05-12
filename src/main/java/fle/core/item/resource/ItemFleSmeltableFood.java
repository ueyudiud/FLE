package fle.core.item.resource;

import static farcore.util.NutritionInfo.make;

import farcore.enums.EnumFoodType;
import farcore.enums.EnumItem;
import farcore.enums.EnumNutrition;
import farcore.interfaces.item.IFoodStat;
import farcore.interfaces.item.IItemInfo;
import fle.api.item.ItemSmeltableFood;
import fle.api.item.food.FoodStatKebab;
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
		addSubItem(201, "beef_kebab_raw", "Raw Beef Kebab", "beef_kebab_raw", new FoodStatKebab(make().apply(120, 15F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(202, "chicken_kebab_raw", "Raw Chicken Kebab", "chicken_kebab_raw", new FoodStatKebab(make().apply(80, 15F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(203, "fish_kebab_raw", "Raw Fish Kebab", "fish_kebab_raw", new FoodStatKebab(make().apply(80, 15F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(204, "pork_kebab_raw", "Raw Pork Kebab", "pork_kebab_raw", new FoodStatKebab(make().apply(120, 15F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(205, "squid_kebab_raw", "Raw Squid Kebab", "squid_kebab_raw", new FoodStatKebab(make().apply(70, 15F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(301, "beef_kebab", "Beef Kebab", "beef_kebab", new FoodStatKebab(make().apply(400, 600F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 4.0F).apply(EnumNutrition.Fat, 0.5F)));
		addSubItem(302, "chicken_kebab", "Chicken Kebab", "chicken_kebab", new FoodStatKebab(make().apply(280, 300F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 4.0F).apply(EnumNutrition.Fat, 0.3F)));
		addSubItem(303, "fish_kebab", "Fish Kebab", "fish_kebab", new FoodStatKebab(make().apply(280, 150F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 3.5F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(304, "pork_kebab", "Pork Kebab", "pork_kebab", new FoodStatKebab(make().apply(400, 550F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 4.0F).apply(EnumNutrition.Fat, 1.0F)));
		addSubItem(305, "squid_kebab", "Squid Kebab", "squid_kebab", new FoodStatKebab(make().apply(240, 120F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 3.2F).apply(EnumNutrition.Fat, 0.2F)));
	}
		
	@Override
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, String iconName, IFoodStat stat)
	{
		super.addSubItem(id, name, local, itemInfo, "fle:resource/food/gamma/" + iconName, stat);
	}
}