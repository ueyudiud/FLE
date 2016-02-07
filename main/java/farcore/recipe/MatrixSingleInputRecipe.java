package farcore.recipe;

import java.util.List;

import farcore.recipe.crafting.ISingleInputRecipe;
import net.minecraft.item.ItemStack;

public class MatrixSingleInputRecipe implements IMatrixInputRecipe
{
	ISingleInputRecipe recipe;
	
	public MatrixSingleInputRecipe(ISingleInputRecipe recipe)
	{
		this.recipe = recipe;
	}

	@Override
	public boolean match(int x, int y, IItemMatrix matrix)
	{
		return recipe.match(matrix.getStack(x, y));
	}

	@Override
	public void output(int x, int y, IItemMatrix matrix)
	{
		matrix.setItemStack(x, y, recipe.output(matrix.getStack(x, y)));
	}
	
	@Override
	public List<ItemStack> displayInput()
	{
		return recipe.list();
	}
	
	@Override
	public ItemStack displayOutput()
	{
		return recipe.displayOutput();
	}
}