package farcore.lib.stack;

import java.util.List;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class BaseStack implements AbstractStack
{
	public static final BaseStack EMPTY = new BaseStack((ItemStack) null);
	
	public static BaseStack sizeOf(BaseStack stack, int size)
	{
		return size <= 0 ? null : new BaseStack(stack.stack, size, stack.useContainer);
	}
	
	private ImmutableList<ItemStack> list;
	private ItemStack stack;
	private boolean useContainer;

	public BaseStack(String modid, String name, int size, int meta)
	{
		this(modid, name, size, meta, false);
	}
	public BaseStack(String modid, String name, int size, int meta, boolean useContainer)
	{
		Item item = GameRegistry.findItem(modid, name);
		if(item != null)
		{
			stack = new ItemStack(item, size, meta);
		}
		useContainer = false;
	}
	public BaseStack(Block block)
	{
		this(block, 1);
	}
	public BaseStack(Block block, int size)
	{
		this(block, size, OreDictionary.WILDCARD_VALUE);
	}
	public BaseStack(Block block, int size, int meta)
	{
		if(block != null)
		{
			stack = new ItemStack(block, size, meta);
		}
		useContainer = false;
	}
	public BaseStack(Item item)
	{
		this(item, 1);
	}
	public BaseStack(Item item, int size)
	{
		this(item, size, OreDictionary.WILDCARD_VALUE);
	}
	public BaseStack(Item item, int size, int meta)
	{
		if(item != null)
		{
			stack = new ItemStack(item, size, meta);
		}
		useContainer = false;
	}
	public BaseStack(ItemStack stack)
	{
		this(stack, false);
	}
	public BaseStack(ItemStack stack, int size)
	{
		if(stack != null)
		{
			this.stack = stack.copy();
			this.stack.stackSize = size;
		}
	}
	public BaseStack(ItemStack stack, int size, int meta)
	{
		if(stack != null)
		{
			this.stack = stack.copy();
			this.stack.stackSize = size;
			this.stack.setItemDamage(meta);
		}
	}
	public BaseStack(ItemStack stack, boolean useContainer)
	{
		this.stack = ItemStack.copyItemStack(stack);
		this.useContainer = useContainer;
	}
	public BaseStack(ItemStack stack, int size, boolean useContainer)
	{
		if(stack != null)
		{
			this.stack = stack.copy();
			this.stack.stackSize = size;
		}
		this.useContainer = useContainer;
	}
	
	@Override
	public boolean similar(ItemStack stack)
	{
		return this.stack == null ? stack == null :
			OreDictionary.itemMatches(this.stack, stack, false);
	}

	@Override
	public boolean contain(ItemStack stack)
	{
		return similar(stack) &&
				(this.stack == null || this.stack.stackSize <= stack.stackSize);
	}

	@Override
	public int size(ItemStack stack)
	{
		return this.stack == null ? 0 :
			this.stack.stackSize;
	}
	
	@Override
	public AbstractStack split(ItemStack stack)
	{
		return sizeOf(this, this.stack.stackSize - stack.stackSize);
	}

	@Override
	public ItemStack instance()
	{
		if(stack != null)
		{
			ItemStack ret = stack.copy();
			if(ret.getItemDamage() == OreDictionary.WILDCARD_VALUE)
			{
				ret.setItemDamage(0);
			}
			return ret;
		}
		return null;
	}

	@Override
	public List<ItemStack> display()
	{
		if(list == null)
		{
			if(stack != null)
			{
				list = ImmutableList.of(stack.copy());
			}
			else
			{
				list = ImmutableList.of();
			}
		}
		return list;
	}

	@Override
	public boolean valid()
	{
		return stack != null;
	}

	@Override
	public boolean useContainer()
	{
		return useContainer;
	}

	@Override
	public String toString()
	{
		return "[" + stack.getUnlocalizedName() + "]" + "x" + stack.stackSize;
	}
	
	@Override
	public int hashCode()
	{
		return stack == null ? 31 :
			stack.getItem().hashCode() * 31 + stack.getItemDamage();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(stack == null) return obj == EMPTY;
		if(obj == this)
		{
			return true;
		}
		else if(!(obj instanceof BaseStack))
		{
			return false;
		}
		BaseStack stack1 = (BaseStack) obj;
		return ItemStack.areItemStacksEqual(stack, stack1.stack) && useContainer == stack1.useContainer;
	}
}