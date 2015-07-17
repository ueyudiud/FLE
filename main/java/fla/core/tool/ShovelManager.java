package fla.core.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fla.api.recipe.DropInfo;
import fla.api.recipe.IItemChecker;
import fla.api.recipe.IItemChecker.ItemChecker;
import fla.api.world.BlockPos;
import fla.core.item.ItemSub;

public class ShovelManager 
{
	private static final List<DropInfo> dustList = new ArrayList();
	
	static
	{
	}
	
	public static void registryDust(DropInfo info)
	{
		dustList.add(info);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onUsedShovel(HarvestDropsEvent evt)
	{
		BlockPos pos = new BlockPos(evt.world, evt.x, evt.y, evt.z);
		if(evt.block == Blocks.gravel)
		{
			evt.drops.clear();
			evt.drops.add(new ItemStack(Blocks.gravel));
		}
		for(DropInfo info : dustList)
		{
			if(evt.world.rand.nextGaussian() < 0.08D && info.d.match(new ItemStack(evt.block, 1, evt.blockMetadata)))
			{
				evt.drops.clear();
				evt.drops.addAll(info.getDustDrop());
				return;
			}
		}
	}
}