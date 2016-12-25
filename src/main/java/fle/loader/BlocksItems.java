package fle.loader;

import farcore.data.CT;
import farcore.lib.item.instance.ItemStoneChip;
import fle.core.blocks.BlockDitch;
import fle.core.items.ItemSimpleFluidContainer;
import fle.core.items.ItemToolFar;
import net.minecraft.block.Block;

public class BlocksItems
{
	public static ItemToolFar tool;
	public static ItemSimpleFluidContainer fluidContainer;
	public static Block ditch;
	
	public static void registerItemsAndBlocks()
	{
		tool = (ItemToolFar) new ItemToolFar().setCreativeTab(CT.tabTool);
		fluidContainer = (ItemSimpleFluidContainer) new ItemSimpleFluidContainer().setCreativeTab(CT.tabTool);
		ditch = new BlockDitch();
	}
	
	public static void setBlocksItemsProperties()
	{
		ItemStoneChip.shootStoneChipExp = 0.7F;
	}
}