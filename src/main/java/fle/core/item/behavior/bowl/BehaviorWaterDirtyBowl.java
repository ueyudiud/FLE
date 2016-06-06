package fle.core.item.behavior.bowl;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class BehaviorWaterDirtyBowl extends BehaviorWaterBowl
{
	@Override
	protected void onPlayerDrink(ItemStack stack, EntityPlayer player)
	{
		if(player.getRNG().nextFloat() < 0.5F)
		{
			player.addPotionEffect(new PotionEffect(Potion.confusion.getId(), 240));
		}
		super.onPlayerDrink(stack, player);
	}
}