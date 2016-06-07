package farcore.lib.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface ICraftingInventory extends ICraftingInventoryMatching
{
	void setStackInMatrix(int x, int y, ItemStack stack);

	void setStackInMatrix(int id, ItemStack stack);
	
	void setToolStack(int id, ItemStack stack);
	
	void setToolMatrialStack(int id, ItemStack stack);
	
	void decrStackInMatrix(int x, int y, int size);

	void decrStackInMatrix(int id, int size);
	
	void decrStackInTool(int id, int size);
	
	void decrStackInToolMatrial(int id, int size);
}