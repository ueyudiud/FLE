package farcore.recipe.stack;

import java.util.List;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class DM extends AM
{
	private Item item;
	private int meta;
	
	public DM(Block block)
	{
		this(block, OreDictionary.WILDCARD_VALUE);
	}
	public DM(Item item)
	{
		this(item, OreDictionary.WILDCARD_VALUE);
	}
	public DM(Block block, int meta)
	{
		this(Item.getItemFromBlock(block), meta);
	}
	public DM(Item item, int meta)
	{
		this.item = item;
		this.meta = meta;
	}

	@Override
	public int hashCode()
	{
		return item.hashCode() * 31 + meta;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof DM ? ((DM) obj).item == item && ((DM) obj).meta == meta : false;
	}
	
	@Override
	public boolean match(ItemStack stack)
	{
		return stack == null ? item == null : item == stack.getItem() &&
			(meta == OreDictionary.WILDCARD_VALUE ? true : meta == item.getDamage(stack));
	}

	@Override
	protected Object createList()
	{
		return item == null ? null : 
			new ItemStack(item, 1, meta == OreDictionary.WILDCARD_VALUE ? 0 : meta);
	}
}