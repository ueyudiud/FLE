package fle.core.item.resource;

import static farcore.util.NutritionInfo.make;

import farcore.enums.EnumFoodType;
import farcore.enums.EnumItem;
import farcore.enums.EnumNutrition;
import farcore.interfaces.item.IFoodStat;
import farcore.interfaces.item.IItemInfo;
import fle.api.item.ItemDivideSmeltableFood;
import fle.api.item.food.FoodStatDividable;
import net.minecraft.item.ItemStack;

public class ItemFleDividableSmeltableFood extends ItemDivideSmeltableFood
{
	public ItemFleDividableSmeltableFood()
	{
		super("dividable.smeltable.food");
		EnumItem.food_divide_smeltable.set(new ItemStack(this));
		init();
	}

	private void init()
	{
		addSubItem(1, "beef_raw", "Raw Beef", "beef_raw", new FoodStatDividable(make().apply(24, 3F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(2, "chicken_raw", "Raw Chicken", "chicken_raw", new FoodStatDividable(make().apply(16, 3F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(3, "fish_raw", "Raw Fish", "fish_raw", new FoodStatDividable(make().apply(16, 3F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(4, "pork_raw", "Raw Pork", "pork_raw", new FoodStatDividable(make().apply(24, 3F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(5, "squid_raw", "Raw Squid", "squid_raw", new FoodStatDividable(make().apply(14, 3F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(6, "lamb_raw", "Raw Lamb", "lamb_raw", new FoodStatDividable(make().apply(20, 3F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(101, "beef", "Beef", "beef", new FoodStatDividable(make().apply(80, 120F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 4.0F).apply(EnumNutrition.Fat, 0.5F)));
		addSubItem(102, "chicken", "Chicken", "chicken", new FoodStatDividable(make().apply(56, 60F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 4.0F).apply(EnumNutrition.Fat, 0.3F)));
		addSubItem(103, "fish", "Fish", "fish", new FoodStatDividable(make().apply(56, 30F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 3.5F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(104, "pork", "Pork", "pork", new FoodStatDividable(make().apply(80, 110F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 4.0F).apply(EnumNutrition.Fat, 1.0F)));
		addSubItem(105, "squid", "Squid", "squid", new FoodStatDividable(make().apply(48, 24F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 3.2F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(106, "lamb", "Lamb", "lamb", new FoodStatDividable(make().apply(60, 50F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 3.5F).apply(EnumNutrition.Fat, 0.3F)));
	}
		
	@Override
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, String iconName, IFoodStat stat)
	{
		super.addSubItem(id, name, local, itemInfo, "fle:resource/food/beta/" + iconName, stat);
	}
}