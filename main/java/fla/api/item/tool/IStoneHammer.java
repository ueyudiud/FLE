package fla.api.item.tool;

import net.minecraft.block.Block;

public interface IStoneHammer extends ITool
{
	public boolean canCrushBlock(Block block, int meta);
}
