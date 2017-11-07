package nebula.common.stack;

import java.util.List;

import com.google.common.collect.ImmutableList;

import nebula.common.util.ItemStacks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreStack implements AbstractStack
{
	public static AbstractStack sizeOf(OreStack stack, int size)
	{
		return size <= 0 ? null : new OreStack(stack.oreName, size);
	}
	
	private ImmutableList<ItemStack>	list;
	public final String					oreName;
	private List<ItemStack>				ore;
	public final int					size;
	
	public OreStack(String ore)
	{
		this(ore, 1);
	}
	
	public OreStack(String ore, int size)
	{
		this.oreName = ore;
		this.ore = OreDictionary.getOres(ore);
		this.size = size;
	}
	
	@Deprecated
	public OreStack(String ore, int size, boolean useContainer)
	{
		this(ore, size);
	}
	
	@Override
	public boolean similar(ItemStack stack)
	{
		return this.ore.stream().anyMatch(target -> OreDictionary.itemMatches(target, stack, false));
	}
	
	@Override
	public boolean contain(ItemStack stack)
	{
		return similar(stack) ? stack.stackSize >= this.size : false;
	}
	
	@Override
	public int size(ItemStack stack)
	{
		return this.size;
	}
	
	@Override
	public AbstractStack split(ItemStack stack)
	{
		return sizeOf(this, this.size - stack.stackSize);
	}
	
	@Override
	public AbstractStack copyWithSize(int size)
	{
		return sizeOf(this, size);
	}
	
	@Override
	public ItemStack instance()
	{
		if (!display().isEmpty()) return ItemStack.copyItemStack(this.list.get(0));
		return null;
	}
	
	@Override
	public List<ItemStack> display()
	{
		if (this.list == null)
		{
			this.list = ItemStacks.sizeOf(this.ore, this.size);
		}
		return this.list;
	}
	
	@Override
	public boolean valid()
	{
		return !this.ore.isEmpty();
	}
	
	@Override
	public String toString()
	{
		return "[ore:" + this.oreName + "]x" + this.size;
	}
}
