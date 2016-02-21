package fle.core.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import farcore.collection.CollectionUtil;
import farcore.collection.CollectionUtil.FleEntry;
import flapi.enums.EnumWorldNBT;
import flapi.item.interfaces.ICrushableTool;
import flapi.material.MaterialOre;
import flapi.recipe.DropInfo;
import flapi.recipe.stack.AbstractStack;
import flapi.recipe.stack.BaseStack;
import flapi.world.BlockPos;
import fle.FLE;
import fle.core.init.IB;
import fle.core.init.Materials;
import fle.core.item.ItemFleSub;
import fle.resource.item.ItemOre;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class StoneHammerHandler
{
	private static final Map<AbstractStack, DropInfo> dustList = new HashMap();
	
	public static void init()
	{
		Map<ItemStack, Integer> tMap;
		tMap = CollectionUtil.asMap(new FleEntry(ItemFleSub.a("chip_stone"), 8), new FleEntry(ItemFleSub.a("fragment_stone"), 3));
		registryDust(new BaseStack(Blocks.stone), new DropInfo(3, 4, CollectionUtil.copy(tMap)));
		tMap = CollectionUtil.asMap(new FleEntry(ItemFleSub.a("chip_sandstone"), 1));
		registryDust(new BaseStack(Blocks.sandstone), new DropInfo(3, 4, CollectionUtil.copy(tMap)));
		tMap = CollectionUtil.asMap(new FleEntry(ItemFleSub.a("chip_netherstone"), 1));
		registryDust(new BaseStack(Blocks.netherrack), new DropInfo(3, 4, CollectionUtil.copy(tMap)));
		tMap = CollectionUtil.asMap(new FleEntry(ItemFleSub.a("chip_obsidian"), 1));
		registryDust(new BaseStack(Blocks.obsidian), new DropInfo(2, 3, CollectionUtil.copy(tMap)));
		tMap = CollectionUtil.asMap(new FleEntry(ItemFleSub.a("chip_limestone"), 1));
//		registryDust(new BaseStack(BlockRock.a(Materials.Limestone)), new DropInfo(3, 4, CollectionUtil.copy(tMap)));
//		tMap = CollectionUtil.asMap(new FleEntry(ItemFleSub.a("chip_rhyolite"), 1));
//		registryDust(new BaseStack(BlockRock.a(Materials.Rhyolite)), new DropInfo(3, 4, CollectionUtil.copy(tMap)));
//		tMap = CollectionUtil.asMap(new FleEntry(ItemFleSub.a("chip_andesite"), 1));
//		registryDust(new BaseStack(BlockRock.a(Materials.Andesite)), new DropInfo(3, 4, CollectionUtil.copy(tMap)));
//		tMap = CollectionUtil.asMap(new FleEntry(ItemFleSub.a("chip_basalt"), 1));
//		registryDust(new BaseStack(BlockRock.a(Materials.Basalt)), new DropInfo(3, 4, CollectionUtil.copy(tMap)));
//		tMap = CollectionUtil.asMap(new FleEntry(ItemFleSub.a("chip_peridotite"), 1));
//		registryDust(new BaseStack(BlockRock.a(Materials.Peridotite)), new DropInfo(3, 4, CollectionUtil.copy(tMap)));
//		for(MaterialOre ore : MaterialOre.getOres())
		{
//			tMap = CollectionUtil.asMap(new FleEntry(ItemOre.a(ore), 1));
//			registryDust(new BaseStack(IB.ore, MaterialOre.getOreID(ore)), new DropInfo(2, 2, CollectionUtil.copy(tMap)));
		}
	}
	
	public static void registryDust(AbstractStack c, DropInfo info)
	{
		dustList.put(c, info);
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onHaevest(BreakEvent evt)
	{
		if (evt.getPlayer() != null && !evt.world.isRemote)
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
								int data = FLE.fle.getWorldManager().getData(new BlockPos(evt.world, evt.x, evt.y, evt.z), EnumWorldNBT.Metadata);
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
		for(AbstractStack tCheck : dustList.keySet())
		{
			if(tCheck.similar(new ItemStack(block, 1, meta)))
			{
				return dustList.get(tCheck);
			}
		}
		return null;
	}

	public static boolean canBlockCrush(Block block, int meta) 
	{
		for(AbstractStack tCheck : dustList.keySet())
		{
			if(tCheck.similar(new ItemStack(block, 1, meta)))
			{
				return true;
			}
		}
		return false;
	}
}