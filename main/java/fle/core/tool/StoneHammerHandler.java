package fle.core.tool;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fle.api.item.ICrushableTool;
import fle.api.recipe.ItemAbstractStack;
import fle.api.util.DropInfo;

public class StoneHammerHandler
{
	private static final Map<ItemAbstractStack, DropInfo> dustList = new HashMap();
	
	public static void registryDust(ItemAbstractStack c, DropInfo info)
	{
		dustList.put(c, info);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onUsedAxe(HarvestDropsEvent evt)
	{
		if (evt.harvester != null)
			if(!evt.harvester.capabilities.isCreativeMode)
				if(evt.harvester.getCurrentEquippedItem() != null)
					if(evt.harvester.getCurrentEquippedItem().getItem() instanceof ICrushableTool)
					{
						if(((ICrushableTool) evt.harvester.getCurrentEquippedItem().getItem()).doCrush(evt.harvester.getCurrentEquippedItem()))
						{
							if(canBlockCrush(evt.block, evt.blockMetadata))
							{
								onBlockCrush(evt);
								return;
							}
						}
					}
	}
	
	public static void onBlockCrush(HarvestDropsEvent evt)
	{
		DropInfo info = getDropInfo(evt.block, evt.blockMetadata);
		if(info != null)
		{
			evt.drops.clear();
			evt.drops.addAll(info.getDrops());
			return;
		}
	}
	
	private static DropInfo getDropInfo(Block block, int meta)
	{
		for(ItemAbstractStack tCheck : dustList.keySet())
		{
			if(tCheck.isStackEqul(new ItemStack(block, 1, meta)))
			{
				return dustList.get(tCheck);
			}
		}
		return null;
	}

	public static boolean canBlockCrush(Block block, int meta) 
	{
		for(ItemAbstractStack tCheck : dustList.keySet())
		{
			if(tCheck.isStackEqul(new ItemStack(block, 1, meta)))
			{
				return true;
			}
		}
		return false;
	}
}