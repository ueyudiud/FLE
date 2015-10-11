package fle.cg.recipe;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import fle.api.FleValue;
import fle.api.cg.IGuideType;
import fle.api.cg.StandardPage;
import fle.api.cg.StandardType;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemArrayStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.recipe.ShapelessFleRecipe;
import fle.core.util.Util;

public class FLEShapelessRecipe extends StandardType
{
	public static List<IGuidePage> list = new ArrayList();
	
	public static void register(Object recipe)
	{
		if(recipe instanceof ShapelessRecipes)
			list.add(new ShapelessPage((ShapelessRecipes) recipe));
		if(recipe instanceof ShapelessOreRecipe)
			list.add(new ShapelessPage((ShapelessOreRecipe) recipe));
		if(recipe instanceof ShapelessFleRecipe)
			list.add(new ShapelessPage((ShapelessFleRecipe) recipe));
	}
	
	@Override
	public List<IGuidePage> getAllPage()
	{
		return list;
	}
	
	@Override
	public String getTypeName()
	{
		return "FLE Shapeless Recipe";
	}

	@Override
	public String getGuideName()
	{
		return "crafting";
	}
	
	@Override
	public Rectangle getRecipeRect()
	{
		return new Rectangle(123, 42, 11, 18);
	}
	
	@Override
	protected List<IGuidePage> getPage(ItemAbstractStack contain)
	{
		List<IGuidePage> ret = new ArrayList<IGuideType.IGuidePage>();
		label:
		for(IGuidePage rawPage : getAllPage())
		{
			ShapelessPage page = (ShapelessPage) rawPage;
			if(contain.isStackEqul(page.output))
			{
				ret.add(rawPage);
				continue;
			}
			for(ItemStack[] stacks : page.showArray)
			{
				if(stacks == null) continue;
				for(ItemStack stack : stacks)
				{
					if(stack == null) continue;
					if(contain.isStackEqul(stack))
					{
						ret.add(rawPage);
						continue label;
					}
				}
			}
		}
		return ret;
	}

	private static class ShapelessPage extends StandardPage
	{
		private static ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/cg/crafting.png");
		static final int[][] ids = 
			{
			{0, 0},
			{1, 0},
			{0, 1},
			{1, 1},
			{0, 2},
			{1, 2},
			{2, 0},
			{2, 1},
			{2, 2}
			};
		
		public ItemAbstractStack[] stacks;
		public ItemStack output;
		public ItemStack[][] showArray;

		public ShapelessPage(ShapelessFleRecipe recipe)
		{
			stacks = recipe.getInputs().clone();
			showArray = new ItemStack[stacks.length][];
			for(int i = 0; i < stacks.length; ++i)
				showArray[i] = stacks[i].toList();
			output = recipe.getRecipeOutput().copy();
		}
		public ShapelessPage(ShapelessOreRecipe recipe)
		{
			List<Object> inputs = recipe.getInput();
			stacks = new ItemAbstractStack[inputs.size()];
			showArray = new ItemStack[stacks.length][];
			int i = 0;
			for(Object obj : inputs)
			{
				if(obj instanceof ItemStack)
				{
					stacks[i] = new ItemBaseStack((ItemStack) obj);
					showArray[i] = new ItemStack[]{(ItemStack) obj};
				}
				else if(obj instanceof List)
				{
					stacks[i] = new ItemArrayStack((List) obj);
					showArray[i] = stacks[i].toList();
				}
				++i;
			}
			Util.setStacksSize(showArray, 1);
			output = recipe.getRecipeOutput().copy();
		}
		public ShapelessPage(ShapelessRecipes recipe)
		{
			Object[] ts = recipe.recipeItems.toArray(new Object[recipe.recipeItems.size()]);
			stacks = new ItemAbstractStack[recipe.recipeItems.size()];
			showArray = new ItemStack[stacks.length][];
			for(int i = 0; i < ts.length; ++i)
			{
				stacks[i] = new ItemBaseStack((ItemStack) ts[i]);
				showArray[i] = new ItemStack[]{ts[i] != null ? ((ItemStack) ts[i]).copy() : null};
			}
			Util.setStacksSize(showArray, 1);
			output = recipe.getRecipeOutput().copy();
		}
		
		@Override
		protected ItemAbstractStack[] getInputStacks()
		{
			return stacks;
		}
		@Override
		protected ItemStack[] getOutputStacks()
		{
			return new ItemStack[]{output};
		}
		@Override
		protected ItemStack[][] getInputStacksForDisplay()
		{
			return showArray;
		}
		@Override
		protected ResourceLocation getLocation()
		{
			return locate;
		}
		@Override
		public Rectangle getRectangle(Type aType, int index)
		{
			switch(aType)
			{
			case FLUID:;
				break;
			case ITEM : return index < stacks.length ? slotRect(69 + ids[index][0] * 17, 26 + ids[index][1] * 17) : slotRect(139, 43);
			case SOLID:
				break;
			default:
				break;
			}
			return null;
		}		
	}
}