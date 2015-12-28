package flapi.recipe;

import net.minecraft.item.ItemStack;
import flapi.chem.base.MatterStack;
import flapi.recipe.stack.ItemAbstractStack;

public class StandardSingleMatterOutputRecipe implements SingleMatterOutputRecipe
{
	ItemAbstractStack stack;
	MatterStack output;
	
	public StandardSingleMatterOutputRecipe(ItemAbstractStack stack, MatterStack output)
	{
		this.stack = stack;
		this.output = output.copy();
	}

	@Override
	public boolean matchInput(ItemStack stack)
	{
		return this.stack.equal(stack);
	}

	@Override
	public ItemStack[] getShowStack()
	{
		return stack.toList();
	}

	@Override
	public MatterStack getOutput(ItemStack stack)
	{
		if(stack == null) return output.copy();
		MatterStack ret = output.copy();
		ret.size *= stack.stackSize;
		return ret;
	}
}