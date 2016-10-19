package farcore.handler;

import farcore.lib.item.IItemBehaviorsAndProperties.IIB_BlockHarvested;
import farcore.lib.item.IItemBehaviorsAndProperties.IIP_DigSpeed;
import farcore.util.U;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FarCoreItemHandler
{
	@SubscribeEvent
	public void getDigSpeed(BreakSpeed event)
	{
		if(event.getEntityPlayer() == null) return;
		ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
		if(stack == null)
		{
			stack = event.getEntityPlayer().getHeldItemOffhand();
		}
		else if(stack.getItem() instanceof IIP_DigSpeed)
		{
			event.setNewSpeed(((IIP_DigSpeed) stack.getItem()).replaceDigSpeed(stack, event));
		}
	}
	
	@SubscribeEvent
	public void onHarvestBlock(HarvestDropsEvent event)
	{
		if(event.getHarvester() == null) return;
		ItemStack stack = event.getHarvester().getHeldItemMainhand();
		if(stack == null)
		{
			stack = event.getHarvester().getHeldItemOffhand();
		}
		if(stack != null && stack.getItem() instanceof IIB_BlockHarvested)
		{
			((IIB_BlockHarvested) stack.getItem()).onBlockHarvested(stack, event);
			U.Players.destoryPlayerCurrentItem(event.getHarvester());
		}
	}
}
