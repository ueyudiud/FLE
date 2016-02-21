package fle.core.item.behavior;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import flapi.enums.EnumFoodType;
import flapi.item.ItemFleFood;
import flapi.item.interfaces.IFoodStat;

public abstract class FoodBase implements IFoodStat<ItemFleFood>
{
	protected EnumFoodType type;
	
	public FoodBase(EnumFoodType aType)
	{
		type = aType;
	}

	public abstract int getFoodLevel(ItemFleFood item, ItemStack itemstack,
			EntityPlayer player);

	public abstract float getSaturation(ItemFleFood item, ItemStack itemstack,
			EntityPlayer player);

	@Override
	public int getEatTick(ItemFleFood item, ItemStack itemstack)
	{
		return type.getEatTick();
	}
	
	boolean isUnnormalFood = false;

	protected void setUnnormal()
	{
		isUnnormalFood = true;
	}
	
	protected void setDrink()
	{
		isDrink = true;
	}
	
	@Override
	public boolean alwaysEdible(ItemFleFood item, ItemStack itemstack,
			EntityPlayer player)
	{
		return isUnnormalFood;
	}

	@Override
	public boolean isRotten(ItemFleFood item, ItemStack itemstack,
			EntityPlayer player)
	{
		return false;
	}

	boolean isDrink = false;
	
	@Override
	public EnumAction getFoodAction(ItemFleFood item, ItemStack itemstack)
	{
		return isDrink ? EnumAction.drink : EnumAction.eat;
	}

	@Override
	public void onEaten(ItemFleFood item, ItemStack itemstack,
			EntityPlayer player)
	{
		itemstack.stackSize--;
	}	
}