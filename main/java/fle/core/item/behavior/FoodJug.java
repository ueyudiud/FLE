package fle.core.item.behavior;

import flapi.enums.EnumFoodType;
import flapi.item.ItemFleFood;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class FoodJug extends FoodBase
{
	private PotionEffect eft;
	
	public FoodJug(EnumFoodType aType, PotionEffect effect)
	{
		super(aType);
		eft = effect;
	}
	
	@Override
	public void onEaten(ItemFleFood item, ItemStack itemstack,
			EntityPlayer player)
	{
		player.addPotionEffect(new PotionEffect(eft));
		ItemStack tStack = FluidContainerRegistry.drainFluidContainer(itemstack);
//		RecipeHelper.onInputItemStack(player);
		if(!player.inventory.addItemStackToInventory(tStack))
		{
			player.dropPlayerItemWithRandomChoice(tStack, false);
		}
	}

	@Override
	public int getFoodLevel(ItemFleFood item, ItemStack itemstack,
			EntityPlayer player)
	{
		return 0;
	}

	@Override
	public float getSaturation(ItemFleFood item, ItemStack itemstack,
			EntityPlayer player)
	{
		return 0.5F;
	}
}