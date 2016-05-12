package farcore.interfaces.item;

import farcore.util.NutritionInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

public interface IFoodStat
{
	NutritionInfo getFoodNutritionInfo(ItemStack stack, EntityPlayer player);
	
	int getEatTick(ItemStack stack);
	
	boolean alwaysEdible(ItemStack stack, EntityPlayer player);
	
	boolean isRotten(ItemStack stack, EntityPlayer player);
	  
	EnumAction getFoodAction(ItemStack stack);
	
	void onEaten(ItemStack stack, EntityPlayer player);
}