package fle.api.util;

import farcore.lib.recipe.IBlockConditionMacher;
import farcore.lib.stack.AbstractStack;
import farcore.lib.stack.BaseStack;
import farcore.lib.stack.OreStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class BlockConditionMatcherIS implements IBlockConditionMacher
{
	private AbstractStack stack;

	public BlockConditionMatcherIS(Block block)
	{
		this(block, OreDictionary.WILDCARD_VALUE);
	}
	public BlockConditionMatcherIS(Block block, int meta)
	{
		this(new ItemStack(block, 1, meta));
	}
	public BlockConditionMatcherIS(ItemStack stack)
	{
		this(new BaseStack(stack));
	}
	public BlockConditionMatcherIS(String ore)
	{
		this(new OreStack(ore));
	}
	public BlockConditionMatcherIS(AbstractStack stack)
	{
		this.stack = stack;
	}
	
	@Override
	public boolean match(World world, int x, int y, int z, Block block, int meta)
	{
		return stack.similar(new ItemStack(block, 1, meta));
	}
}