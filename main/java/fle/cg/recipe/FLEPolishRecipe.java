package fle.cg.recipe;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import fle.api.FleValue;
import fle.api.cg.GuiBookBase;
import fle.api.cg.IGuideType;
import fle.api.cg.StandardPage;
import fle.api.cg.StandardType;
import fle.api.recipe.CraftingState;
import fle.api.recipe.ItemAbstractStack;

public class FLEPolishRecipe extends StandardType
{
	private List<IGuidePage> list = new ArrayList();
	
	private void init()
	{
		if(!init)
		{
			for(fle.core.recipe.FLEPolishRecipe.PolishRecipe recipe : fle.core.recipe.FLEPolishRecipe.getInstance().getRecipes())
			{
				list.add(new PolishPage(recipe));
			}
			init = true;
		}
	}
	
	private boolean init = false;
	
	@Override
	public String getTypeName()
	{
		return "Polish";
	}
	
	@Override
	public String getGuideName()
	{
		return "polish";
	}

	@Override
	public List<IGuidePage> getAllPage()
	{
		init();
		return list;
	}
	
	@Override
	public Rectangle getRecipeRect()
	{
		return new Rectangle(95, 39, 32, 8);
	}

	@Override
	protected List<IGuidePage> getPage(ItemAbstractStack contain)
	{
		List<IGuidePage> ret = new ArrayList<IGuideType.IGuidePage>();
		for(IGuidePage rawPage : getAllPage())
		{
			PolishPage page = (PolishPage) rawPage;
			if(contain.isStackEqul(page.output))
			{
				ret.add(rawPage);
				continue;
			}
			for(ItemStack stack : page.input.toList())
			{
				if(stack == null) continue;
				if(contain.isStackEqul(stack))
				{
					ret.add(rawPage);
					continue;
				}
			}
		}
		return ret;
	}
		
	private static class PolishPage extends StandardPage
	{
		private static ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/cg/polish.png");
		private ItemStack output;
		private ItemAbstractStack input;
		private CraftingState[] states;
		
		public PolishPage(fle.core.recipe.FLEPolishRecipe.PolishRecipe recipe)
		{
			output = recipe.output.copy();
			input = recipe.getinput();
			states = CraftingState.getStates(recipe.getRecipeMap());
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
			return aType == Type.ITEM ? index == 0 ? slotRect(23, 35) : slotRect(132, 35) : null;
		}
		
		@Override
		public void drawOther(GuiBookBase gui, int xOffset, int yOffset)
		{
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			for(int i = 0; i < 3; ++i)
				for(int j = 0; j < 3; ++j)
					gui.drawCondition(44 + i * 17, 18 + j * 17, states[i + j * 3]);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			super.drawOther(gui, xOffset, yOffset);
		}
	}
}