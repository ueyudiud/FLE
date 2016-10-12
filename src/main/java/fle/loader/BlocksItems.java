package fle.loader;

import farcore.data.CT;
import farcore.lib.item.instance.ItemStoneChip;
import fle.core.items.ItemToolFar;

public class BlocksItems
{
	public static ItemToolFar tool;
	
	public static void registerItemsAndBlocks()
	{
		tool = (ItemToolFar) new ItemToolFar().setCreativeTab(CT.tabTool);
	}

	public static void setBlocksItemsProperties()
	{
		ItemStoneChip.shootStoneChipExp = 0.7F;
	}
}
