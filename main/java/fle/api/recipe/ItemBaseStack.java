package fle.api.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemBaseStack extends ItemAbstractStack
{
	private net.minecraft.item.ItemStack stack;

	public ItemBaseStack(Block aBlock) 
	{
		this(aBlock, OreDictionary.WILDCARD_VALUE);
	}
	public ItemBaseStack(Block aBlock, int aMeta) 
	{
		this(new net.minecraft.item.ItemStack(aBlock, 1, aMeta));
	}
	public ItemBaseStack(Item aItem) 
	{
		this(aItem, OreDictionary.WILDCARD_VALUE);
	}
	public ItemBaseStack(Item aItem, int aMeta) 
	{
		this(new net.minecraft.item.ItemStack(aItem, 1, aMeta));
	}
	public ItemBaseStack(net.minecraft.item.ItemStack aStack) 
	{
		if(aStack != null)
			stack = aStack.copy();
	}

	@Override
	public boolean isStackEqul(net.minecraft.item.ItemStack item)
	{
		return OreDictionary.itemMatches(stack, item, false);
	}

	@Override
	public boolean isStackEqul(FluidStack item) 
	{
		return false;
	}

	@Override
	public boolean isStackEqul(ItemAbstractStack stack) 
	{
		return stack instanceof ItemBaseStack ? net.minecraft.item.ItemStack.areItemStacksEqual(((ItemBaseStack) stack).stack, this.stack) : false;
	}

	@Override
	public List<net.minecraft.item.ItemStack> toArray() 
	{
		List<net.minecraft.item.ItemStack> tList = new ArrayList();
		if(stack != null)
			tList.add(stack.copy());
		return tList;
	}
	
	@Override
	public String toString()
	{
		return "stack.base." + stack.getUnlocalizedName() + "x" + stack.stackSize;
	}
}