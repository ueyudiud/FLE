/*
 * copyright© 2016-2018 ueyudiud
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
	protected boolean		isDrink			= false;
	protected boolean		isWolfEdible	= false;
	protected boolean		isAlwaysEdible	= false;
	protected float[]		nutritionAmt	= null;
	protected int			eatDuration		= 32;
	protected float			foodAmt;
	protected float			saturationAmt;
	protected float			drinkAmt;
	protected PotionEffect	effect;
	
	public FoodStatBase(float foodAmount, float saturationAmount, float drinkAmount)
	{
		this.foodAmt = foodAmount;
		this.saturationAmt = saturationAmount;
		this.drinkAmt = drinkAmount;
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
		if (this.nutritionAmt == null)
		{
			this.nutritionAmt = new float[] { 0, 0, 0, 0, 0, 0 };
		}
		this.nutritionAmt[nutrition.ordinal()] = amount;
		return this;
	}
	
	public FoodStatBase setDrink(boolean isDrink)
	{
		this.isDrink = isDrink;
		return this;
	}
	
	public FoodStatBase setWolfEdible()
	{
		this.isWolfEdible = true;
		return this;
	}
	
	public FoodStatBase setAlwaysEdible()
	{
		this.isAlwaysEdible = true;
		return this;
	}
	
	@Override
	public float[] getNutritionAmount(ItemStack stack)
	{
		return this.nutritionAmt;
	}
	
	@Override
	public float getFoodAmount(ItemStack stack)
	{
		return this.foodAmt;
	}
	
	@Override
	public float getSaturation(ItemStack stack)
	{
		return this.saturationAmt;
	}
	
	@Override
	public float getDrinkAmount(ItemStack stack)
	{
		return this.drinkAmt;
	}
	
	@Override
	public boolean isEdible(ItemStack stack, EntityPlayer player)
	{
		return player.canEat(this.isAlwaysEdible);
	}
	
	@Override
	public boolean isWolfEdible(ItemStack stack)
	{
		return this.isWolfEdible;
	}
	
	@Override
	public boolean isDrink(ItemStack stack)
	{
		return this.isDrink;
	}
	
	@Override
	public int getEatDuration(ItemStack stack)
	{
		return this.eatDuration;
	}
	
	@Override
	public ItemStack onEat(ItemStack stack, EntityPlayer player)
	{
		if (this.effect != null)
		{
			player.addPotionEffect(new PotionEffect(this.effect));
		}
		stack.stackSize--;
		return stack;
	}
}
