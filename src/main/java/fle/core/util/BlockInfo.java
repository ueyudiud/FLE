package fle.core.util;

import net.minecraft.block.Block;

public class BlockInfo
{
	public Block block;
	public int meta;
	
	public BlockInfo(Block block)
	{
		this(block, 0);
	}
	public BlockInfo(Block block, int meta)
	{
		this.block = block;
		this.meta = meta;
	}
}