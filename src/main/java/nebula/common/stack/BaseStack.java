package nebula.common.stack;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class BaseStack implements AbstractStack
{
	public static final BaseStack EMPTY = new BaseStack((ItemStack) null);
	
	public static BaseStack sizeOf(BaseStack stack, int size)
	{
		return size <= 0 ? null : new BaseStack(stack.stack, size);
	}
	
	private ImmutableList<ItemStack> list;
	private ItemStack stack;
	public final boolean isWildcardValue;
	
	public BaseStack(String modid, String name, int size, int meta)
	{
		Item item = GameRegistry.findItem(modid, name);
		if(item != null)
		{
			this.stack = new ItemStack(item, size, meta);
		}
		this.isWildcardValue = item != null && ((item.getHasSubtypes() || item instanceof ItemBlock) && meta == OreDictionary.WILDCARD_VALUE);
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
			this.stack = new ItemStack(block, size, meta);
		}
		this.isWildcardValue = meta == OreDictionary.WILDCARD_VALUE;
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
			this.stack = new ItemStack(item, size, meta);
		}
		this.isWildcardValue = item != null &&
				((item.getHasSubtypes() || item instanceof ItemBlock) && meta == OreDictionary.WILDCARD_VALUE);
	}
	public BaseStack(ItemStack stack)
	{
		this.stack = ItemStack.copyItemStack(stack);
		this.isWildcardValue = false;
	}
	public BaseStack(ItemStack stack, int size)
	{
		if(stack != null)
		{
			this.stack = stack.copy();
			this.stack.stackSize = size;
		}
		this.isWildcardValue = stack != null &&
				(stack.getHasSubtypes() || stack.getItem() instanceof ItemBlock) && stack.getItemDamage() == OreDictionary.WILDCARD_VALUE;
	}
	public BaseStack(ItemStack stack, int size, int meta)
	{
		if(stack != null)
		{
			this.stack = stack.copy();
			this.stack.stackSize = size;
			this.stack.setItemDamage(meta);
		}
		this.isWildcardValue = stack != null &&
				(stack.getHasSubtypes() || stack.getItem() instanceof ItemBlock) && meta == OreDictionary.WILDCARD_VALUE;
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
	public AbstractStack copyWithSize(int size)
	{
		return sizeOf(this, size);
	}
	
	@Override
	public ItemStack instance()
	{
		if(this.stack != null)
		{
			ItemStack ret = this.stack.copy();
			if(this.isWildcardValue)
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
		if(this.list == null)
			if(this.stack != null)
			{
				this.list = ImmutableList.of(this.stack.copy());
			}
			else
			{
				this.list = ImmutableList.of();
			}
		return this.list;
	}
	
	@Override
	public boolean valid()
	{
		return this.stack != null;
	}
	
	@Override
	public String toString()
	{
		return "[" + this.stack.getUnlocalizedName() + "]" + "x" + this.stack.stackSize;
	}
	
	@Override
	public int hashCode()
	{
		return this.stack == null ? 31 :
			this.stack.getItem().hashCode() * 31 + this.stack.getItemDamage();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this.stack == null) return obj == EMPTY;
		if(obj == this)
			return true;
		else if(!(obj instanceof BaseStack))
			return false;
		BaseStack stack1 = (BaseStack) obj;
		return ItemStack.areItemStacksEqual(this.stack, stack1.stack);
	}
}