package farcore.tile.inventory;

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

public class InvHelper
{	
	public static boolean matchInput(IInventory inv, int matchSlot, 
			IItemChecker checker)
	{
		return inv.getStackInSlot(matchSlot) != null ? 
				checker.match(inv.getStackInSlot(matchSlot)) : 
					checker == null;
	}
	public static boolean matchInput(IInventory inv, int matchSlot, 
			IItemStackMatcher matcher)
	{
		return inv.getStackInSlot(matchSlot) != null ? 
				matcher.match(inv.getStackInSlot(matchSlot)) : 
					matcher == null;
	}
	public static int matchShapedInventory(IInventory inv, int startSlot, 
			int endSlot, IItemChecker[] checkers)
	{
		return matchShapedInventory(inv, startSlot, endSlot, 
				endSlot - startSlot, 1, checkers, checkers.length, 1, false)[0];
	}
	public static int matchShapedInventory(IInventory inv, int startSlot, 
			int endSlot, IItemStackMatcher[] checkers)
	{
		return matchShapedInventory(inv, startSlot, endSlot, 
				endSlot - startSlot, 1, checkers, checkers.length, 1, false)[0];
	}
	public static int[] matchShapedInventory(IInventory inv, int startSlot, 
			int endSlot, int width, int height, IItemChecker[] checkers,
			int checkWidth, int checkHeight)
	{
		return matchShapedInventory(inv, startSlot, endSlot, 
				width, height, checkers, checkWidth, checkHeight, false);
	}
	public static int[] matchShapedInventory(IInventory inv, int startSlot, 
			int endSlot, int width, int height, IItemStackMatcher[] checkers,
			int checkWidth, int checkHeight)
	{
		return matchShapedInventory(inv, startSlot, endSlot, 
				width, height, checkers, checkWidth, checkHeight, false);
	}
	public static int[] matchShapedInventory(IItemMatrix matrix, int width, int height, IMatrixInputRecipe[] recipe)
	{
		if(width > matrix.getXSize() || height > matrix.getYSize()) return null;
		for(int xOffset = 0; xOffset <= matrix.getXSize() - width; ++xOffset)
			for(int yOffset = 0; yOffset <= matrix.getYSize() - height; ++yOffset)
			{
				if(match$1(matrix, xOffset, yOffset, width, height, recipe, false))
					return new int[]{xOffset, yOffset, 0};
			}
		return null;
	}
	public static int[] matchShapedInventory(IInventory inv, int startSlot, 
			int endSlot, int width, int height, IItemChecker[] checkers,
			int checkWidth, int checkHeight, boolean mirror)
	{
		int uStartSlot = endSlot - checkers.length;
		if(uStartSlot < 0 || uStartSlot >= inv.getSizeInventory()) 
			return null;
		if(width < checkWidth || height < checkHeight) return null;
		for(int xOffset = 0; xOffset <= width - checkWidth; ++xOffset)
			for(int yOffset = 0; yOffset <= height - checkHeight; ++yOffset)
			{
				if(mirror)
					if(match$1(inv, uStartSlot, xOffset, yOffset, width, height, 
							checkers, checkWidth, checkHeight, true))
						return new int[]{xOffset, yOffset, 1};
				if(match$1(inv, uStartSlot, xOffset, yOffset, width, height, 
						checkers, checkWidth, checkHeight, false))
					return new int[]{xOffset, yOffset, 0};
			}
		return null;
	}
	public static int[] matchShapedInventory(IInventory inv, int startSlot, 
			int endSlot, int width, int height, IItemStackMatcher[] checkers,
			int checkWidth, int checkHeight, boolean mirror)
	{
		int uStartSlot = endSlot - checkers.length;
		if(uStartSlot < 0 || uStartSlot >= inv.getSizeInventory()) 
			return null;
		if(width < checkWidth || height < checkHeight) return null;
		for(int xOffset = 0; xOffset <= width - checkWidth; ++xOffset)
			for(int yOffset = 0; yOffset <= height - checkHeight; ++yOffset)
			{
				if(mirror)
					if(match$1(inv, uStartSlot, xOffset, yOffset, width, height, 
							checkers, checkWidth, checkHeight, true))
						return new int[]{xOffset, yOffset, 1};
				if(match$1(inv, uStartSlot, xOffset, yOffset, width, height, 
						checkers, checkWidth, checkHeight, false))
					return new int[]{xOffset, yOffset, 0};
			}
		return null;
	}
	static boolean match$1(IItemMatrix matrix, int oX, int oY, int w, int h, IMatrixInputRecipe[] recipe, boolean mirror)
	{
		for(int i1 = 0; i1 < w; ++i1)
			for(int j1 = 0; j1 < h; ++j1)
			{
				ItemStack stack = mirror ?
						matrix.getStack(oX + w - i1 - 1, oY + j1) :
						matrix.getStack(oX + i1, oY + j1);
				IMatrixInputRecipe checker = recipe[w * i1 + j1];
				if(checker == null)
					if(stack != null) return false;
					else continue;
				if(!checker.match(oX + i1, oY + j1, matrix))
					return false;
			}
		return true;
	}
	static boolean match$1(IInventory inv, int startSlot, int w, int h, 
			int width, int height, IItemChecker[] checkers, 
			int checkWidth, int checkHeight, boolean mirror)
	{
		for(int i1 = 0; i1 < checkWidth; ++i1)
			for(int j1 = 0; j1 < checkHeight; ++j1)
			{
				ItemStack stack = mirror ?
						matrixGet(inv, startSlot, width, height, w + checkWidth - i1 - 1, h + j1) :
						matrixGet(inv, startSlot, width, height, w + i1, h + j1);
				IItemChecker checker = checkers[checkHeight * i1 + j1];
				if(checker == null)
				{
					if(stack != null) return false;
					continue;
				}
				if(!checker.match(stack)) return false;
			}
		return true;
	}
	static boolean match$1(IInventory inv, int startSlot, int w, int h, 
			int width, int height, IItemStackMatcher[] checkers, 
			int checkWidth, int checkHeight, boolean mirror)
	{
		for(int i1 = 0; i1 < checkWidth; ++i1)
			for(int j1 = 0; j1 < checkHeight; ++j1)
			{
				ItemStack stack = mirror ?
						matrixGet(inv, startSlot, width, height, w + checkWidth - i1 - 1, h + j1) :
						matrixGet(inv, startSlot, width, height, w + i1, h + j1);
				IItemStackMatcher checker = checkers[checkHeight * i1 + j1];
				if(checker == null)
				{
					if(stack != null) return false;
					continue;
				}
				if(!checker.match(stack)) return false;
			}
		return true;
	}
	static ItemStack matrixGet(IInventory inv, int startID, 
			int width, int height, int x, int y)
	{
		if(x < 0 || y < 0 || x >= width || y >= height)
		{
			return null;
		}
		return inv.getStackInSlot(startID + x + y * width);
	}
//	public static boolean matchShapelessInventory(IInventory inv, int[] matchSlots, IItemChecker[] ic)
//	{
//		List<IItemChecker> list = new ArrayList();
//		list.addAll(Arrays.asList(ic));
//		
//		for(int i : matchSlots)
//		{
//			boolean flag = false;
//			ItemStack stack = inv.getStackInSlot(i);
//			if(stack == null) continue;
//			Iterator<IItemChecker> itr = list.iterator();
//			while(itr.hasNext())
//			{
//				IItemChecker checker = itr.next();
//				if(checker.match(stack))
//				{
//					flag = true;
//					list.remove(checker);
//					break;
//				}
//			}
//			if(!flag) return false;
//		}
//		return list.isEmpty();
//	}
	public static boolean matchShapelessInventory(
			IInventory inv, int startSlot, int endSlot, IItemChecker[] ic)
	{
		List<IItemChecker> list = new ArrayList();
		list.addAll(Arrays.asList(ic));
		
		for(int i = startSlot; i < endSlot; ++i)
		{
			boolean flag = false;
			ItemStack stack = inv.getStackInSlot(i);
			if(stack == null) continue;
			Iterator<IItemChecker> itr = list.iterator();
			while(itr.hasNext())
			{
				IItemChecker checker = itr.next();
				if(checker.match(stack))
				{
					flag = true;
					list.remove(checker);
					break;
				}
			}
			if(!flag) return false;
		}
		return list.isEmpty();
	}
	public static boolean matchShapelessInventory(
			IInventory inv, int startSlot, int endSlot, IItemStackMatcher[] ic)
	{
		List<IItemStackMatcher> list = new ArrayList();
		list.addAll(Arrays.asList(ic));
		
		for(int i = startSlot; i < endSlot; ++i)
		{
			boolean flag = false;
			ItemStack stack = inv.getStackInSlot(i);
			if(stack == null) continue;
			Iterator<IItemStackMatcher> itr = list.iterator();
			while(itr.hasNext())
			{
				IItemStackMatcher checker = itr.next();
				if(checker.match(stack))
				{
					flag = true;
					list.remove(checker);
					break;
				}
			}
			if(!flag) return false;
		}
		return list.isEmpty();
	}
	public static boolean matchShapelessInventory(
			IInventory inv, int startSlot, int endSlot, IIStackCheckerMatcher[] ic)
	{
		Map<IIStackCheckerMatcher, Integer> map = new HashMap();
		for(IIStackCheckerMatcher matcher : ic)
		{
			map.put(matcher, matcher.size());
		}
		IInventory warper = InventoryWarper.warp(inv);
		for(int i = startSlot; i < endSlot; ++i)
		{
			boolean flag = false;
			ItemStack stack = warper.getStackInSlot(i);
			if(stack == null) continue;
			stack = stack.copy();
			Iterator<Entry<IIStackCheckerMatcher, Integer>> itr = map.entrySet().iterator();
			while(itr.hasNext())
			{
				if(stack.stackSize <= 0) break;
				Entry<IIStackCheckerMatcher, Integer> checker = itr.next();
				if(checker.getKey().checker().match(stack))
				{
					int size = checker.getValue();
					int j;
					ItemStack decr = warper
							.decrStackSize(i, j = Math.min(size, stack.stackSize));
					stack.stackSize -= j;
					if(size == j)
						map.remove(checker.getKey());
					else
						map.put(checker.getKey(), size - j);
				}
			}
			if(map.isEmpty()) return true;
		}
		return map.isEmpty();
	}

