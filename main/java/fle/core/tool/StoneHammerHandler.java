package fle.core.tool;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent.HarvestCheck;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fle.api.item.ICrushableTool;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.util.DropInfo;
import fle.core.block.BlockRock;
import fle.core.init.Materials;
import fle.core.item.ItemFleSub;
import fle.core.util.FleEntry;

public class StoneHammerHandler
{
	private static final Map<ItemAbstractStack, DropInfo> dustList = new HashMap();
	
	static
	{
		Map<ItemStack, Integer> tMap;
		tMap = FleEntry.asMap(new FleEntry(ItemFleSub.a("stone_a"), 8), new FleEntry(ItemFleSub.a("stone_b"), 3));
		registryDust(new ItemBaseStack(Blocks.stone), new DropInfo(2, 3, FleEntry.copy(tMap)));
		tMap = FleEntry.asMap(new FleEntry(ItemFleSub.a("sandstone"), 1));
		registryDust(new ItemBaseStack(Blocks.sandstone), new DropInfo(2, 3, FleEntry.copy(tMap)));
		tMap = FleEntry.asMap(new FleEntry(ItemFleSub.a("netherstone"), 1));
		registryDust(new ItemBaseStack(Blocks.netherrack), new DropInfo(2, 3, FleEntry.copy(tMap)));
		tMap = FleEntry.asMap(new FleEntry(ItemFleSub.a("chip_obsidian"), 1));
		registryDust(new ItemBaseStack(Blocks.obsidian), new DropInfo(2, 3, FleEntry.copy(tMap)));
		tMap = FleEntry.asMap(new FleEntry(ItemFleSub.a("limestone"), 1));
		registryDust(new ItemBaseStack(BlockRock.a(Materials.Limestone)), new DropInfo(2, 3, FleEntry.copy(tMap)));
	}
	
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
							else
							{
								evt.drops.clear();
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