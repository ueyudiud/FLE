package flapi.recipe.stack;

import java.util.List;

import flapi.collection.abs.AbstractStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreStack extends ItemAbstractStack
{
	public String name;
	public int size;
	
	public OreStack(String name)
	{
		this(name, 1);
	}
	public OreStack(String name, int size)
	{
		this.name = name;
		this.size = size;
	}
	
	@Override
	public boolean equal(ItemStack arg)
	{
		for(ItemStack stack : OreDictionary.getOres(name))
		{
			if(OreDictionary.itemMatches(stack, arg, false))
				return true;
		}
		return false;
	}

	@Override
	public boolean equal(AbstractStack<ItemStack> arg)
	{
		if(!(arg instanceof OreStack)) return false;
		return name.equals(((OreStack) arg).name) && ((OreStack) arg).size == size;
	}

	@Override
	public boolean contain(AbstractStack<ItemStack> arg)
	{
		if(!(arg instanceof OreStack)) return false;
		return name.equals(((OreStack) arg).name) && ((OreStack) arg).size <= size;
	}

	@Override
	public ItemStack[] toList()
	{
		List<ItemStack> list = OreDictionary.getOres(name, true);
		return list.toArray(new ItemStack[list.size()]);
	}
}