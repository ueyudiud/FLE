package fle.api.util;

import farcore.lib.recipe.IBlockConditionMacher;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class BlockConditionMatcherBI implements IBlockConditionMacher
{
	private Block block;
	private int meta;

	public BlockConditionMatcherBI(Block block)
	{
		this(block, OreDictionary.WILDCARD_VALUE);
	}
	public BlockConditionMatcherBI(Block block, int meta)
	{
		this.block = block;
		this.meta = meta;
	}

	@Override
	public boolean match(World world, int x, int y, int z, Block block, int meta)
	{
		return this.block == block && this.meta == meta;
	}
}