	public static boolean matchShapeless(IInventory inv, int outputSlot, ItemStack output)
	{
		return inv.getStackInSlot(outputSlot) == null ? true : 
			output == null ? true : 
				ItemStack.areItemStackTagsEqual(inv.getStackInSlot(outputSlot), output) && 
				output.isItemEqual(inv.getStackInSlot(outputSlot)) && 
				inv.getStackInSlot(outputSlot).stackSize + output.stackSize <= getMaxSizeLimit(inv, output);
	}
//	public static boolean matchOutput(IInventory inv, int[] outputSlots, ItemStack output)
//	{
//		for(int i : outputSlots)
//			if(matchOutput(inv, i, output))
//				return true;
//		return false;
//	}
	public static boolean matchShapeless(IInventory inv, int startSlot, int endSlot, ItemStack output)
	{
		if(output == null) return true;
		ItemStack stack = output.copy();
		for(int i = startSlot; i < endSlot; ++i)
		{
			int a = addIn(false, inv, i, stack);
			stack.stackSize -= a;
			if(stack.stackSize == 0) return true;
		}
		return stack.stackSize == 0;
	}
	public static boolean matchShapeless(IInventory inv, int startSlot, int endSlot, ItemStack[] output)
	{
		IInventory inv1 = InventoryWarper.warp(inv);
		for(int i = 0; i < output.length; ++i)
		{
			if(output[i] == null) continue;
			ItemStack stack = output[i].copy();
			for(int j = startSlot; j < endSlot; ++j)
			{
				int a = addIn(true, inv1, j, stack);
				stack.stackSize -= a;
				if(stack.stackSize == 0) break;
			}
			if(stack.stackSize != 0) return false;
		}
		return true;
	}
//	public static boolean matchOutput(IInventory inv, int[] outputSlots, ItemStack[] output)
//	{
//		IInventory inv1 = InventoryWarper.warp(inv);
//		for(int i = 0; i < output.length; ++i)
//		{
//			if(output[i] == null) continue;
//			ItemStack stack = output[i].copy();
//			for(int j : outputSlots)
//			{
//				int a = addIn(true, inv1, j, stack);
//				stack.stackSize -= a;
//				if(stack.stackSize == 0) break;
//			}
//			if(stack.stackSize != 0) return false;
//		}
//		return true;
//	}

