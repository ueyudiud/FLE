package farcore.lib.stack;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.util.U;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ArrayStack implements AbstractStack
{
	public static ArrayStack sizeOf(ArrayStack stack, int size)
	{
		return new ArrayStack(size, stack.array);
	}
	
	public int size;
	public final List<ItemStack> array;
	
	public ArrayStack(int size, Collection<ItemStack> collection)
	{
		array = ImmutableList.copyOf(collection);
		this.size = size;
	}
	public ArrayStack(Collection<ItemStack> collection)
	{
		this(1, collection);
	}
	public ArrayStack(int size, ItemStack...stacks)
	{
		array = ImmutableList.copyOf(stacks);
		this.size = size;
	}
	public ArrayStack(ItemStack...stacks)
	{
		this(1, stacks);
	}

	@Override
	public boolean similar(ItemStack stack)
	{
		if(stack == null) return false;
		for(ItemStack target : array)
		{
			if(OreDictionary.itemMatches(target, stack, false))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean contain(ItemStack stack)
	{
		return similar(stack) && stack.stackSize >= size;
	}

	@Override
	public int size(ItemStack stack)
	{
		return size;
	}

	@Override
	public AbstractStack split(ItemStack stack)
	{
		return size >= stack.stackSize ?
				new ArrayStack(size - stack.stackSize, array) : 
					null;
	}

	@Override
	public ItemStack instance()
	{
		return U.ItemStacks.sizeOf(array.get(0), size);
	}

	@Override
	public List<ItemStack> display()
	{
		return U.ItemStacks.sizeOf(array, size);
	}

	@Override
	public boolean valid()
	{
		return !array.isEmpty();
	}

	@Override
	public boolean useContainer()
	{
		return false;
	}
}