package farcore.lib.stack;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.lib.oredict.OreDictExt;
import farcore.util.ItemStacks;
import net.minecraft.item.ItemStack;

public class OreStack implements AbstractStack
{
	public static AbstractStack sizeOf(OreStack stack, int size)
	{
		return size <= 0 ? null : new OreStack(stack.oreName, size, stack.useContainer);
	}

	private ImmutableList<ItemStack> list;
	private String oreName;
	private List<ItemStack> ore;
	private int size;
	private boolean useContainer;
	
	public OreStack(String ore)
	{
		this(ore, 1);
	}
	public OreStack(String ore, int size)
	{
		this(ore, size, false);
	}
	public OreStack(String ore, int size, boolean useContainer)
	{
		oreName = ore;
		this.ore = OreDictExt.getOres(ore);
		this.size = size;
		this.useContainer = useContainer;
	}

	@Override
	public boolean similar(ItemStack stack)
	{
		return OreDictExt.oreMatchs(stack, oreName);
	}
	
	@Override
	public boolean contain(ItemStack stack)
	{
		return similar(stack) ? stack.stackSize >= size : false;
	}
	
	@Override
	public int size(ItemStack stack)
	{
		return size;
	}

	@Override
	public AbstractStack split(ItemStack stack)
	{
		return sizeOf(this, size - stack.stackSize);
	}
	
	@Override
	public ItemStack instance()
	{
		if(!display().isEmpty())
			return list.get(0);
		return null;
	}
	
	@Override
	public List<ItemStack> display()
	{
		if(list == null)
		{
			list = ItemStacks.sizeOf(ore, size);
		}
		return list;
	}
	
	@Override
	public boolean valid()
	{
		return !ore.isEmpty();
	}
	
	@Override
	public boolean useContainer()
	{
		return useContainer;
	}
}