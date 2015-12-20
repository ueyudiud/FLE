package fle.core.item.behavior;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import flapi.enums.EnumFoodType;
import flapi.item.ItemFleFood;

public class FoodBowl extends FoodBase
{
	protected int f;
	protected float s;
	
	public FoodBowl(EnumFoodType aType, int aFoodLevel, float aSaturation)
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
	
	@Override
	public void onEaten(ItemFleFood item, ItemStack itemstack,
			EntityPlayer player)
	{
		super.onEaten(item, itemstack, player);
		ItemStack tStack = new ItemStack(Items.bowl);
		if(!player.inventory.addItemStackToInventory(tStack))
		{
			player.dropPlayerItemWithRandomChoice(tStack, false);
		}
	}
}