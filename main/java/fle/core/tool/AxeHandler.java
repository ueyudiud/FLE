package fle.core.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fle.api.item.ITreeLog;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.recipe.ItemOreStack;
import fle.api.util.DropInfo;
import fle.api.world.BlockPos;
import fle.core.init.IB;
import fle.core.item.ItemFleSub;

public class AxeHandler 
{
	private static final List<ItemAbstractStack> axeList = new ArrayList();
	private static final List<TreeInfo> treeList = new ArrayList();
	
	static
	{
		registryAxe(new ItemOreStack("craftingToolAxe"));
		Map<ItemStack, Integer> map = new HashMap();
		map.put(ItemFleSub.a("branch_oak"), 5);
		map.put(ItemFleSub.a("seed_oak"), 1);
		map.put(ItemFleSub.a("leaves"), 4);
		registryTree(new TreeInfo(new TreeChecker(Blocks.log, 0, 4, 8), new TreeChecker(Blocks.leaves, 0, 8), (ITreeLog) IB.treeLog, new DropInfo(0.6F, map)));
		map.clear();
		map.put(ItemFleSub.a("branch_spruce"), 5);
		map.put(ItemFleSub.a("seed_spruce"), 1);
		map.put(ItemFleSub.a("leaves"), 4);
		registryTree(new TreeInfo(new TreeChecker(Blocks.log, 1, 5, 9), new TreeChecker(Blocks.leaves, 1, 9), (ITreeLog) IB.treeLog, new DropInfo(0.6F, map)));
		map.clear();
		map.put(ItemFleSub.a("branch_birch"), 5);
		map.put(ItemFleSub.a("seed_birch"), 1);
		map.put(ItemFleSub.a("leaves"), 4);
		registryTree(new TreeInfo(new TreeChecker(Blocks.log, 2, 6, 10), new TreeChecker(Blocks.leaves, 2, 10), (ITreeLog) IB.treeLog, new DropInfo(0.6F, map)));
		map.clear();
		map.put(ItemFleSub.a("branch_jungle"), 6);
		map.put(ItemFleSub.a("seed_jungle"), 1);
		map.put(ItemFleSub.a("leaves"), 6);
		registryTree(new TreeInfo(new TreeChecker(Blocks.log, 3, 7, 11), new TreeChecker(Blocks.leaves, 3, 11), (ITreeLog) IB.treeLog, new DropInfo(0.5F, map)));
		map.clear();
		map.put(ItemFleSub.a("branch_acacia"), 5);
		map.put(ItemFleSub.a("seed_acacia"), 1);
		map.put(ItemFleSub.a("leaves"), 4);
		registryTree(new TreeInfo(new TreeChecker(Blocks.log2, 0, 4, 8), new TreeChecker(Blocks.leaves2, 0, 8), (ITreeLog) IB.treeLog, new DropInfo(0.6F, map)));
		map.clear();
		map.put(ItemFleSub.a("branch_darkoak"), 5);
		map.put(ItemFleSub.a("seed_darkoak"), 1);
		map.put(ItemFleSub.a("leaves"), 5);
		registryTree(new TreeInfo(new TreeChecker(Blocks.log2, 1, 5, 9), new TreeChecker(Blocks.leaves2, 1, 9), (ITreeLog) IB.treeLog, new DropInfo(0.5F, map)));

	}
	
	public AxeHandler()
	{
		
	}
	
	public static void registryAxe(ItemAbstractStack item)
	{
		axeList.add(item);
	}
	
	public static void registryTree(TreeInfo info)
	{
		treeList.add(info);
	}
	
	public static boolean isItemAxe(ItemStack itemstack)
	{
		for(ItemAbstractStack c : axeList)
		{
			if(c.isStackEqul(itemstack)) return true;
		}
		return false;
	}
	
