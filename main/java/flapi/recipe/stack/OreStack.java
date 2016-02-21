package flapi.recipe.stack;

import java.util.List;

import farcore.collection.abs.AStack;
import farcore.util.U;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreStack extends TemplateStack
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
	public boolean similar(ItemStack stack)
	{
		if(stack == null) return false;
		for(ItemStack target : OreDictionary.getOres(name))
		{
			if(OreDictionary.itemMatches(target, stack, false))
				return true;
		}
		return false;
	}
	
	@Override
	public int size(ItemStack stack)
	{
		return size;
	}
	
	@Override
	protected List<ItemStack> create()
	{
		return null;//Unused
	}
	
	@Override
	public List<ItemStack> list()
	{
		return OreDictionary.getOres(name, false);
	}
	
	private ItemStack instance;
	
	@Override
	public ItemStack instance()
	{
		if(instance == null)
		{
			instance = U.I.stack(name, size);
		}
		return instance.copy();
	}

	@Override
	public String toString()
	{
		return "{ore:" + name + "x" + size + "}";
	}
}