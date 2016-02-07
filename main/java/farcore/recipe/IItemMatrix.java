package farcore.recipe;

import net.minecraft.item.ItemStack;

public interface IItemMatrix
{
	int getXSize();
	int getYSize();
	
	ItemStack getStack(int x, int y);
	
	void setItemStack(int x, int y, ItemStack stack);
}