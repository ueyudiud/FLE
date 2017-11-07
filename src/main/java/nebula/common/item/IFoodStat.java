/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.item;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public interface IFoodStat
{
	IFoodStat NO_EATABLE = new IFoodStat()
	{
		@Override
		public ItemStack onEat(ItemStack stack, EntityPlayer player)
		{
			stack.stackSize--;
			return stack;
		}
		
		@Override
		public boolean isWolfEdible(ItemStack stack)
		{
			return false;
		}
		
		@Override
		public boolean isEdible(ItemStack stack, EntityPlayer player)
		{
			return false;
		}
		
		@Override
		public boolean isDrink(ItemStack stack)
		{
			return false;
		}
		
		@Override
		public float getSaturation(ItemStack stack)
		{
			return 0;
		}
		
		@Override
		public float[] getNutritionAmount(ItemStack stack)
		{
			return null;
		}
		
		@Override
		public float getFoodAmount(ItemStack stack)
		{
			return 0;
		}
		
		@Override
		public float getDrinkAmount(ItemStack stack)
		{
			return 0;
		}
		
		@Override
		public int getEatDuration(ItemStack stack)
		{
			return 0;
		}
	};
	
	@Nullable
	float[] getNutritionAmount(ItemStack stack);
	
	float getFoodAmount(ItemStack stack);
	
	float getSaturation(ItemStack stack);
	
	float getDrinkAmount(ItemStack stack);
	
	boolean isEdible(ItemStack stack, EntityPlayer player);
	
	boolean isWolfEdible(ItemStack stack);
	
	boolean isDrink(ItemStack stack);
	
	int getEatDuration(ItemStack stack);
	
	@Nullable
	ItemStack onEat(ItemStack stack, EntityPlayer player);
}
