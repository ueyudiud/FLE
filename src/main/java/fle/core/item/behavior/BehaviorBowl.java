package fle.core.item.behavior;

import farcore.item.ItemBase;
import farcore.util.U;
import fle.api.item.BowlEvent.BowlUseEvent;
import fle.api.item.behavior.BehaviorBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BehaviorBowl extends BehaviorBase
{
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		BowlUseEvent event = new BowlUseEvent(player, stack, ((ItemBase) stack.getItem()).getMovingObjectPositionFromPlayer(world, player, true));
		if(event.isCanceled())
		{
			return event.getStack();
		}
		else
		{
			return super.onItemRightClick(event.getStack(), world, player);
		}
	}
}