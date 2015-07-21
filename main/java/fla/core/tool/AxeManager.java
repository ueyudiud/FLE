package fla.core.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fla.api.item.ITreeLog;
import fla.api.recipe.IItemChecker;
import fla.api.recipe.IItemChecker.ItemChecker;
import fla.api.recipe.IItemChecker.OreChecker;
import fla.api.util.FlaValue;
import fla.api.world.BlockPos;
import fla.core.FlaItems;
import fla.core.item.ItemSub;
import fla.core.util.MathHelper;

public class AxeManager 
{
	private static final List<IItemChecker> axeList = new ArrayList();
	private static final List<TreeInfo> treeList = new ArrayList();
	
	static
	{
		registryAxe(FlaValue.AXE_WOODEN);
		registryAxe(FlaValue.AXE_STONE);
		registryAxe(FlaValue.AXE_IRON);
		registryAxe(FlaValue.AXE_GOLD);
		registryAxe(FlaValue.AXE_DIAMOND);
		registryAxe(new ItemChecker(FlaItems.rough_flint_axe, OreDictionary.WILDCARD_VALUE));
		Map<ItemStack, Double> map = new HashMap();
		map.put(ItemSub.a("branch_oak"), 0.2D);
		map.put(ItemSub.a("seed_oak"), 0.01D);
		map.put(ItemSub.a("leaves"), 0.02D);
		registryTree(new TreeInfo(new TreeChecker(Blocks.log, 0, 4, 8), new TreeChecker(Blocks.leaves, 0, 8), FlaItems.log, new HashMap(map)));
		map.clear();
		map.put(ItemSub.a("branch_spruce"), 0.2D);
		map.put(ItemSub.a("seed_spruce"), 0.01D);
		map.put(ItemSub.a("leaves"), 0.02D);
		registryTree(new TreeInfo(new TreeChecker(Blocks.log, 1, 5, 9), new TreeChecker(Blocks.leaves, 1, 9), FlaItems.log, new HashMap(map)));
		map.clear();
		map.put(ItemSub.a("branch_birch"), 0.2D);
		map.put(ItemSub.a("seed_birch"), 0.02D);
		map.put(ItemSub.a("leaves"), 0.02D);
		registryTree(new TreeInfo(new TreeChecker(Blocks.log, 2, 6, 10), new TreeChecker(Blocks.leaves, 2, 10), FlaItems.log, new HashMap(map)));
		map.clear();
		map.put(ItemSub.a("branch_jungle"), 0.1D);
		map.put(ItemSub.a("seed_jungle"), 0.005D);
		map.put(ItemSub.a("leaves"), 0.02D);
		registryTree(new TreeInfo(new TreeChecker(Blocks.log, 3, 7, 11), new TreeChecker(Blocks.leaves, 3, 11), FlaItems.log, new HashMap(map)));
		map.clear();
		map.put(ItemSub.a("branch_acacia"), 0.2D);
		map.put(ItemSub.a("seed_acacia"), 0.01D);
		map.put(ItemSub.a("leaves"), 0.02D);
		registryTree(new TreeInfo(new TreeChecker(Blocks.log2, 0, 4, 8), new TreeChecker(Blocks.leaves2, 0, 8), FlaItems.log, new HashMap(map)));
		map.clear();
		map.put(ItemSub.a("branch_darkoak"), 0.15D);
		map.put(ItemSub.a("seed_darkoak"), 0.01D);
		map.put(ItemSub.a("leaves"), 0.02D);
		registryTree(new TreeInfo(new TreeChecker(Blocks.log2, 1, 5, 9), new TreeChecker(Blocks.leaves2, 1, 9), FlaItems.log, new HashMap(map)));
	}
	
	public static void registryAxe(IItemChecker item)
	{
		axeList.add(item);
	}
	
	public static void registryTree(TreeInfo info)
	{
		treeList.add(info);
	}
	
