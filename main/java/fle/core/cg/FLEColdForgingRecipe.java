package fle.core.cg;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import flapi.cg.GuiBookBase;
import flapi.cg.IGuideType;
import flapi.cg.StandardPage;
import flapi.cg.StandardType;
import flapi.recipe.CraftingState;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.util.FleValue;
import fle.core.init.Lang;
import fle.core.recipe.ColdForgingRecipe;

public class FLEColdForgingRecipe extends StandardType
{
	private List<IGuidePage> list = new ArrayList();
	
	private void init()
	{
		if(!init)
		{
			for(ColdForgingRecipe recipe : ColdForgingRecipe.getRecipes())
			{
				list.add(new ColdForgingPage(recipe));
			}
			init = true;
		}
	}
	
	private boolean init = false;
	
	@Override
	public String getTypeName()
	{
		return Lang.cg_coldForging;
	}
	
	@Override
	public String getGuideName()
	{
		return "cold_forging";
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
		return new Rectangle(118, 41, 11, 18);
	}

	@Override
	protected List<IGuidePage> getPage(ItemAbstractStack contain)
	{
		List<IGuidePage> ret = new ArrayList<IGuideType.IGuidePage>();
		label:
		for(IGuidePage rawPage : getAllPage())
		{
			ColdForgingPage page = (ColdForgingPage) rawPage;
			if(contain.equal(page.output))
			{
				ret.add(rawPage);
				continue;
			}
			for(ItemAbstractStack stack : page.input)
			{
				if(stack == null) continue;
				for(ItemStack target : stack.toList())
				{
					if(contain.equal(target))
					{
						ret.add(rawPage);
						continue label;
					}
				}
			}
		}
		return ret;
	}

	private static class ColdForgingPage extends StandardPage
	{
		private static ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/cg/cold_forging.png");
		private ItemStack output;
		private ItemAbstractStack[] input;
		private CraftingState[] states;
		private ItemStack[][] display;
		
		public ColdForgingPage(ColdForgingRecipe recipe)
		{
			output = recipe.getOutput();
			input = recipe.getRecipeInputs();
			display = new ItemStack[input.length][];
			for (int i = 0; i < input.length; i++)
			{
				ItemAbstractStack stack = input[i];
				if(stack != null)
					display[i] = stack.toList();
				else display[i] = new ItemStack[0];
			}
			states = recipe.getRecipeMap();
		}

		@Override
		protected ItemAbstractStack[] getInputStacks()
		{
			return input;
		}

		@Override
		protected ItemStack[] getOutputStacks()
		{
			return new ItemStack[]{output};
		}

		@Override
		protected ItemStack[][] getInputStacksForDisplay()
		{
			return display;
		}

		@Override
		public ResourceLocation getLocation()
		{
			return locate;
		}

		@Override
		public Rectangle getRectangle(Type aType, int index)
		{
			return aType == Type.ITEM ? 
					index < input.length ? slotRect(24 + (index % 2) * 17, 44 + (index / 2) * 17) : slotRect(134, 42) : null;
		}
		
		@Override
		public void drawOther(GuiBookBase gui, int xOffset, int yOffset)
		{
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			for(int i = 0; i < 3; ++i)
				for(int j = 0; j < 3; ++j)
					gui.drawCondition(67 + i * 17, 25 + j * 17, states[i + j * 3]);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			super.drawOther(gui, xOffset, yOffset);
		}
	}
}