	public static void onInputItemStack(IInventory inv, int slotSlot) 
	{
		inv.decrStackSize(slotSlot, 1);
	}	
	public static void onInputShapedInventory(
			IInventory inv, int startSlot, int endSlot, int offset, IItemChecker[] ic) 
	{
		onInputShapedInventory(inv, startSlot, endSlot, endSlot - startSlot, 
				1, new int[]{offset, 0, 0}, ic.length, 1);
	}	
	public static void onInputShapedInventory(
			IInventory inv, int startSlot, int endSlot, int offset, IItemStackMatcher[] ic) 
	{
		onInputShapedInventory(inv, startSlot, endSlot, endSlot - startSlot, 
				1, new int[]{offset, 0, 0}, ic.length, 1);
	}
	public static void onInputShapedInventory(IItemMatrix matrix, int[] idx, int w, int h, IMatrixInputRecipe[] recipe)
	{
		IItemMatrix matrix2 = MatrixWarper.warp(matrix);
		for(int i = 0; i < w; ++i)
			for(int j = 0; j < h; ++j)
			{
				recipe[w * j + i].output
				(idx[2] == 0 ? idx[0] + i : idx[0] + w - i - 1, idx[1] + j, matrix2);
				MatrixWarper.copyOf(matrix2, matrix);
			}
	}
	public static void onInputShapedInventory(IInventory inv, 
			int startSlot, int endSlot, int width, int height, 
			int[] offset, int checkWidth, int checkHeight) 
	{
		for(int i = 0; i < checkWidth; ++i)
			for(int j = 0; j < checkHeight; ++j)
			{
				inv.decrStackSize(startSlot + (offset[1] + height) * width + (offset[0] + i), 1);
			}
	}
	public static void onInputShapedInventory(IInventory inv, 
			int startSlot, int endSlot, int width, int height, 
			int[] offset, IItemStackMatcher[] matchers, int checkWidth, int checkHeight) 
	{
		for(int i = 0; i < checkWidth; ++i)
			for(int j = 0; j < checkHeight; ++j)
			{
				int locate = offset[0] == 0 ?
						startSlot + (offset[1] + j) * width + (offset[0] + i) : 
							startSlot + (offset[1] + j) * width +
							(offset[0] + checkWidth - i - 1);
				ItemStack item = inv.getStackInSlot(locate);
				inv.decrStackSize(locate, 
						matchers[i + j * checkWidth].sizeRequire(item));
			}
	}
//	public static void onInputShaelessInventory(IInventory inv, int[] inputSlots, IItemChecker[] ic) 
//	{
//		List<IItemChecker> list = new ArrayList();
//		list.addAll(Arrays.asList(ic));
//		
//		for(int i : inputSlots)
//		{
//			ItemStack stack = inv.getStackInSlot(i);
//			if(stack == null) continue;
//			Iterator<IItemChecker> itr = list.iterator();
//			while(itr.hasNext())
//			{
//				IItemChecker checker = itr.next();
//				if(checker.match(stack))
//				{
//					inv.decrStackSize(i, 1);
//					list.remove(checker);
//					break;
//				}
//			}
//		}
//	}
	public static void onInputShaelessInventory(IInventory inv, int startSlot, 
			int endSlot, IItemChecker[] ic) 
	{
		List<IItemChecker> list = new ArrayList();
		list.addAll(Arrays.asList(ic));
		
		for(int i = startSlot; i < endSlot; ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(stack == null) continue;
			Iterator<IItemChecker> itr = list.iterator();
			while(itr.hasNext())
			{
				IItemChecker checker = itr.next();
				if(checker.match(stack))
				{
					inv.decrStackSize(i, 1);
					list.remove(checker);
					break;
				}
			}
		}
	}
	public static void onInputShaelessInventory(IInventory inv, int startSlot, 
			int endSlot, IItemStackMatcher[] ic) 
	{
		List<IItemStackMatcher> list = new ArrayList();
		list.addAll(Arrays.asList(ic));
		
		for(int i = startSlot; i < endSlot; ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(stack == null) continue;
			Iterator<IItemStackMatcher> itr = list.iterator();
			while(itr.hasNext())
			{
				IItemStackMatcher checker = itr.next();
				if(checker.match(stack))
				{
					inv.decrStackSize(i, checker.sizeRequire(stack));
					list.remove(checker);
					break;
				}
			}
		}
	}
	
