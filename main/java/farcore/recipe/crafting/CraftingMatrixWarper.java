package farcore.recipe.crafting;

import java.util.Arrays;
import java.util.List;

import farcore.recipe.IItemMatrix;
import farcore.tile.inventory.MatrixWarper;
import farcore.util.Util;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class CraftingMatrixWarper implements IItemMatrix
{
	public static IItemMatrix warp(InventoryCrafting crafting)
	{
		return new CraftingMatrixWarper(crafting);
	}
	
	private static final List<String> widthList = Arrays.asList("inventoryWidth", "field_70464_b");
	
	public final int width;
	public final int height;
	private final InventoryCrafting inventory;
	
	public CraftingMatrixWarper(InventoryCrafting crafting)
	{
		this.width = (int)Util.getValue(InventoryCrafting.class, 
				widthList, crafting);
		this.height = crafting.getSizeInventory() / width;
		this.inventory = crafting;
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
		return y < 0 || y >= height ? null :
			inventory.getStackInRowAndColumn(x, y);
	}

	@Override
	public void setItemStack(int x, int y, ItemStack stack)
	{
		if(x < 0 || y < 0 || x >= width || y >= height) return;
		inventory.setInventorySlotContents(x + y * width, stack);
	}
}