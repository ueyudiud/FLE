package fle.core.item.behavior;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import flapi.enums.EnumFoodType;
import flapi.item.ItemFleFood;

public class FoodStandard extends FoodBase
{
	protected int f;
	protected float s;
	
	public FoodStandard(EnumFoodType aType, int aFoodLevel, float aSaturation)
	{
		super(aType);
		f = aFoodLevel;
		s = aSaturation;
	}

	@Override
	public int getFoodLevel(ItemFleFood item, ItemStack itemstack,
			EntityPlayer player)
	{
		return f;
	}

	@Override
	public float getSaturation(ItemFleFood item, ItemStack itemstack,
			EntityPlayer player)
	{
		return s;
	}
}