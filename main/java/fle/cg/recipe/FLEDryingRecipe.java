package fle.cg.recipe;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import fle.api.FleValue;
import fle.api.cg.GuiBookBase;
import fle.api.cg.StandardPage;
import fle.api.cg.StandardType;
import fle.api.recipe.ItemAbstractStack;
import fle.core.recipe.FLEDryingRecipe.DryingRecipe;

public class FLEDryingRecipe extends StandardType
{
	private List<IGuidePage> list = new ArrayList();
	
	private boolean init = false;
	
	private void init()
	{
		if(!init)
		{
			for(fle.core.recipe.FLEDryingRecipe.DryingRecipe recipe : fle.core.recipe.FLEDryingRecipe.getInstance().getRecipes())
			{
				list.add(new DryingPage(recipe));
			}
			init = true;
		}
	}

	@Override
	public String getTypeName()
	{
		return "Drying";
	}

	@Override
	public String getGuideName()
	{
		return "drying";
	}

	@Override
	public List<IGuidePage> getAllPage()
	{
		init();
		return list;
	}

	@Override
	protected List<IGuidePage> getPage(ItemAbstractStack contain)
	{
		List<IGuidePage> list = new ArrayList();
		
		label:
		for(IGuidePage rawPage : getAllPage())
		{
			DryingPage page = (DryingPage) rawPage;
			if(contain.isStackEqul(page.output))
			{
				list.add(page);
				continue label;
			}
			for(ItemStack tStack : page.input.toList())
			{
				if(contain.isStackEqul(tStack))
				{
					list.add(page);
					continue label;
				}
			}
		}
		return list;
	}
	
	private static class DryingPage extends StandardPage
	{
		private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/cg/drying_table.png");
		
		ItemAbstractStack input;
		int tick;
		ItemStack output;
		
		public DryingPage(DryingRecipe recipe)
		{
			input = recipe.input;
			tick = recipe.recipeTime;
			output = recipe.output.copy();
		}

		@Override
		protected ItemAbstractStack[] getInputStacks()
		{
			return new ItemAbstractStack[]{input};
		}

		@Override
		protected ItemStack[] getOutputStacks()
		{
			return new ItemStack[]{output};
		}

		@Override
		protected ItemStack[][] getInputStacksForDisplay()
		{
			return new ItemStack[][]{input.toList()};
		}

		@Override
		protected ResourceLocation getLocation()
		{
			return locate;
		}

		@Override
		public Rectangle getRectangle(Type aType, int index)
		{
			return aType == Type.ITEM ? index == 0 ? slotRect(48, 35) : slotRect(124, 35): null;
		}
		
		@Override
		public void drawOther(GuiBookBase gui, int xOffset, int yOffset)
		{
			drawToolTip(gui.getFortRender(), 
					Arrays.asList(String.format("Recipe buffer : %d", tick)));
			super.drawOther(gui, xOffset, yOffset);
		}
	}
}