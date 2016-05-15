package fle.api.item.food;

import com.sun.org.apache.bcel.internal.generic.NEW;

import farcore.enums.EnumItem;
import farcore.util.NutritionInfo;
import farcore.util.U;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class FoodStatKebab extends FoodStatStandard
{
	public FoodStatKebab(NutritionInfo info)
	{
		super(info);
	}
	public FoodStatKebab(NutritionInfo info, int tick, boolean alwaysEatable)
	{
		super(info, tick, alwaysEatable);
	}

	@Override
	public void onEaten(ItemStack stack, EntityPlayer player)
	{
		stack.stackSize--;
		U.Inventorys.givePlayer(player, EnumItem.stick.instance(1));
	}
}