package flapi.recipe.stack;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;

public abstract class TemplateStack extends AbstractStack
{
	private ImmutableList<ItemStack> list;
	private ItemStack[] stacks;
	
	@Override
	public boolean contain(ItemStack stack)
	{
		return similar(stack) && size(stack) <= stack.stackSize;
	}
	
	protected abstract List<ItemStack> create();
	
	@Override
	public List<ItemStack> list()
	{
		if(list == null)
		{
			list = ImmutableList.copyOf(create());
		}
		return list;
	}
	
	@Override
	public ItemStack[] toList()
	{
		List<ItemStack> list = list();
		stacks = list.toArray(new ItemStack[list.size()]);
		return stacks;
	}
}