	public static boolean isItemAxe(ItemStack itemstack)
	{
		for(IItemChecker c : axeList)
		{
			if(c.match(itemstack)) return true;
		}
		return false;
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onUsingAxe(BreakEvent evt)
	{
		if(evt.getPlayer() != null)
		{
			if(evt.getPlayer().capabilities.isCreativeMode) return;
		}
		BlockPos pos = new BlockPos(evt.world, evt.x, evt.y, evt.z);
		if(evt.getPlayer() != null && new OreChecker("logWood").match(new ItemStack(pos.getBlock(), 1, pos.getBlockMeta())))
		{
			if(!isItemAxe(evt.getPlayer().getCurrentEquippedItem()))
			{
				evt.setCanceled(true);
			}
		}
	}
	
	boolean isUsingAxe = false;
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onUsedAxe(HarvestDropsEvent evt)
	{
		isUsingAxe = false;
		BlockPos pos = new BlockPos(evt.world, evt.x, evt.y, evt.z);
		for(TreeInfo info : treeList)
		{
			if(info.leaf.match(new ItemStack(evt.block, 1, evt.blockMetadata)))
			{
				evt.drops.clear();
				evt.drops.addAll(info.getLeafDrop());
				return;
			}
		}
		if(new OreChecker("treeSapling").match(new ItemStack(evt.block, 1, evt.blockMetadata)))
		{
			evt.drops.clear();
			evt.drops.add(ItemSub.a("branch_bush"));
			return;
		}
		if(isUsingAxe)
		{
			return;
		}
		isUsingAxe = true;
		if(new OreChecker("logWood").match(new ItemStack(evt.block, 1, evt.blockMetadata)))
		{
			for(TreeInfo info : treeList)
			{
				if(info.log.match(new ItemStack(evt.block, 1, evt.blockMetadata)))
				{
					evt.drops.clear();
					List<BlockPos> rootPos = new ArrayList();
					List<BlockPos> completeCheckPos = new ArrayList();
					rootPos.add(pos);
					if(info.log.match(new ItemStack(pos.toPos(1, 0, 1).getBlock(), 1, pos.toPos(1, 0, 1).getBlockMeta()))) rootPos.add(pos.toPos(1, 0, 1));
					if(info.log.match(new ItemStack(pos.toPos(1, 0, 0).getBlock(), 1, pos.toPos(1, 0, 0).getBlockMeta()))) rootPos.add(pos.toPos(1, 0, 0));
					if(info.log.match(new ItemStack(pos.toPos(1, 0, -1).getBlock(), 1, pos.toPos(1, 0, -1).getBlockMeta()))) rootPos.add(pos.toPos(1, 0, -1));
					if(info.log.match(new ItemStack(pos.toPos(0, 0, 1).getBlock(), 1, pos.toPos(0, 0, 1).getBlockMeta()))) rootPos.add(pos.toPos(0, 0, 1));
					if(info.log.match(new ItemStack(pos.toPos(0, 0, -1).getBlock(), 1, pos.toPos(0, 0, -1).getBlockMeta()))) rootPos.add(pos.toPos(0, 0, -1));
					if(info.log.match(new ItemStack(pos.toPos(-1, 0, 1).getBlock(), 1, pos.toPos(-1, 0, 1).getBlockMeta()))) rootPos.add(pos.toPos(-1, 0, 1));
					if(info.log.match(new ItemStack(pos.toPos(-1, 0, 0).getBlock(), 1, pos.toPos(-1, 0, 0).getBlockMeta()))) rootPos.add(pos.toPos(-1, 0, 0));
					if(info.log.match(new ItemStack(pos.toPos(-1, 0, -1).getBlock(), 1, pos.toPos(-1, 0, -1).getBlockMeta()))) rootPos.add(pos.toPos(-1, 0, -1));
					IItemChecker check = new ItemChecker(new ItemStack(evt.block, 1, pos.getBlockMeta() == 0 ? evt.blockMetadata : pos.getBlockMeta()));
					int i = 0;
					int log = 1;
					while(!completeCheckPos.containsAll(rootPos) && rootPos.size() > i)
					{
						BlockPos root = rootPos.get(i);
						BlockPos[] poss = new BlockPos[]{root.toPos(ForgeDirection.UP),
								root.toPos(ForgeDirection.UP).toPos(ForgeDirection.EAST),
								root.toPos(ForgeDirection.UP).toPos(ForgeDirection.NORTH),
								root.toPos(ForgeDirection.UP).toPos(ForgeDirection.WEST),
								root.toPos(ForgeDirection.UP).toPos(ForgeDirection.SOUTH),
								root.toPos(ForgeDirection.UP).toPos(ForgeDirection.NORTH).toPos(ForgeDirection.EAST),
								root.toPos(ForgeDirection.UP).toPos(ForgeDirection.NORTH).toPos(ForgeDirection.WEST),
								root.toPos(ForgeDirection.UP).toPos(ForgeDirection.SOUTH).toPos(ForgeDirection.EAST),
								root.toPos(ForgeDirection.UP).toPos(ForgeDirection.SOUTH).toPos(ForgeDirection.WEST)
						};
						for(int x = 0; x < poss.length; ++x)
						{
							if(completeCheckPos.contains(poss[x]) || (!info.log.match(new ItemStack(poss[x].getBlock(), 1, poss[x].getBlockMeta())))) continue;
							++log;
							evt.world.setBlockToAir(poss[x].x, poss[x].y, poss[x].z);
							rootPos.add(poss[x]);
						}
						if(!info.leafDrops.isEmpty())
						{
							for(int x = -2; x < 3; ++x)
								for(int y = -2; y < 3; ++y)
									for(int z = -2; z < 3; ++z)
									{
										BlockPos leaf = root.toPos(x, y, z);
										if(info.leaf.match(new ItemStack(leaf.getBlock(), 1, leaf.getBlockMeta())))
										{
											evt.drops.addAll(info.getLeafDrop());
											evt.world.setBlockToAir(leaf.x, leaf.y, leaf.z);
										}
									}
						}
						completeCheckPos.add(root);
						++i;
					}
					evt.drops.add(info.logDrops.createStandardLog(evt.block, evt.blockMetadata, log));
					isUsingAxe = false;
					return;
				}
			}
			List<BlockPos> rootPos = new ArrayList();
			List<BlockPos> completeCheckPos = new ArrayList();
			rootPos.add(pos);
			IItemChecker check = new ItemChecker(new ItemStack(evt.block, 1, pos.getBlockMeta() == 0 ? evt.blockMetadata : pos.getBlockMeta()));
			int i = 0;
			while(!completeCheckPos.containsAll(rootPos) && rootPos.size() > i)
			{
				BlockPos root = rootPos.get(i);
				BlockPos[] poss = new BlockPos[]{root.toPos(ForgeDirection.UP),
						root.toPos(ForgeDirection.UP).toPos(ForgeDirection.EAST),
						root.toPos(ForgeDirection.UP).toPos(ForgeDirection.NORTH),
						root.toPos(ForgeDirection.UP).toPos(ForgeDirection.WEST),
						root.toPos(ForgeDirection.UP).toPos(ForgeDirection.SOUTH),
						root.toPos(ForgeDirection.UP).toPos(ForgeDirection.NORTH).toPos(ForgeDirection.EAST),
						root.toPos(ForgeDirection.UP).toPos(ForgeDirection.NORTH).toPos(ForgeDirection.WEST),
						root.toPos(ForgeDirection.UP).toPos(ForgeDirection.SOUTH).toPos(ForgeDirection.EAST),
						root.toPos(ForgeDirection.UP).toPos(ForgeDirection.SOUTH).toPos(ForgeDirection.WEST)
				};
				for(int x = 0; x < poss.length; ++x)
				{
					if(completeCheckPos.contains(poss[x])) continue;
					if(check.match(new ItemStack(poss[x].getBlock(), 1, poss[x].getBlockMeta())))
					{
						poss[x].getBlock().harvestBlock(evt.world, evt.harvester, poss[x].x, poss[x].y, poss[x].z, poss[x].getBlockMeta());
						evt.world.setBlockToAir(poss[x].x, poss[x].y, poss[x].z);
						rootPos.add(poss[x]);
					}
				}
				completeCheckPos.add(root);
				++i;
			}
		}
		isUsingAxe = false;
	}
	
	public static class TreeChecker implements IItemChecker
	{
		Block block;
		List<Integer> meta;
		
		public TreeChecker(Block i, Integer...is)
		{
			this.block = i;
			meta = Arrays.asList(is);
		}
		public TreeChecker(Block i, int meta)
		{
			this(i, new Integer[]{meta});
		}
		public TreeChecker(Block i)
		{
			this(i, 0);
		}

		@Override
		public boolean match(ItemStack target) 
		{
			return target.getItem() == Item.getItemFromBlock(block) ? meta.contains(target.getItemDamage()) : false;
		}
		
		@Override
		public List<ItemStack> getEqulStacks() 
		{
			List<ItemStack> ret = new ArrayList();
			for(Integer meta : this.meta)
			{
				ret.add(new ItemStack(block, 1, meta));
			}
			return ret;
		}
	}
	
	public static class TreeInfo
	{
		private static final Random rand = new Random();
		
		public IItemChecker log;
		public IItemChecker leaf;
		
		public ITreeLog logDrops;
		public Map<ItemStack, Double> leafDrops = new HashMap();
		
		public TreeInfo(IItemChecker log, IItemChecker leaf, ITreeLog l, Map<ItemStack, Double> leafDrop)
		{
			this.log = log;
			this.leaf = leaf;
			this.logDrops = l;
			this.leafDrops = leafDrop;
		}
		
		public boolean hasLog()
		{
			return this.logDrops != null;
		}
		
		public List<ItemStack> getLeafDrop()
		{
			List<ItemStack> ret = new ArrayList();
			if(leafDrops != null)
			{
				Iterator<ItemStack> itr = leafDrops.keySet().iterator();
				while (itr.hasNext()) 
				{
					ItemStack stack = itr.next();
					double chance = leafDrops.get(stack);
					double a = (rand.nextGaussian() + 1) / 2;
					if(a < chance)
					{
						ret.add(stack.copy());
					}
				}
			}
			return ret;
		}
	}
}