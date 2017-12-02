/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.material.prop;

import nebula.common.item.IFoodStat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

/**
 * @author ueyudiud
 */
public class PropertyEdible implements IFoodStat
{
	public float		foodLevel;
	public float		saturationLevel;
	public float		waterLevel;
	public float[]		nutrAmount;
	public PotionEffect	effect;
	public boolean		wolfEdible	= false;
	
	public PropertyEdible()
	{
	}
	
	public PropertyEdible(float foodLevel, float saturationLevel, float waterLevel, float[] nutrAmount)
	{
		this.foodLevel = foodLevel;
		this.saturationLevel = saturationLevel;
		this.waterLevel = waterLevel;
		this.nutrAmount = nutrAmount;
	}
	
	public PropertyEdible setEffect(PotionEffect effect)
	{
		this.effect = effect;
		return this;
	}
	
	public PropertyEdible setWolfEdible(boolean wolfEdible)
	{
		this.wolfEdible = wolfEdible;
		return this;
	}
	
	@Override
	public float[] getNutritionAmount(ItemStack stack)
	{
		return this.nutrAmount;
	}
	
	@Override
	public float getFoodAmount(ItemStack stack)
	{
		return this.foodLevel;
	}
	
	@Override
	public float getSaturation(ItemStack stack)
	{
		return this.saturationLevel;
	}
	
	@Override
	public float getDrinkAmount(ItemStack stack)
	{
		return this.waterLevel;
	}
	
	@Override
	public boolean isEdible(ItemStack stack, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public boolean isWolfEdible(ItemStack stack)
	{
		return this.wolfEdible;
	}
	
	@Override
	public boolean isDrink(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public int getEatDuration(ItemStack stack)
	{
		return 32;
	}
	
	@Override
	public ItemStack onEat(ItemStack stack, EntityPlayer player)
	{
		stack.stackSize--;
		if (this.effect != null)
		{
			player.addPotionEffect(new PotionEffect(this.effect));
		}
		return stack;
	}
}
