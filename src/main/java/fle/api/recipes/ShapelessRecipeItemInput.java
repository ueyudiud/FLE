package fle.api.recipes;

import java.util.List;

import farcore.data.EnumToolType;
import farcore.lib.collection.IteratorList;
import farcore.lib.item.ITool;
import farcore.util.U;
import fle.api.recipes.ShapedRecipeItemInput.RecipeItemInputConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;

public class ShapelessRecipeItemInput extends ShapelessRecipeInput<ItemStack, RecipeItemInputConfig>
{
	@Override
	protected RecipeItemInputConfig decode(IteratorList<Object> itr)
	{
		return ShapedRecipeItemInput.decode$(itr);
	}
	
	@Override
	protected EnumActionResult matchInput(RecipeItemInputConfig arg, ItemStack target)
	{
		if(target == null) return EnumActionResult.PASS;
		switch (arg.type)
		{
		case 0 :
		case 1 :
			int size = arg.input.size(target);
			return arg.input.similar(target) ? ((arg.neededSizeSimilar ? size == target.stackSize : size <= target.stackSize) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL) : EnumActionResult.PASS;
		case 2 :
			List<EnumToolType> types = U.ItemStacks.getCurrentToolType(target);
			if(!types.contains(arg.toolType)) return EnumActionResult.PASS;
			if(arg.levelRequire != -1 && arg.levelRequire >= U.ItemStacks.getToolLevel(target, arg.toolType)) return EnumActionResult.FAIL;
			return EnumActionResult.SUCCESS;
		default:
			return EnumActionResult.PASS;
		}
	}
	
	@Override
	protected void onInput(int id, RecipeItemInputConfig arg, ICraftingMatrix<ItemStack> matrix)
	{
		ItemStack stack;
		int size;
		switch (arg.type)
		{
		case 0 :
			size = arg.input.size(stack = matrix.get(id));
			if(arg.giveback != null && (arg.neededSizeSimilar || size >= stack.stackSize))
			{
				if(arg.givebackChance == 10000 || U.L.nextInt(10000) < arg.givebackChance)
				{
					matrix.set(id, arg.giveback.instance());
				}
				else
				{
					matrix.set(id, null);
				}
			}
			else
			{
				stack.stackSize -= size;
			}
			break;
		case 1 :
			(stack = matrix.get(id)).damageItem((int) arg.damageAmount, null);
			if(stack.stackSize <= 0)
			{
				matrix.set(id, null);
			}
		case 2 :
			stack = matrix.get(id);
			if(stack.getItem() instanceof ITool)
			{
				((ITool) stack.getItem()).onToolUse(null, stack, arg.toolType, arg.damageAmount);
				if(stack.stackSize <= 0)
				{
					matrix.set(id, null);
				}
			}
			else
			{
				stack.damageItem((int) arg.damageAmount, null);
				if(stack.stackSize <= 0)
				{
					matrix.set(id, null);
				}
			}
		default:
			break;
		}
	}
}