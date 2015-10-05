package fle.api.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemArrayStack extends ItemAbstractStack
{
	private Set<ItemStack> stack;

	public ItemArrayStack(Block aBlock) 
	{
		this(aBlock, OreDictionary.WILDCARD_VALUE);
	}
	public ItemArrayStack(Block aBlock, int aMeta) 
	{
		this(new ItemStack(aBlock, 1, aMeta));
	}
	public ItemArrayStack(Item aItem) 
	{
		this(aItem, OreDictionary.WILDCARD_VALUE);
	}
	public ItemArrayStack(Item aItem, int aMeta) 
	{
		this(new ItemStack(aItem, 1, aMeta));
	}
	public ItemArrayStack(ItemStack...aStack) 
	{
		this(Arrays.asList(aStack));
	}
	public ItemArrayStack(Iterable<ItemStack> list)
	{
		stack = Sets.newConcurrentHashSet(list);
	}

	@Override
	public boolean isStackEqul(ItemStack item)
	{
		for(ItemStack tStack : stack)
		{
			if(OreDictionary.itemMatches(tStack, item, false))
				return true;
		}
		return false;
	}

	@Override
	public boolean isStackEqul(FluidStack item) 
	{
		return false;
	}

	@Override
	public boolean isStackEqul(ItemAbstractStack stack) 
	{
		return stack instanceof ItemArrayStack ? ((ItemArrayStack) stack).stack.equals(this.stack) : false;
	}

	@Override
	public List<ItemStack> toArray() 
	{
		return new ArrayList(stack);
	}
	
	@Override
	public String toString()
	{
		return "stack.array." + stack.toString();
	}
}