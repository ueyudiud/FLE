package farcore.recipe.stack;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class OM extends AM
{
	private boolean flag;
	private String name;

	public OM(String name)
	{
		this(name, false);
	}
	public OM(String name, boolean flag)
	{
		this.flag = flag;
		this.name = name;
	}
	
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof OM ? ((OM) obj).flag == flag : ((OM) obj).name == name;
	}

	@Override
	public boolean match(ItemStack stack)
	{
		for(ItemStack stack1 : OreDictionary.getOres(name))
		{
			if(OreDictionary.itemMatches(stack1, stack, flag))
				return true;
		}
		return false;
	}
	
	@Override
	protected Object createList()
	{
		return OreDictionary.getOres(name);
	}
}