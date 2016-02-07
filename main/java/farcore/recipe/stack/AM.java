package farcore.recipe.stack;

import java.util.List;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;

public abstract class AM implements IItemChecker
{
	protected ImmutableList<ItemStack> list;
	
	@Override
	public final List<ItemStack> list()
	{
		if(list == null)
		{
			Object object = createList();
			if(object == null)
				list = ImmutableList.of();
			else if(object instanceof ItemStack)
				list = ImmutableList.of((ItemStack) object);
			else if(object instanceof ItemStack[])
				list = ImmutableList.copyOf((ItemStack[]) object);
			else if(object instanceof List)
				list = ImmutableList.<ItemStack>copyOf((List<ItemStack>) object);
		}
		return list;
	}

	protected abstract Object createList();
}