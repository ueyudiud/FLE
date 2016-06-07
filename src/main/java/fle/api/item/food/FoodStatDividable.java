package fle.api.item.food;

import farcore.util.NutritionInfo;
import fle.api.item.ItemDividableFood;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class FoodStatDividable extends FoodStatStandard
{
	public FoodStatDividable(NutritionInfo info)
	{
		super(info);
	}
	public FoodStatDividable(NutritionInfo info, int tick, boolean alwaysEatable)
	{
		super(info, tick, alwaysEatable);
	}
	
	@Override
	public NutritionInfo getFoodNutritionInfo(ItemStack stack, EntityPlayer player)
	{
		float amount = ItemDividableFood.getFoodAmount(stack);
		if(amount > 1F)
		{
			amount = 1F;
		}
		return super.getFoodNutritionInfo(stack, player).multiply(amount / 16F);
	}

	@Override
	public void onEaten(ItemStack stack, EntityPlayer player)
	{
		ItemDividableFood.useAndDecrFood(stack, 1.0F);
	}
}