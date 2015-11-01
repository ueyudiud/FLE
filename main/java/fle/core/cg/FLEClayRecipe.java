package fle.core.cg;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import fle.api.FleValue;
import fle.api.cg.GuiBookBase;
import fle.api.cg.StandardPage;
import fle.api.cg.StandardType;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.core.item.ItemFleSub;
import fle.core.recipe.CeramicsRecipe;

public class FLEClayRecipe extends StandardType
{
	private List<IGuidePage> list = new ArrayList();
	
	private boolean init = false;
	
	private void init()
	{
		if(!init)
		{
			for(CeramicsRecipe recipe : CeramicsRecipe.getRecipeList())
			{
				list.add(new ClayRecipe(recipe));
			}
			init = true;
		}
	}
	
	@Override
	public String getTypeName()
	{
		return "Clay Model";
	}

	@Override
	public String getGuideName()
	{
		return "clay";
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
		for(IGuidePage rawPage : getAllPage())
		{
			ClayRecipe page = (ClayRecipe) rawPage;
			if(contain.isStackEqul(page.output))
			{
				list.add(page);
				continue;
			}
			for(ItemStack tStack : page.input.toList())
			{
				if(contain.isStackEqul(tStack))
				{
					list.add(page);
					break;
				}
			}
		}
		return list;
	}
	
	private static class ClayRecipe extends StandardPage
	{
		private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/cg/clay_model.png");
		
		static final ItemBaseStack input = new ItemBaseStack(ItemFleSub.a("argil_ball", 4));
		float[] value;
		ItemStack output;

		public ClayRecipe(CeramicsRecipe recipe)
		{
			value = recipe.getDefaultValue();
			output = recipe.getOutput();
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
		public ResourceLocation getLocation() 
		{
			return locate;
		}

		@Override
		public Rectangle getRectangle(Type aType, int index)
		{
			return aType == Type.ITEM ? index == 0 ? slotRect(94, 20) : slotRect(130, 40) : null;
		}
		
		@Override
		public void drawOther(GuiBookBase gui, int xOffset, int yOffset)
		{
			gui.bindTexture(locate);
			int i;
			for(i = 0; i < 5; ++i)
			{
				int i0 = (int) (value[i] * 22);
				gui.drawTexturedModalRect(xOffset + 34 + (22 - i0), yOffset + 20 + 11 * i, 176 + (22 - i0), -1 + i * 11, i0, 11);
			}
			for(i = 0; i < 5; ++i)
			{
				int i0 = (int) (value[i + 5] * 22);
				gui.drawTexturedModalRect(xOffset + 55, yOffset + 20 + 11 * i, 198, -1 + i * 11, i0, 11);
			}
			gui.drawTexturedModalRect(34, 21, 176, 54, 43, 53);
			super.drawOther(gui, xOffset, yOffset);
		}
	}
}