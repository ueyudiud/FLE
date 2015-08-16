package fle.api.recipe;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemOreStack extends ItemAbstractStack
{
	private String target;
	
	public ItemOreStack(String aOreName)
	{
		target = aOreName;
	}

	@Override
	public boolean isStackEqul(ItemStack aItem) 
	{
		for(ItemStack tStack : OreDictionary.getOres(target))
		{
			if(OreDictionary.itemMatches(tStack, aItem, false)) return true;
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
		return stack instanceof ItemOreStack ? ((ItemOreStack) stack).target == target : false;
	}

	@Override
	public List<ItemStack> toArray() 
	{
		return OreDictionary.getOres(target, false);
	}
	
	@Override
	public String toString()
	{
		return "stack.ore." + target;
	}
}