package farcore.tile.inventory;

import static farcore.tile.inventory.InvHelper.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import farcore.recipe.IItemMatrix;
import farcore.recipe.IMatrixInputRecipe;
import farcore.recipe.stack.IIStackCheckerMatcher;
import farcore.recipe.stack.IItemChecker;
import farcore.recipe.stack.IItemStackMatcher;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface ISmartInventory extends IInventory
{
	public static class InvMatrix implements IItemMatrix
	{
		ISmartInventory $this;
		InvLocate locate;
		
		public InvMatrix(ISmartInventory inventory, char chr)
		{
			$this = inventory;
			locate = inventory.getControlor().getLocate(chr);
		}

		@Override
		public int getXSize()
		{
			return locate.width;
		}

		@Override
		public int getYSize()
		{
			return locate.height;
		}

		@Override
		public ItemStack getStack(int x, int y)
		{
			return x < 0 || y < 0 || x >= locate.width || y >= locate.height ?
					null :
					$this.getStackInSlot(locate.startID + locate.width * y + x);
		}

		@Override
		public void setItemStack(int x, int y, ItemStack stack)
		{
			if(x < 0 || y < 0 || x >= locate.width || y >= locate.height)
				return;
			$this.setInventorySlotContents(
					locate.startID + locate.width * y + x, stack);
		}		
	}
	
	ItemStack[] getStacks();
	
	InvControlor getControlor();
	
	@Override
	default int getSizeInventory()
	{
		return getStacks().length;
	}
	
	@Override
	default ItemStack getStackInSlot(int index)
	{
		return getStacks()[index];
	}
	
	@Override
	default void setInventorySlotContents(int index, ItemStack stack)
	{
		if(stack != null)
			stack = stack.copy();
		getStacks()[index] = stack;
	}
	
	@Override
	default ItemStack getStackInSlotOnClosing(int index)
	{
		ItemStack stack = getStacks()[index];
		getStacks()[index] = null;
		return stack;
	}
	
	@Override
	default ItemStack decrStackSize(int index, int size)
	{
		if(getStacks()[index] == null) return null;
		ItemStack ret = getStacks()[index].copy();
		getStacks()[index].stackSize -= size;
		if(getStacks()[index].stackSize < 1) getStacks()[index] = null;
		ret.stackSize = Math.min(size, ret.stackSize);
		return ret;
	}
	
	@Override
	default int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	default void openInventory()
	{
		
	}
	
	@Override
	default void closeInventory()
	{
		
	}
	
	@Override
	default boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return getControlor().getLocate(index).flag1;
	}
	
	default boolean matchItemStack(int matchSlot, IItemChecker checker)
	{
		return matchInput(this, matchSlot, checker);
	}
	default boolean matchItemStack(int matchSlot, IItemStackMatcher checker)
	{
		return matchInput(this, matchSlot, checker);
	}
	default int matchShapedInput(char chr, IItemChecker[] checkers)
	{
		InvLocate locate = getControlor().getLocate(chr);
		if(locate != null)
			return matchShapedInventory(this, locate.startID, locate.endID, checkers);
		return -1;
	}
	default boolean matchShapelessInput(char chr, IItemChecker[] checkers)
	{
		InvLocate locate = getControlor().getLocate(chr);
		if(locate != null)
			return matchShapelessInventory(this, locate.startID, locate.endID, checkers);
		return false;
	}
	default int[] matchMatrixShaped(char chr, int w, int h, IItemChecker[] checkers, boolean mirror)
	{
		InvLocate locate = getControlor().getLocate(chr);
		if(locate != null)
			return matchShapedInventory(this, 
					locate.startID, locate.endID, locate.width, 
					locate.height, checkers, w, h, mirror);
		return null;
	}
	default int[] matchMatrixShaped(char chr, int w, int h, IMatrixInputRecipe[] recipe)
	{
		if(getControlor().getLocate(chr) != null)
		{
			IItemMatrix matrix = new InvMatrix(this, chr);
			return matchShapedInventory(matrix, w, h, recipe);
		}
		return null;
	}

	default int matchShaped(char chr, IItemStackMatcher[] checkers)
	{
		InvLocate locate = getControlor().getLocate(chr);
		if(locate != null)
			return matchShapedInventory(this, locate.startID, locate.endID, checkers);
		return -1;
	}
	default boolean matchShapelessInput(char chr, IItemStackMatcher[] checkers)
	{
		InvLocate locate = getControlor().getLocate(chr);
		if(locate != null)
			return matchShapelessInventory(this, locate.startID, locate.endID, checkers);
		return false;
	}
	
	default boolean matchOutput(int index, ItemStack output)
	{
		return matchShapeless(this, index, output);
	}
	default boolean matchOutput(char chr, ItemStack output)
	{
		InvLocate locate = getControlor().getLocate(chr);
		if(locate != null)
			return matchShapeless(this, locate.startID, locate.endID, output);
		return false;
	}
	default boolean matchOutput(char chr, ItemStack[] output)
	{
		InvLocate locate = getControlor().getLocate(chr);
		if(locate != null)
			return matchShapeless(this, locate.startID, locate.endID, output);
		return false;
	}

	default void onInputMatrixShaped(char chr, int[] idx, int width, int height, IMatrixInputRecipe[] recipe)
	{
		if(getControlor().getLocate(chr) != null)
		{
			IItemMatrix matrix = new InvMatrix(this, chr);
			onInputShapedInventory(matrix, idx, width, height, recipe);
		}
	}
	default void onInputShaped(char chr, int idx, IItemChecker[] checkers)
	{
		InvLocate locate = getControlor().getLocate(chr);
		if(locate != null)
			onInputShapedInventory(this, locate.startID, locate.endID, idx, checkers);
	}
	default void onInputShaped(char chr, int idx, IItemStackMatcher[] checkers)
	{
		InvLocate locate = getControlor().getLocate(chr);
		if(locate != null)
			onInputShapedInventory(this, locate.startID, locate.endID, idx, checkers);
	}
	default void onInputShaped(char chr, int[] idx, IItemChecker[] checkers, int checkW, int checkH)
	{
		InvLocate locate = getControlor().getLocate(chr);
		if(locate != null)
			onInputShapedInventory(this, locate.startID, locate.endID, locate.width, locate.height, idx, checkW, checkH);
	}
	default void onInputShaped(char chr, int[] idx, IItemStackMatcher[] checkers, int checkW, int checkH)
	{
		InvLocate locate = getControlor().getLocate(chr);
		if(locate != null)
			onInputShapedInventory(this, locate.startID, locate.endID, locate.width, locate.height, idx, checkers, checkW, checkH);
	}

	default void onOutput(int index, ItemStack stack)
	{
		onOutputItemStack(this, index, stack);
	}
	default void onOutput(char chr, ItemStack stack)
	{
		InvLocate locate = getControlor().getLocate(chr);
		if(locate != null)
			onOutputShapelessStack(this, locate.startID, locate.endID, stack);
	}
	default void onOutput(char chr, ItemStack[] stacks)
	{
		InvLocate locate = getControlor().getLocate(chr);
		if(locate != null)
			onOutputShapelessStacks(this, locate.startID, locate.endID, stacks);
	}
}