package fla.core.tool;

import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerEvent.HarvestCheck;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fla.api.item.tool.IDigableTool;
import fla.api.item.tool.IStoneHammer;

public class ToolManager 
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void getToolDigSpeed(BreakSpeed evt)
	{
		if(evt.entityPlayer != null)
			if(evt.entityPlayer.getCurrentEquippedItem() != null)
				if(evt.entityPlayer.getCurrentEquippedItem().getItem() instanceof IDigableTool)
				{
					IDigableTool tool = (IDigableTool) evt.entityPlayer.getCurrentEquippedItem().getItem();
					if(tool.isToolEffective(evt.entityPlayer.getCurrentEquippedItem(), evt.block, evt.metadata))
					{
						evt.newSpeed = tool.getToolDigSpeed(evt.entityPlayer.getCurrentEquippedItem(), evt.block, evt.metadata);
					}
					else
					{
						evt.newSpeed = 1.0F;
					}
				}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void setCanHarvest(HarvestCheck evt)
	{
		if(evt.entityPlayer != null)
			if(evt.entityPlayer.getCurrentEquippedItem() != null)
				if(evt.entityPlayer.getCurrentEquippedItem().getItem() instanceof IDigableTool)
				{
					evt.success = true;
				}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
	public void canHarvestBlock(HarvestDropsEvent evt) 
	{
		if(evt.harvester != null)
			if(evt.harvester.getCurrentEquippedItem() != null)
				if(evt.harvester.getCurrentEquippedItem().getItem() instanceof IDigableTool)
				{
					IDigableTool tool = (IDigableTool) evt.harvester.getCurrentEquippedItem().getItem();
					if(tool.canToolHarvestBlock(evt.harvester.getCurrentEquippedItem(), evt.block, evt.blockMetadata) || evt.block.canHarvestBlock(evt.harvester, evt.blockMetadata))
					{
						if(tool instanceof IStoneHammer)
							if(((IStoneHammer) tool).canCrushBlock(evt.block, evt.blockMetadata))
							{
								StoneHammerManager.onBlockCrush(evt);
								return;
							}
						evt.drops.clear();
						evt.drops.addAll(evt.block.getDrops(evt.world, evt.x, evt.y, evt.z, evt.blockMetadata, evt.fortuneLevel));
					}
				}
	}
	
	

}