	public static void onOutputItemStack(IInventory inv, int outputSlot, ItemStack output)
	{
		if(output == null) return;
		if(inv.getStackInSlot(outputSlot) == null) inv.setInventorySlotContents(outputSlot, output.copy());
		else inv.getStackInSlot(outputSlot).stackSize += output.stackSize;
	}	
	public static void onOutputShapelessStack(IInventory inv, int startSlot, int endSlot, ItemStack output)
	{
		if(output == null) return;
		ItemStack stack = output.copy();
		for(int i = startSlot; i < endSlot; ++i)
		{
			int a = addIn(true, inv, i, stack.copy());
			stack.stackSize -= a;
			if(stack.stackSize == 0) return;
		}
	}
//	public static void onOutputShapelessStack(IInventory inv, int[] outputSlots, ItemStack output)
//	{
//		if(output == null) return;
//		ItemStack stack = output.copy();
//		for(int i : outputSlots)
//		{
//			int a = addIn(true, inv, i, stack.copy());
//			stack.stackSize -= a;
//			if(stack.stackSize == 0) return;
//		}
//	}
	public static void onOutputShapelessStacks(IInventory inv, int startSlot, int endSlot, ItemStack[] output)
	{
		for(int i = 0; i < output.length; ++i)
			onOutputShapelessStack(inv, startSlot, endSlot, output[i]);
	}
//	public static void onOutputShapelessStacks(IInventory inv, int[] outputSlots, ItemStack[] output)
//	{
//		for(int i = 0; i < output.length; ++i)
//			onOutputShapelessStack(inv, outputSlots, output[i]);
//	}
	
