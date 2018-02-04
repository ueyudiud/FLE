/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common;

import nebula.common.item.IItemBehaviorsAndProperties.IIB_BlockHarvested;
import nebula.common.util.Players;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Handler item properties and behaviours.
 * 
 * @author ueyudiud
 *
 */
public class NebulaItemHandler
{
	@SubscribeEvent
	public void onHarvestBlock(HarvestDropsEvent event)
	{
		if (event.getHarvester() == null) return;
		ItemStack stack = event.getHarvester().getHeldItemMainhand();
		if (stack == null)
		{
			stack = event.getHarvester().getHeldItemOffhand();
		}
		if (stack != null && stack.getItem() instanceof IIB_BlockHarvested)
		{
			((IIB_BlockHarvested) stack.getItem()).onBlockHarvested(stack, event);
			Players.destoryPlayerCurrentItem(event.getHarvester());
		}
	}
}
