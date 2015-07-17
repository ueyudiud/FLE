package fla.core.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import fla.api.recipe.DropInfo;
import fla.api.recipe.IItemChecker.ItemChecker;
import fla.core.item.ItemSub;

public class StoneHammerManager 
{
	private static final List<DropInfo> dustList = new ArrayList();
	
	static
	{
		Map<ItemStack, Integer> map = new HashMap();
		map.put(ItemSub.a("stone_a"), 8);
		map.put(ItemSub.a("stone_b"), 2);
		registryDust(new DropInfo(new ItemChecker(Blocks.stone), map));
	}
	
	public static void registryDust(DropInfo info)
	{
		dustList.add(info);
	}
	
	public static void onBlockCrush(HarvestDropsEvent evt)
	{
		DropInfo info = getDropInfo(evt.block, evt.blockMetadata);
		if(info != null)
		{
			evt.drops.clear();
			evt.drops.addAll(info.getDustDrop());
			return;
		}
	}
	
	private static DropInfo getDropInfo(Block block, int meta)
	{
		for(DropInfo info : dustList)
		{
			if(info.d.match(new ItemStack(block, 1, meta)))
			{
				return info;
			}
		}
		return null;
	}

	public static boolean canBlockCrush(Block block, int meta) 
	{
		for(DropInfo info : dustList)
		{
			if(info.d.match(new ItemStack(block, 1, meta)))
			{
				return true;
			}
		}
		return false;
	}
}