	static int addIn(boolean doAdd, IInventory inv, int outputSlot, ItemStack output)
	{
		if(!doAdd)
		{
			return inv.getStackInSlot(outputSlot) == null ? output.stackSize : ItemStack.areItemStackTagsEqual(inv.getStackInSlot(outputSlot), output) && output.isItemEqual(inv.getStackInSlot(outputSlot)) ? Math.min(output.stackSize, getMaxSizeLimit(inv, output) - inv.getStackInSlot(outputSlot).stackSize) : 0;
		}
		else if(inv.getStackInSlot(outputSlot) == null) 
		{
			inv.setInventorySlotContents(outputSlot, output);
			return output.stackSize;
		}
		else if(ItemStack.areItemStackTagsEqual(inv.getStackInSlot(outputSlot), output) && output.isItemEqual(inv.getStackInSlot(outputSlot)))
		{
			int add = Math.min(output.stackSize, getMaxSizeLimit(inv, output) - inv.getStackInSlot(outputSlot).stackSize);
			inv.getStackInSlot(outputSlot).stackSize += add;
			return add;
		}
		else
		{
			return 0;
		}
	}
	
	static int getMaxSizeLimit(IInventory inv, ItemStack type)
	{
		return type != null ? Math.min(inv.getInventoryStackLimit(), type.getMaxStackSize()) : inv.getInventoryStackLimit();
	}
}