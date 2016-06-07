package fle.core.item.resource;

import static farcore.util.NutritionInfo.make;

import farcore.enums.EnumFoodType;
import farcore.enums.EnumItem;
import farcore.enums.EnumNutrition;
import farcore.interfaces.item.IFoodStat;
import farcore.interfaces.item.IItemInfo;
import fle.api.item.ItemDivideSmeltableFood;
import fle.api.item.food.FoodStatStandard;
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
		addSubItem(1, "beef_raw", "Raw Beef", "beef_raw", new FoodStatStandard(make().apply(120, 15F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(2, "chicken_raw", "Raw Chicken", "chicken_raw", new FoodStatStandard(make().apply(80, 15F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(3, "fish_raw", "Raw Fish", "fish_raw", new FoodStatStandard(make().apply(80, 15F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(4, "pork_raw", "Raw Pork", "pork_raw", new FoodStatStandard(make().apply(120, 15F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(5, "squid_raw", "Raw Squid", "squid_raw", new FoodStatStandard(make().apply(70, 15F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(6, "lamp_raw", "Raw Lamp", "lamp_raw", new FoodStatStandard(make().apply(100, 15F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 2.8F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(101, "beef", "Beef", "beef", new FoodStatStandard(make().apply(400, 600F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 4.0F).apply(EnumNutrition.Fat, 0.5F)));
		addSubItem(102, "chicken", "Chicken", "chicken", new FoodStatStandard(make().apply(280, 300F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 4.0F).apply(EnumNutrition.Fat, 0.3F)));
		addSubItem(103, "fish", "Fish", "fish", new FoodStatStandard(make().apply(280, 150F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 3.5F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(104, "pork", "Pork", "pork", new FoodStatStandard(make().apply(400, 550F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 4.0F).apply(EnumNutrition.Fat, 1.0F)));
		addSubItem(105, "squid", "Squid", "squid", new FoodStatStandard(make().apply(240, 120F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 3.2F).apply(EnumNutrition.Fat, 0.2F)));
		addSubItem(106, "lamp", "Lamp", "lamp", new FoodStatStandard(make().apply(300, 250F).apply(EnumFoodType.Meat).apply(EnumNutrition.Protein, 3.5F).apply(EnumNutrition.Fat, 0.3F)));
	}
		
	@Override
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, String iconName, IFoodStat stat)
	{
		super.addSubItem(id, name, local, itemInfo, "fle:resource/food/beta/" + iconName, stat);
	}
}