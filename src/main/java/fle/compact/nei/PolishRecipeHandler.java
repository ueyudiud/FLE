package fle.compact.nei;

import java.util.List;

import farcore.FarCore;
import farcore.lib.nei.FarPositionedStack;
import farcore.lib.nei.FarTemplateRecipeHandler;
import fle.api.recipe.machine.PolishRecipe;
import fle.api.recipe.machine.PolishRecipe.$Recipe;
import fle.api.recipe.machine.PolishRecipe.PolishCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IIcon;

public class PolishRecipeHandler extends FarTemplateRecipeHandler
{
	public class PolishCachedRecipe extends BaseCachedRecipe
	{
		$Recipe recipe;
		
		public PolishCachedRecipe($Recipe recipe)
		{
			this.recipe = recipe;
			try
			{
				resources.add(new FarPositionedStack(recipe.input, 20 - sOffsetX, 35 - sOffsetY));
				products.add(new FarPositionedStack(recipe.output, 133 - sOffsetX, 35 - sOffsetY));
			}
			catch(Exception exception)
			{
				exception.printStackTrace();
			}
		}
	}
	
	@Override
	public String getRecipeName()
	{
		return "Polish";
	}

	@Override
	protected String getRecipeId()
	{
		return "polish";
	}

	@Override
	protected void initRecipeList(List<BaseCachedRecipe> list)
	{
		for($Recipe recipe : PolishRecipe.getRecipes())
		{
			list.add(new PolishCachedRecipe(recipe));
		}
	}
	
	@Override
	public void drawExtras(int recipe)
	{
		super.drawExtras(recipe);
		$Recipe r = ((PolishCachedRecipe) arecipes.get(recipe)).recipe;
		Minecraft.getMinecraft().renderEngine.bindTexture(FarCore.conditionTextureMap.location);
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 3; ++j)
			{
				PolishCondition condition = r.conditions[j][i];
				IIcon[] icons = condition.getIcons();
				for(IIcon icon : icons)
				{
					drawTexturedModelRectFromIcon(45 + i * 17, 18 + j * 17, icon, 16, 16);
				}
			}
	}
	
	@Override
	public void loadTransferRects()
	{
		super.loadTransferRects();
		addTransfetRect(100, 34, 22, 18);
	}

	@Override
	public String getGuiTexture()
	{
		return "fle:textures/gui/NEI/polish.png";
	}
}