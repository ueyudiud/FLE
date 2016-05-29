package fle.compact.nei;

import java.util.List;

import codechicken.nei.PositionedStack;
import cpw.mods.fml.common.FMLCommonHandler;
import farcore.lib.nei.FarPositionedStack;
import farcore.lib.nei.FarTemplateRecipeHandler;
import farcore.lib.recipe.FleCraftingManager;
import farcore.lib.recipe.IFleRecipe;
import farcore.lib.recipe.ShapedFleRecipe;
import farcore.lib.recipe.ShapelessFleRecipe;
import farcore.lib.stack.AbstractStack;
import farcore.lib.stack.BaseStack;

public class FarCraftingHandler extends FarTemplateRecipeHandler
{
	private static final int[][] shapless_set = {
			{0, 0},
			{1, 0},
			{0, 1},
			{1, 1},
			{2, 0},
			{2, 1},
			{0, 2},
			{1, 2},
			{2, 2}
	};
	
	public class ShapedCachedRecipe extends BaseCachedRecipe
	{
		public ShapedFleRecipe recipe;
		
		public ShapedCachedRecipe(ShapedFleRecipe recipe)
		{
			this.recipe = recipe;
			try
			{
				AbstractStack stack;
				if(recipe.tools.length > 0)
				{
					stack = recipe.tools[0].getKey();
					if(stack != BaseStack.EMPTY)
						resources.add(new FarPositionedStack(stack, 16 - sOffsetX, 18 - sOffsetY));
					stack = recipe.tools[0].getValue();
					if(stack != BaseStack.EMPTY)
						resources.add(new FarPositionedStack(stack, 36 - sOffsetX, 15 - sOffsetY));
					if(recipe.tools.length > 1)
					{
						stack = recipe.tools[1].getKey();
						if(stack != BaseStack.EMPTY)
							resources.add(new FarPositionedStack(stack, 16 - sOffsetX, 35 - sOffsetY));
						stack = recipe.tools[1].getValue();
						if(stack != BaseStack.EMPTY)
							resources.add(new FarPositionedStack(stack, 36 - sOffsetX, 35 - sOffsetY));
						if(recipe.tools.length > 2)
						{
							stack = recipe.tools[2].getKey();
							if(stack != BaseStack.EMPTY)
								resources.add(new FarPositionedStack(stack, 16 - sOffsetX, 52 - sOffsetY));
							stack = recipe.tools[2].getValue();
							if(stack != BaseStack.EMPTY)
								resources.add(new FarPositionedStack(stack, 36 - sOffsetX, 55 - sOffsetY));
						}
					}
				}
				for(int x = 0; x < recipe.width; ++x)
					for(int y = 0; y < recipe.height; ++y)
					{
						if(recipe.input[y][x] != null)
							resources.add(new FarPositionedStack(recipe.input[y][x], 69 - sOffsetX + 17 * x, 18 - sOffsetY + 17 * y));
					}
				products.add(new PositionedStack(recipe.output, 139 - sOffsetX, 35 - sOffsetY, false));
			}
			catch(Exception exception)
			{
				exception.printStackTrace();
			}
		}
	}
	public class ShapelessCachedRecipe extends BaseCachedRecipe
	{		
		public ShapelessFleRecipe recipe;
		
		public ShapelessCachedRecipe(ShapelessFleRecipe recipe)
		{
			this.recipe = recipe;
			try
			{
				AbstractStack stack;
				if(recipe.tools.length > 0)
				{
					stack = recipe.tools[0].getKey();
					if(stack != BaseStack.EMPTY)
						resources.add(new FarPositionedStack(stack, 16 - sOffsetX, 18 - sOffsetY));
					stack = recipe.tools[0].getValue();
					if(stack != BaseStack.EMPTY)
						resources.add(new FarPositionedStack(stack, 36 - sOffsetX, 15 - sOffsetY));
					if(recipe.tools.length > 1)
					{
						stack = recipe.tools[1].getKey();
						if(stack != BaseStack.EMPTY)
							resources.add(new FarPositionedStack(stack, 16 - sOffsetX, 35 - sOffsetY));
						stack = recipe.tools[1].getValue();
						if(stack != BaseStack.EMPTY)
							resources.add(new FarPositionedStack(stack, 36 - sOffsetX, 35 - sOffsetY));
						if(recipe.tools.length > 2)
						{
							stack = recipe.tools[2].getKey();
							if(stack != BaseStack.EMPTY)
								resources.add(new FarPositionedStack(stack, 16 - sOffsetX, 52 - sOffsetY));
							stack = recipe.tools[2].getValue();
							if(stack != BaseStack.EMPTY)
								resources.add(new FarPositionedStack(stack, 36 - sOffsetX, 55 - sOffsetY));
						}
					}
				}
				int c = 0;
				if(recipe.outputDetect != null)
				{
					resources.add(new FarPositionedStack(recipe.outputDetect.display(), 69 - sOffsetX, 18 - sOffsetY));
					c = 1;
				}
				for(AbstractStack stack2 : recipe.input)
				{
					resources.add(new FarPositionedStack(stack2, 69 - sOffsetX + 17 * shapless_set[c][0], 18 - sOffsetY + 17 * shapless_set[c][1]));
					++c;
				}
				products.add(new PositionedStack(recipe.output, 139 - sOffsetX, 35 - sOffsetY, false));
			}
			catch(Exception exception)
			{
				exception.printStackTrace();
			}
		}
	}
	
	public FarCraftingHandler()
	{
		super();
	}

	@Override
	public String getRecipeName()
	{
		return "Far Crafting";
	}

	@Override
	protected String getRecipeId()
	{
		return "far crafting";
	}

	@Override
	protected void initRecipeList(List<BaseCachedRecipe> list)
	{
		CraftingRecipeWrapEvent event;
		for(IFleRecipe recipe : FleCraftingManager.getRecipes())
		{
			event = new CraftingRecipeWrapEvent(recipe);
			FMLCommonHandler.instance().bus().post(event);
			if(event.neiDisplay != null)
			{
				list.add(event.neiDisplay);
			}
			else
			{
				if(recipe instanceof ShapedFleRecipe)
				{
					list.add(new ShapedCachedRecipe((ShapedFleRecipe) recipe));
				}
				else if(recipe instanceof ShapelessFleRecipe)
				{
					list.add(new ShapelessCachedRecipe((ShapelessFleRecipe) recipe));
				}
			}
		}
	}

	@Override
	public String getGuiTexture()
	{
		return "fle:textures/gui/NEI/crafting.png";
	}
	
	@Override
	public int recipiesPerPage()
	{
		return 2;
	}
}