package farcore.recipe.stack;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.util.Util;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class IS extends AS
{
	public IS(Block block)
	{
		this(block, 1);
	}
	public IS(Item item)
	{
		this(item, 1);
	}
	public IS(Block block, int size)
	{
		this(block, size, OreDictionary.WILDCARD_VALUE);
	}
	public IS(Item item, int size)
	{
		this(item, size, OreDictionary.WILDCARD_VALUE);
	}
	public IS(Block block, int size, int meta)
	{
		this(new DM(block, meta), size);
	}
	public IS(Item item, int size, int meta)
	{
		this(new DM(item, meta), size);
	}
	public IS(DM matcher, int size)
	{
		super(matcher, size);
	}
}