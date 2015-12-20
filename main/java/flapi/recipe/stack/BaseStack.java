package flapi.recipe.stack;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import flapi.collection.abs.AbstractStack;

public class BaseStack extends ItemAbstractStack
{
	public ItemStack stack;
	
	public BaseStack(Item item){this(item, OreDictionary.WILDCARD_VALUE);}
	public BaseStack(Item item, int meta){this(new ItemStack(item, 1, meta));}
	public BaseStack(Block block){this(block, OreDictionary.WILDCARD_VALUE);}
	public BaseStack(Block block, int meta){this(new ItemStack(block, 1, meta));}
	public BaseStack(ItemStack item)
	{
		stack = item;
		if(stack != null) stack = stack.copy();
	}

	@Override
	public boolean equal(ItemStack arg)
	{
		return OreDictionary.itemMatches(stack, arg, false);
	}
	
	@Override
	public boolean equal(AbstractStack<ItemStack> arg)
	{
		return arg instanceof BaseStack ? ItemStack.areItemStacksEqual(stack, ((BaseStack) arg).stack) : false;
	}

	@Override
	public boolean contain(AbstractStack<ItemStack> arg)
	{
		return arg instanceof BaseStack ? stack.isItemEqual(((BaseStack) arg).stack) && stack.stackSize >= ((BaseStack) arg).stack.stackSize : false;
	}

	@Override
	public ItemStack[] toList()
	{
		return new ItemStack[]{stack};
	}
}