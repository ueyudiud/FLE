package fle.api.recipes;

import java.util.Collection;
import java.util.List;

import farcore.data.EnumToolType;
import farcore.lib.collection.ArrayIterator;
import farcore.lib.item.ITool;
import farcore.lib.stack.AbstractStack;
import farcore.lib.stack.ArrayStack;
import farcore.lib.stack.BaseStack;
import farcore.lib.stack.OreStack;
import farcore.util.U;
import fle.api.recipes.ShapedRecipeItemInput.RecipeItemInputConfig;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ShapedRecipeItemInput extends ShapedRecipeInput<ItemStack, RecipeItemInputConfig>
{
	public static class RecipeItemInputConfig
	{
		AbstractStack input;
		
		/**
		 * 0 for use item.
		 * 1 for damage tool.
		 * 2 for Far Core tool checking.
		 */
		byte type;
		boolean neededSizeSimilar;

		int givebackChance;
		AbstractStack giveback;
		
		EnumToolType toolType;
		int levelRequire;
		float damageAmount;

		public RecipeItemInputConfig(EnumToolType type, int levelRequire, float damageAmt)
		{
			this(type.stack(), (byte) 2, true, 0, null, damageAmt);
			toolType = type;
			this.levelRequire = levelRequire;
			damageAmount = damageAmt;
		}
		public RecipeItemInputConfig(AbstractStack input, float damageAmt)
		{
			this(input, (byte) 1, true, 0, null, damageAmt);
		}
		public RecipeItemInputConfig(AbstractStack input)
		{
			this(input, false, null);
		}
		public RecipeItemInputConfig(AbstractStack input, boolean neededSizeSimilar, AbstractStack giveback)
		{
			this(input, neededSizeSimilar, 10000, giveback);
		}
		public RecipeItemInputConfig(AbstractStack input, boolean neededSizeSimilar, int givebackChance, AbstractStack giveback)
		{
			this(input, (byte) 0, neededSizeSimilar, givebackChance, giveback, 0);
		}
		public RecipeItemInputConfig(AbstractStack input, byte type, boolean neededSizeSimilar, int givebackChance, AbstractStack giveback, float damageAmount)
		{
			this.input = input;
			this.type = type;
			this.neededSizeSimilar = neededSizeSimilar;
			this.givebackChance = givebackChance;
			this.giveback = giveback;
			this.damageAmount = damageAmount;
		}
	}
	
	static RecipeItemInputConfig decode$(ArrayIterator<Object> itr)
	{
		Object object = itr.next();
		if(object instanceof RecipeItemInputConfig)
			return (RecipeItemInputConfig) object;
		else
		{
			Object obj1;
			int size = 1;
			if(itr.hasNext())
			{
				if((obj1 = itr.next()) instanceof Integer)
				{
					size = (Integer) obj1;
				}
				else
				{
					itr.previous();
				}
			}
			return new RecipeItemInputConfig(U.ItemStacks.sizeOf(decodeStack(object), size));
		}
	}
	
	@Override
	protected RecipeItemInputConfig decode(ArrayIterator<Object> itr)
	{
		return decode$(itr);
	}

	private static AbstractStack decodeStack(Object object)
	{
		if(object instanceof Item)
			return new BaseStack((Item) object);
		if(object instanceof Block)
			return new BaseStack((Block) object);
		if(object instanceof ItemStack)
			return new BaseStack((ItemStack) object);
		if(object instanceof Collection)
			return new ArrayStack((Collection<ItemStack>) object);
		if(object instanceof ItemStack[])
			return new ArrayStack((ItemStack[]) object);
		if(object instanceof String)
			return new OreStack((String) object);
		return null;
	}

	public static boolean matchInput$(RecipeItemInputConfig arg, ItemStack target)
	{
		if(target == null) return false;
		switch (arg.type)
		{
		case 0 :
		case 1 :
			return arg.neededSizeSimilar ? arg.input.similar(target) && arg.input.size(target) == target.stackSize : arg.input.contain(target);
		case 2 :
			List<EnumToolType> types = U.ItemStacks.getCurrentToolType(target);
			if(!types.contains(arg.toolType)) return false;
			if(arg.levelRequire != -1 && arg.levelRequire >= U.ItemStacks.getToolLevel(target, arg.toolType)) return false;
			return true;
		default:
			return false;
		}
	}
	
	@Override
	protected boolean matchInput(RecipeItemInputConfig arg, ItemStack target)
	{
		return matchInput$(arg, target);
	}
	
	@Override
	protected void onInput(int x, int y, RecipeItemInputConfig arg,
			ICraftingMatrix<ItemStack> matrix)
	{
		ItemStack stack;
		int size;
		switch (arg.type)
		{
		case 0 :
			size = arg.input.size(stack = matrix.get(x, y));
			if(arg.giveback != null && (arg.neededSizeSimilar || size >= stack.stackSize))
			{
				if(arg.givebackChance == 10000 || U.L.nextInt(10000) < arg.givebackChance)
				{
					matrix.set(x, y, arg.giveback.instance());
				}
				else
				{
					matrix.set(x, y, null);
				}
			}
			else
			{
				stack.stackSize -= size;
			}
			break;
		case 1 :
			(stack = matrix.get(x, y)).damageItem((int) arg.damageAmount, null);
			if(stack.stackSize <= 0)
			{
				matrix.set(x, y, null);
			}
		case 2 :
			stack = matrix.get(x, y);
			if(stack.getItem() instanceof ITool)
			{
				((ITool) stack.getItem()).onToolUse(null, stack, arg.toolType, arg.damageAmount);
				if(stack.stackSize <= 0)
				{
					matrix.set(x, y, null);
				}
			}
			else
			{
				stack.damageItem((int) arg.damageAmount, null);
				if(stack.stackSize <= 0)
				{
					matrix.set(x, y, null);
				}
			}
		default:
			break;
		}
	}
	
	@Override
	protected boolean isValid(RecipeItemInputConfig source)
	{
		return source.input.valid() && (source.giveback == null || source.giveback.valid());
	}
}