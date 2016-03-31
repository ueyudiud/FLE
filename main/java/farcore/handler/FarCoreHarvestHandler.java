package farcore.handler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import farcore.interfaces.ISmartHarvestBlock;
import farcore.lib.recipe.DropHandler;
import farcore.lib.recipe.ToolDestoryDropRecipes;
import farcore.util.U;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class FarCoreHarvestHandler
{
	@SubscribeEvent
	public void onHarvestCheck(BreakEvent event)
	{
	    if ((event.getPlayer() != null) && 
	    	      (!event.getPlayer().capabilities.isCreativeMode) && 
	    	      (event.getPlayer().getCurrentEquippedItem() != null))
	    {
	    	ItemStack stack = event.getPlayer().getCurrentEquippedItem();
	    	DropHandler info = ToolDestoryDropRecipes.match(stack, event.world, event.x, event.y, event.z, event.block, event.blockMetadata);
	    	if(info != null)
	    	{
	    		event.getPlayer().addExhaustion(0.025F);
	    		U.Worlds.spawnDropsInWorld(event.world, event.x, event.y, event.z, info.randomDropsWithCast(event.world.rand));
	    		return;
	    	}
	    	if(event.block instanceof ISmartHarvestBlock)
	    	{
	    		if(((ISmartHarvestBlock) event.block).canHarvestBlock(event.world, event.x, event.y, event.z, event.blockMetadata, event.getPlayer()))
	    		{
	    			((ISmartHarvestBlock) event.block).harvestAndDropBlock(event.world, event.x, event.y, event.z, event.blockMetadata, event.getPlayer());
	    			return;
	    		}
	    		else
	    		{
	    			event.setCanceled(true);
	    		}
	    	}
	    }
	    if(event.getPlayer() == null || event.getPlayer().capabilities.isCreativeMode) return;
	    if(!U.Player.isToolEffective(event.getPlayer(), event.world, event.x, event.y, event.z, event.block, event.blockMetadata, false))
	    {
	    	event.setCanceled(true);
	    }
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void getBreakSpeed(BreakSpeed event)
	{
		if(event.y != -1)
		{
			if((event.entityPlayer != null) && 
		    	      (!event.entityPlayer.capabilities.isCreativeMode) && 
		    	      (event.entityPlayer.getCurrentEquippedItem() != null))
			{
				float speed = U.Player.getBaseDigspeed(event.entityPlayer, event.entityPlayer.worldObj, event.x, event.y, event.z, event.block, event.metadata);
				if(speed >= 0)
				{
					event.newSpeed = speed;
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void disableClassicCheck(HarvestDropsEvent event)
	{
		if(event.harvester != null && 
				event.harvester.getCurrentEquippedItem() != null)
		{
			DropHandler info = ToolDestoryDropRecipes.match(event.harvester.getCurrentEquippedItem(), event.world, event.x, event.y, event.z, event.block, event.blockMetadata);
	    	if(info != null)
	    	{
	    		event.drops.clear();
	    		event.dropChance = 0F;
	    	}
		}
	}
}