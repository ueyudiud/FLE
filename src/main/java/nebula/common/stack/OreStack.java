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
		this.oreName = ore;
		this.ore = OreDictionary.getOres(ore);
		this.size = size;
		this.useContainer = useContainer;
	}
	
	@Override
	public boolean similar(ItemStack stack)
	{
		return OreDictionary.containsMatch(false, this.ore, stack);
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
		if(!display().isEmpty())
			return this.list.get(0);
		return null;
	}
	
	@Override
	public List<ItemStack> display()
	{
		if(this.list == null)
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
	public boolean useContainer()
	{
		return this.useContainer;
	}
}