/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.handler;

import farcore.data.M;
import farcore.data.MC;
import farcore.lib.item.ItemMulti;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ueyudiud
 */
public class FarCoreBlockHandler
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onBlockHarvestDrop(HarvestDropsEvent event)
	{
		if (event.getState().getBlock() == Blocks.GRAVEL)
		{
			event.getDrops().clear();
			event.getDrops().add(ItemMulti.createStack(M.gravel, MC.pile, 4));
		}
	}
}