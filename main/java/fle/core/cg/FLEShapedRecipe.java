package fle.core.cg;

import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import flapi.cg.IGuideType;
import flapi.cg.StandardPage;
import flapi.cg.StandardType;
import flapi.recipe.ShapedFleRecipe;
import flapi.recipe.stack.ArrayStack;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.util.FleValue;
import fle.core.init.Lang;
import fle.core.util.Util;

public class FLEShapedRecipe extends StandardType
{
	private static List<IGuidePage> list = new ArrayList();
	
	public static void register(Object recipe)
	{
		if(recipe instanceof ShapedRecipes)
			list.add(new ShapedPage((ShapedRecipes) recipe));
		if(recipe instanceof ShapedOreRecipe)
			list.add(new ShapedPage((ShapedOreRecipe) recipe));
		if(recipe instanceof ShapedFleRecipe)
			list.add(new ShapedPage((ShapedFleRecipe) recipe));
	}
	
	@Override
	public List<IGuidePage> getAllPage()
	{
		return list;
	}
	
	@Override
	public String getTypeName()
	{
		return Lang.cg_crafting_shaped;
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
			ShapedPage page = (ShapedPage) rawPage;
			if(contain.equal(page.output))
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
					if(contain.equal(stack))
					{
						ret.add(rawPage);
						continue label;
					}
				}
			}
		}
		return ret;
	}
	
	private static class ShapedPage extends StandardPage
	{
		private static ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/cg/crafting.png");
		private static boolean init = false;
		protected static Field oreRecipeWidth;
		protected static Field oreRecipeHeight;
		
		private static void init()
		{
			try
			{
				oreRecipeWidth = ShapedOreRecipe.class.getDeclaredField("width");
				oreRecipeHeight = ShapedOreRecipe.class.getDeclaredField("height");
				init = true;
			}
			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}
		
		protected int xSize;
		protected boolean isSmallRecipe;
		public ItemAbstractStack[] stacks;
		public ItemStack output;
		private ItemStack[][] showArray;

		public ShapedPage(ShapedFleRecipe recipe)
		{
			stacks = recipe.getInputs().clone();
			showArray = new ItemStack[stacks.length][];
			for(int i = 0; i < stacks.length; ++i)
			{
				if(stacks[i] != null)
				{
					showArray[i] = stacks[i].toList();
				}
			}
			isSmallRecipe = recipe.getXSize() <= 2 && recipe.getYSize() <= 2;
			output = recipe.getRecipeOutput().copy();
			xSize = recipe.getXSize();
			recipe.getYSize();
		}
		public ShapedPage(ShapedOreRecipe recipe)
		{
			Object[] inputs = recipe.getInput();
			stacks = new ItemAbstractStack[inputs.length];
			showArray = new ItemStack[inputs.length][];
			for(int i = 0; i < inputs.length; ++i)
			{
				if(inputs[i] instanceof ItemStack)
				{
					stacks[i] = new BaseStack((ItemStack) inputs[i]);
					showArray[i] = new ItemStack[]{(ItemStack) inputs[i]};
				}
				else if(inputs[i] instanceof List)
				{
					stacks[i] = new ArrayStack((List) inputs[i]);
					showArray[i] = stacks[i].toList();
				}
			}
			Util.setStacksSize(showArray, 1);
			isSmallRecipe = inputs.length <= 4 && inputs.length != 3;
			output = recipe.getRecipeOutput().copy();
			if(!init) init();
			try
			{
				xSize = oreRecipeWidth.getInt(recipe);
				oreRecipeHeight.getInt(recipe);
			}
			catch(Throwable e)
			{
				xSize = isSmallRecipe ? 2 : 3;
			}
		}
		public ShapedPage(ShapedRecipes recipe)
		{
			stacks = new ItemAbstractStack[recipe.recipeItems.length];
			showArray = new ItemStack[stacks.length][];
			for(int i = 0; i < recipe.recipeItems.length; ++i)
			{
				stacks[i] = new BaseStack(recipe.recipeItems[i]);
				if(recipe.recipeItems[i] != null)
					showArray[i] = new ItemStack[]{recipe.recipeItems[i] != null ? recipe.recipeItems[i].copy() : null};
			}
			Util.setStacksSize(showArray, 1);
			isSmallRecipe = recipe.recipeHeight <= 2 && recipe.recipeWidth <= 2;
			output = recipe.getRecipeOutput().copy();
			xSize = recipe.recipeWidth;
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
		public ResourceLocation getLocation()
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
			case ITEM : return index < showArray.length ? slotRect(69 + (index % xSize) * 17, 26 + (index / xSize) * 17) : slotRect(139, 43);
			case SOLID:
				break;
			default:
				break;
			}
			return null;
		}		
	}
}