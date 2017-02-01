package fle.api.recipes;

import fle.api.recipes.ShapedRecipeItemInput.RecipeItemInputConfig;
import nebula.common.base.ArrayIterator;
import nebula.common.item.ITool;
import nebula.common.util.L;
import net.minecraft.item.ItemStack;

public class SingleRecipeItemInput extends SingleRecipeInput<RecipeItemInputConfig, ICraftingMatrix<ItemStack>, ItemStack>
{
	@Override
	protected RecipeItemInputConfig decode(Object... objects)
	{
		return ShapedRecipeItemInput.decode$(new ArrayIterator(objects));
	}

	@Override
	protected boolean match(ItemStack tsourceet)
	{
		return ShapedRecipeItemInput.matchInput$(source, tsourceet);
	}

	@Override
	protected void onInput(int id, ICraftingMatrix<ItemStack> matrix)
	{
		ItemStack stack;
		int size;
		switch (source.type)
		{
		case 0 :
			size = source.input.size(stack = matrix.get(id));
			if(source.giveback != null && (source.neededSizeSimilar || size >= stack.stackSize))
			{
				if(source.givebackChance == 10000 || L.nextInt(10000) < source.givebackChance)
				{
					matrix.set(id, source.giveback.instance());
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
			(stack = matrix.get(id)).damageItem((int) source.damageAmount, null);
			if(stack.stackSize <= 0)
			{
				matrix.set(id, null);
			}
		case 2 :
			stack = matrix.get(id);
			if(stack.getItem() instanceof ITool)
			{
				((ITool) stack.getItem()).onToolUse(null, stack, source.toolType, source.damageAmount);
				if(stack.stackSize <= 0)
				{
					matrix.set(id, null);
				}
			}
			else
			{
				stack.damageItem((int) source.damageAmount, null);
				if(stack.stackSize <= 0)
				{
					matrix.set(id, null);
				}
			}
		default:
			break;
		}
	}
	
	@Override
	public boolean isValid()
	{
		return source.input.valid() && (source.giveback == null || source.giveback.valid());
	}
}