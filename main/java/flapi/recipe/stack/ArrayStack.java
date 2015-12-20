package flapi.recipe.stack;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import flapi.collection.abs.AbstractStack;

public class ArrayStack extends ItemAbstractStack
{
	private List<ItemStack> stack;

	public ArrayStack(Block aBlock) 
	{
		this(aBlock, OreDictionary.WILDCARD_VALUE);
	}
	public ArrayStack(Block aBlock, int aMeta) 
	{
		this(new ItemStack(aBlock, 1, aMeta));
	}
	public ArrayStack(Item aItem) 
	{
		this(aItem, OreDictionary.WILDCARD_VALUE);
	}
	public ArrayStack(Item aItem, int aMeta) 
	{
		this(new ItemStack(aItem, 1, aMeta));
	}
	public ArrayStack(ItemStack...aStack) 
	{
		this(Arrays.asList(aStack));
	}
	public ArrayStack(List<ItemStack> list)
	{
		stack = list;
	}

	@Override
	public boolean equal(ItemStack item)
	{
		for(ItemStack tStack : stack)
		{
			if(OreDictionary.itemMatches(tStack, item, false))
				return true;
		}
		return false;
	}

	@Override
	public boolean equal(AbstractStack<ItemStack> stack) 
	{
		return stack instanceof ArrayStack ? ((ArrayStack) stack).stack.equals(this.stack) : false;
	}

	@Override
	public ItemStack[] toList() 
	{
		return stack.toArray(new ItemStack[stack.size()]);
	}
	
	@Override
	public String toString()
	{
		return "stack.array." + stack.toString();
	}
	
	@Override
	public boolean contain(AbstractStack<ItemStack> arg)
	{
		return false;
	}
}