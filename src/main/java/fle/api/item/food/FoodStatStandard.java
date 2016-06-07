package fle.api.item.food;

import farcore.interfaces.item.IFoodStat;
import farcore.util.NutritionInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

public class FoodStatStandard implements IFoodStat
{
	private NutritionInfo info;
	private int eatTick;
	private boolean flag;

	public FoodStatStandard(NutritionInfo info)
	{
		this(info, 50, false);
	}
	public FoodStatStandard(NutritionInfo info, int tick, boolean alwaysEatable)
	{
		this.info = info;
		this.eatTick = tick;
		this.flag = alwaysEatable;
	}

	@Override
	public NutritionInfo getFoodNutritionInfo(ItemStack stack, EntityPlayer player)
	{
		return info;
	}

	@Override
	public int getEatTick(ItemStack stack)
	{
		return eatTick;
	}

	@Override
	public boolean alwaysEdible(ItemStack stack, EntityPlayer player)
	{
		return flag;
	}

	@Override
	public boolean isRotten(ItemStack stack, EntityPlayer player)
	{
		return false;
	}

	@Override
	public EnumAction getFoodAction(ItemStack stack)
	{
		return EnumAction.eat;
	}

	@Override
	public void onEaten(ItemStack stack, EntityPlayer player)
	{
		stack.stackSize--;
	}
}