package farcore.recipe;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IMatrixInputRecipe extends IRecipe
{
	boolean match(int x, int y, IItemMatrix matrix);
	
	void output(int x, int y, IItemMatrix matrix);
	
	List<ItemStack> displayInput();
	
	ItemStack displayOutput();
}