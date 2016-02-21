package flapi.recipe.stack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.collection.abs.AStack;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ArrayStack extends TemplateStack
{
	public static final ArrayStack NULL = new ArrayStack();
	
	private List<ItemStack> stacks;

	private ArrayStack(){}
	public ArrayStack(Item item                      ){this(item, OreDictionary.WILDCARD_VALUE);}
	public ArrayStack(Item item  ,           int meta){this(new ItemStack(item, 1, meta));}
	public ArrayStack(Item item  , int size, int meta){this(new ItemStack(item, size, meta));}
	public ArrayStack(Block block                    ){this(block, OreDictionary.WILDCARD_VALUE);}
	public ArrayStack(Block block,           int meta){this(new ItemStack(block, 1, meta));}
	public ArrayStack(Block block, int size, int meta){this(new ItemStack(block, size, meta));}
	public ArrayStack(ItemStack...stacks) 
	{
		this(ImmutableList.copyOf(stacks));
	}
	public ArrayStack(List<ItemStack> list)
	{
		stacks = list;
	}

	@Override
	public boolean similar(ItemStack item)
	{
		for(ItemStack tStack : stacks)
		{
			if(U.I.equal(tStack, item, false))
				return true;
		}
		return false;
	}
	
	@Override
	protected List<ItemStack> create()
	{
		ArrayList<ItemStack> list = new ArrayList();
		for(ItemStack stack : stacks)
		{
			list.add(U.I.copyAndValidate(stack, true));
		}
		return list;
	}
	
	@Override
	public int size(ItemStack stack)
	{
		return this.stacks.get(0).stackSize;
	}
	
	@Override
	public ItemStack instance()
	{
		return this.stacks.isEmpty() ? null : this.stacks.get(0).copy();
	}
	
	@Override
	public String toString()
	{
		return "{array:" + stacks + "}";
	}
}