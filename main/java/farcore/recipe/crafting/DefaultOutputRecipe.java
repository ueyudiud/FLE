package farcore.recipe.crafting;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;

public class DefaultOutputRecipe implements ISingleInputRecipe
{
	private ItemStack output;

	public DefaultOutputRecipe(ItemStack stack)
	{
		this.output = stack.copy();
	}
	
	@Override
	public boolean match(ItemStack stack)
	{
		return true;
	}

	@Override
	public int sizeRequire(ItemStack stack)
	{
		return 1;
	}

	@Override
	public List<ItemStack> list()
	{
		return ImmutableList.of();
	}

	@Override
	public ItemStack output(ItemStack stack)
	{
		return output.copy();
	}

	@Override
	public ItemStack displayOutput()
	{
		return output.copy();
	}
}