	@SubscribeEvent
	public void onPlaceItem(PlaceEvent evt)
	{
		if(evt.player != null)
			if(!evt.player.capabilities.isCreativeMode)
			{
				if(evt.block == Blocks.log || evt.block == Blocks.log2)
				{
					evt.setCanceled(true);
				}
			}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onUsingAxe(BreakEvent evt)
	{
		if(evt.getPlayer() != null)
		{
			if(evt.getPlayer().capabilities.isCreativeMode) return;
		}
		BlockPos pos = new BlockPos(evt.world, evt.x, evt.y, evt.z);
		if(evt.getPlayer() != null && new ItemOreStack("logWood").isStackEqul(new ItemStack(pos.getBlock(), 1, pos.getBlockMeta())))
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
		if(new ItemOreStack("treeSapling").isStackEqul(new ItemStack(evt.block, 1, evt.blockMetadata)))
		{
			evt.drops.clear();
			return;
		}
		for(TreeInfo info : treeList)
		{
			if(info.leaf.isStackEqul(new ItemStack(evt.block, 1, evt.blockMetadata)))
			{
				evt.drops.clear();
				evt.drops.addAll(info.getLeafDrop());
				return;
			}
		}
		if(isUsingAxe)
		{
			return;
		}
		isUsingAxe = true;
		if(new ItemOreStack("logWood").isStackEqul(new ItemStack(evt.block, 1, evt.blockMetadata)))
		{
			for(TreeInfo info : treeList)
			{
				if(info.log.isStackEqul(new ItemStack(evt.block, 1, evt.blockMetadata)))
				{
					evt.drops.clear();
					List<BlockPos> rootPos = new ArrayList();
					List<BlockPos> completeCheckPos = new ArrayList();
					rootPos.add(pos);
					if(info.log.isStackEqul(new ItemStack(pos.toPos(1, 0, 1).getBlock(), 1, pos.toPos(1, 0, 1).getBlockMeta()))) rootPos.add(pos.toPos(1, 0, 1));
					if(info.log.isStackEqul(new ItemStack(pos.toPos(1, 0, 0).getBlock(), 1, pos.toPos(1, 0, 0).getBlockMeta()))) rootPos.add(pos.toPos(1, 0, 0));
					if(info.log.isStackEqul(new ItemStack(pos.toPos(1, 0, -1).getBlock(), 1, pos.toPos(1, 0, -1).getBlockMeta()))) rootPos.add(pos.toPos(1, 0, -1));
					if(info.log.isStackEqul(new ItemStack(pos.toPos(0, 0, 1).getBlock(), 1, pos.toPos(0, 0, 1).getBlockMeta()))) rootPos.add(pos.toPos(0, 0, 1));
					if(info.log.isStackEqul(new ItemStack(pos.toPos(0, 0, -1).getBlock(), 1, pos.toPos(0, 0, -1).getBlockMeta()))) rootPos.add(pos.toPos(0, 0, -1));
					if(info.log.isStackEqul(new ItemStack(pos.toPos(-1, 0, 1).getBlock(), 1, pos.toPos(-1, 0, 1).getBlockMeta()))) rootPos.add(pos.toPos(-1, 0, 1));
					if(info.log.isStackEqul(new ItemStack(pos.toPos(-1, 0, 0).getBlock(), 1, pos.toPos(-1, 0, 0).getBlockMeta()))) rootPos.add(pos.toPos(-1, 0, 0));
					if(info.log.isStackEqul(new ItemStack(pos.toPos(-1, 0, -1).getBlock(), 1, pos.toPos(-1, 0, -1).getBlockMeta()))) rootPos.add(pos.toPos(-1, 0, -1));
					ItemAbstractStack check = new ItemBaseStack(new ItemStack(evt.block, 1, pos.getBlockMeta() == 0 ? evt.blockMetadata : pos.getBlockMeta()));
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
							if(completeCheckPos.contains(poss[x]) || (!info.log.isStackEqul(new ItemStack(poss[x].getBlock(), 1, poss[x].getBlockMeta())))) continue;
							++log;
							evt.world.setBlockToAir(poss[x].x, poss[x].y, poss[x].z);
							rootPos.add(poss[x]);
						}
						if(info.leafDrops != null)
						{
							for(int x = -2; x < 3; ++x)
								for(int y = -2; y < 3; ++y)
									for(int z = -2; z < 3; ++z)
									{
										BlockPos leaf = root.toPos(x, y, z);
										if(info.leaf.isStackEqul(new ItemStack(leaf.getBlock(), 1, leaf.getBlockMeta())))
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
			ItemAbstractStack check = new ItemBaseStack(new ItemStack(evt.block, 1, pos.getBlockMeta() == 0 ? evt.blockMetadata : pos.getBlockMeta()));
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
					if(check.isStackEqul(new ItemStack(poss[x].getBlock(), 1, poss[x].getBlockMeta())))
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
	
	public static class TreeChecker extends ItemAbstractStack
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
		public boolean isStackEqul(ItemStack target)
		{
			return target.getItem() == Item.getItemFromBlock(block) ? meta.contains(target.getItemDamage()) : false;
		}
		
		@Override
		public boolean isStackEqul(FluidStack item) 
		{
			return false;
		}
		
		@Override
		public boolean isStackEqul(ItemAbstractStack stack) 
		{
			return false;
		}
		
		@Override
		public List<ItemStack> toArray()
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
		
		public ItemAbstractStack log;
		public ItemAbstractStack leaf;
		
		public ITreeLog logDrops;
		public DropInfo leafDrops;
		
		public TreeInfo(ItemAbstractStack log, ItemAbstractStack leaf, ITreeLog l, DropInfo leafDrop)
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
			return leafDrops.getDrops();
		}
	}
}