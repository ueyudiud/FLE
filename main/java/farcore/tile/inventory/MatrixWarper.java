package farcore.tile.inventory;

import java.util.Arrays;
import java.util.List;

import farcore.recipe.IItemMatrix;
import farcore.util.Util;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class MatrixWarper implements IItemMatrix
{
	public static IItemMatrix warp(IItemMatrix matrix)
	{
		return new MatrixWarper(matrix);
	}
	public static void copyOf(IItemMatrix resource, IItemMatrix target)
	{
		for(int x = 0; x < target.getXSize(); ++x)
			for(int y = 0; y < target.getYSize(); ++y)
				target.setItemStack(x, y, resource.getStack(x, y));
	}
	
	private ItemStack[] stacks;
	private int width;
	private int height;

	private MatrixWarper(IItemMatrix matrix)
	{
		width = matrix.getXSize();
		height = matrix.getYSize();
		stacks = new ItemStack[width * height];
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
			{
				ItemStack stack = matrix.getStack(i, j);
				stacks[width * i + j] = stack != null ? stack.copy() : null;
			}
	}

	@Override
	public int getXSize()
	{
		return width;
	}

	@Override
	public int getYSize()
	{
		return height;
	}

	@Override
	public ItemStack getStack(int x, int y)
	{
		return x < 0 || y < 0 || x >= width || y >= height ? null :
			stacks[width * y + x];
	}

	@Override
	public void setItemStack(int x, int y, ItemStack stack)
	{
		if(x < 0 || y < 0 || x >= width || y >= height) return;
		if(stack != null)
			stack = stack.copy();
		stacks[width * y + x] = stack;
	}
}