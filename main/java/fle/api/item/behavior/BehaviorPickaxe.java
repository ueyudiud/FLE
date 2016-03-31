package fle.api.item.behavior;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class BehaviorPickaxe extends BehaviorDigableTool
{
	private static final Set blockCanHeaverst = Sets.newHashSet();
			//new Block[] {Blocks.cobblestone, Blocks.double_stone_slab, Blocks.stone_slab, Blocks.stone, Blocks.sandstone, Blocks.mossy_cobblestone, Blocks.iron_ore, Blocks.iron_block, Blocks.coal_ore, Blocks.gold_block, Blocks.gold_ore, Blocks.diamond_ore, Blocks.diamond_block, Blocks.ice, Blocks.netherrack, Blocks.lapis_ore, Blocks.lapis_block, Blocks.redstone_ore, Blocks.lit_redstone_ore, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.activator_rail });
	private static final Set toolClasses = Sets.newHashSet("pickaxe");
	
	@Override
	public boolean canHarvestBlock(Block block, ItemStack stack)
	{
		return blockCanHeaverst.contains(block) || super.canHarvestBlock(block, stack);
	}
	
	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return toolClasses;
	}
}