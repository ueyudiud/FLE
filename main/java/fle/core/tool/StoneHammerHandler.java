package fle.core.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fle.FLE;
import fle.api.item.ICrushableTool;
import fle.api.material.MaterialOre;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.util.DropInfo;
import fle.api.world.BlockPos;
import fle.core.block.BlockRock;
import fle.core.block.ItemOreCobble;
import fle.core.init.IB;
import fle.core.init.Materials;
import fle.core.item.ItemFleSub;
import fle.core.item.ItemOre;
import fle.core.util.FleEntry;

public class StoneHammerHandler
{
	private static final Map<ItemAbstractStack, DropInfo> dustList = new HashMap();
	
	static
	{
		Map<ItemStack, Integer> tMap;
		tMap = FleEntry.asMap(new FleEntry(ItemFleSub.a("stone_a"), 8), new FleEntry(ItemFleSub.a("stone_b"), 3));
		registryDust(new ItemBaseStack(Blocks.stone), new DropInfo(3, 4, FleEntry.copy(tMap)));
		tMap = FleEntry.asMap(new FleEntry(ItemFleSub.a("sandstone"), 1));
		registryDust(new ItemBaseStack(Blocks.sandstone), new DropInfo(3, 4, FleEntry.copy(tMap)));
		tMap = FleEntry.asMap(new FleEntry(ItemFleSub.a("netherstone"), 1));
		registryDust(new ItemBaseStack(Blocks.netherrack), new DropInfo(3, 4, FleEntry.copy(tMap)));
		tMap = FleEntry.asMap(new FleEntry(ItemFleSub.a("chip_obsidian"), 1));
		registryDust(new ItemBaseStack(Blocks.obsidian), new DropInfo(2, 3, FleEntry.copy(tMap)));
		tMap = FleEntry.asMap(new FleEntry(ItemFleSub.a("limestone"), 1));
		registryDust(new ItemBaseStack(BlockRock.a(Materials.Limestone)), new DropInfo(3, 4, FleEntry.copy(tMap)));
		for(MaterialOre ore : MaterialOre.getOres())
		{
			tMap = FleEntry.asMap(new FleEntry(ItemOre.a(ore), 1));
			registryDust(new ItemBaseStack(IB.ore, MaterialOre.getOreID(ore)), new DropInfo(2, 2, FleEntry.copy(tMap)));
		}
	}
	
	public static void registryDust(ItemAbstractStack c, DropInfo info)
	{
		dustList.put(c, info);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onHaevest(BreakEvent evt)
	{
		if (evt.getPlayer() != null)
			if(!evt.getPlayer().capabilities.isCreativeMode)
				if(evt.getPlayer().getCurrentEquippedItem() != null)
				{
					ItemStack tStack = evt.getPlayer().getCurrentEquippedItem();
					if(tStack.getItem() instanceof ICrushableTool)
					{
						int level = tStack.getItem().getHarvestLevel(tStack, "abstract_hammer");
						if(evt.block.getHarvestLevel(evt.blockMetadata) <= level && evt.block.getHarvestTool(evt.blockMetadata) == "pickaxe")
						{
							if(((ICrushableTool) tStack.getItem()).doCrush(evt.world, evt.x, evt.y, evt.z, evt.getPlayer().getCurrentEquippedItem()))
							{
								int data = FLE.fle.getWorldManager().getData(new BlockPos(evt.world, evt.x, evt.y, evt.z), 0);
								if(canBlockCrush(evt.block, data != 0 ? data : evt.blockMetadata))
								{
									ArrayList<ItemStack> list = onBlockCrush(evt.block, data != 0 ? data : evt.blockMetadata);
									ForgeEventFactory.fireBlockHarvesting(list, evt.world, evt.block, evt.x, evt.y, evt.z, evt.blockMetadata, 0, 1.0F, false, evt.getPlayer());
									for(ItemStack tStack1 : list)
										dropBlockAsItem(evt.world, evt.x, evt.y, evt.z, tStack1.copy());
									return;
								}
							}
						}
					}
				}
	}
	
	public static boolean isHammerItemEffective(Block aBlock, World aWorld, int x, int y, int z, ItemStack aStack)
	{
		return isHammerEffective(aBlock, aBlock.getDamageValue(aWorld, x, y, z), aStack);
	}
	
	public static boolean isHammerEffective(Block aBlock, int meta, ItemStack aTool)
	{
		int level = aTool.getItem().getHarvestLevel(aTool, "abstract_hammer");
		if(level < 0) return false;
		if(aBlock.getHarvestLevel(meta) <= level && aBlock.getHarvestTool(meta) == "pickaxe")
		{
			return canBlockCrush(aBlock, meta);
		}
		return false;
	}
	
	public static void dropBlockAsItem(World aWorld, int x, int y, int z, ItemStack aStack)
    {
        if (!aWorld.isRemote && aWorld.getGameRules().getGameRuleBooleanValue("doTileDrops") && !aWorld.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
        {
            float f = 0.7F;
            double d0 = (double)(aWorld.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(aWorld.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(aWorld.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(aWorld, (double)x + d0, (double)y + d1, (double)z + d2, aStack);
            entityitem.delayBeforeCanPickup = 10;
            aWorld.spawnEntityInWorld(entityitem);
        }
    }
	
	public static ArrayList<ItemStack> onBlockCrush(Block block, int meta)
	{
		DropInfo info = getDropInfo(block, meta);
		if(info != null)
		{
			return new ArrayList(info.getDrops());
		}
		return new ArrayList();
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