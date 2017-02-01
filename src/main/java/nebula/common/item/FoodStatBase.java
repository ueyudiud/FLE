/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.item;

import nebula.common.foodstat.EnumNutrition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

/**
 * @author ueyudiud
 */
public class FoodStatBase implements IFoodStat
{
	protected boolean isDrink = false;
	protected boolean isWolfEdible = false;
	protected boolean isAlwaysEdible = false;
	protected float[] nutritionAmt = null;
	protected int eatDuration = 32;
	protected float foodAmt;
	protected float saturationAmt;
	protected float drinkAmt;
	protected PotionEffect effect;

	public FoodStatBase(float foodAmount, float saturationAmount, float drinkAmount)
	{
		foodAmt = foodAmount;
		saturationAmt = saturationAmount;
		drinkAmt = drinkAmount;
	}
	
	public FoodStatBase setEffect(PotionEffect effect)
	{
		this.effect = effect;
		return this;
	}
	
	public FoodStatBase setNutrition(float[] nutritionAmt)
	{
		this.nutritionAmt = nutritionAmt;
		return this;
	}
	
	public FoodStatBase setNutrition(EnumNutrition nutrition, float amount)
	{
		if(nutritionAmt == null)
		{
			nutritionAmt = new float[]{0, 0, 0, 0, 0, 0};
		}
		nutritionAmt[nutrition.ordinal()] = amount;
		return this;
	}

	public FoodStatBase setDrink(boolean isDrink)
	{
		this.isDrink = isDrink;
		return this;
	}
	
	public FoodStatBase setWolfEdible()
	{
		isWolfEdible = true;
		return this;
	}
	
	public FoodStatBase setAlwaysEdible()
	{
		isAlwaysEdible = true;
		return this;
	}

	@Override
	public float[] getNutritionAmount(ItemStack stack)
	{
		return nutritionAmt;
	}
	
	@Override
	public float getFoodAmount(ItemStack stack)
	{
		return foodAmt;
	}
	
	@Override
	public float getSaturation(ItemStack stack)
	{
		return saturationAmt;
	}
	
	@Override
	public float getDrinkAmount(ItemStack stack)
	{
		return drinkAmt;
	}
	
	@Override
	public boolean isEdible(ItemStack stack, EntityPlayer player)
	{
		return player.canEat(isAlwaysEdible);
	}
	
	@Override
	public boolean isWolfEdible(ItemStack stack)
	{
		return isWolfEdible;
	}
	
	@Override
	public boolean isDrink(ItemStack stack)
	{
		return isDrink;
	}
	
	@Override
	public int getEatDuration(ItemStack stack)
	{
		return eatDuration;
	}
	
	@Override
	public ItemStack onEat(ItemStack stack, EntityPlayer player)
	{
		if(effect != null)
		{
			player.addPotionEffect(new PotionEffect(effect));
		}
		stack.stackSize--;
		return stack;
	}
}