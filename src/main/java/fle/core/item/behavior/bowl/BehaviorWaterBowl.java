package fle.core.item.behavior.bowl;

import farcore.enums.EnumItem;
import farcore.util.FarFoodStats;
import farcore.util.U;
import fle.api.item.behavior.BehaviorBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BehaviorWaterBowl extends BehaviorBase
{
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		player.setItemInUse(stack, stack.getMaxItemUseDuration());
		return super.onItemRightClick(stack, world, player);
	}
	
	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
	{
		onPlayerDrink(stack, player);
		U.Inventorys.givePlayer(player, EnumItem.bowl.instance());
		stack.stackSize--;
		return U.Inventorys.valid(stack);
	}
	
	protected void onPlayerDrink(ItemStack stack, EntityPlayer player)
	{
		((FarFoodStats) player.getFoodStats()).addWaterLevel(100F);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 30;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.drink;
	}
}