package farcore.recipe.crafting;

import java.util.List;

import farcore.recipe.stack.IItemStackMatcher;
import net.minecraft.item.ItemStack;

public class SingleMatcherRecipe implements ISingleInputRecipe
{
	byte type = 0;
	IItemStackMatcher matcher;
	
	public SingleMatcherRecipe setType(int type)
	{
		this.type = (byte) (type & 0xFF);
		return this;
	}
	
	public SingleMatcherRecipe(IItemStackMatcher matcher)
	{
		this.matcher = matcher;
	}

	@Override
	public boolean match(ItemStack stack)
	{
		return matcher.match(stack);
	}

	@Override
	public int sizeRequire(ItemStack stack)
	{
		return matcher.sizeRequire(stack);
	}

	@Override
	public List<ItemStack> list()
	{
		return matcher.list();
	}

	@Override
	public ItemStack output(ItemStack stack)
	{
		switch (type)
		{
		case 0:
			stack.stackSize--;
			if(stack.stackSize == 0)
				return null;
			return stack;
		default:
			return null;
		}
	}

	@Override
	public ItemStack displayOutput()
	{
		return null;
	}
}