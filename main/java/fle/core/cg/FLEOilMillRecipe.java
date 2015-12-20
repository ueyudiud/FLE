package fle.core.cg;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import flapi.cg.GuiBookBase;
import flapi.cg.StandardPage;
import flapi.cg.StandardType;
import flapi.recipe.RecipeInfomation;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.util.FleValue;
import fle.core.init.Lang;

public class FLEOilMillRecipe extends StandardType
{
	private List<IGuidePage> list = new ArrayList();
	
	private boolean init = false;
	
	private void init()
	{
		if(!init)
		{
			for(fle.core.recipe.FLEOilMillRecipe.OilMillRecipe recipe : fle.core.recipe.FLEOilMillRecipe.getInstance().getRecipes())
			{
				list.add(new OilMillRecipe(recipe));
			}
			init = true;
		}
	}

	@Override
	public String getTypeName()
	{
		return Lang.cg_oilMill;
	}
	
	@Override
	public String getGuideName()
	{
		return "oil_mill";
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
			OilMillRecipe page = (OilMillRecipe) rawPage;
			if(contain.equal(page.output))
			{
				list.add(page);
				continue label;
			}
			for(ItemStack tStack : page.input.toList())
			{
				if(contain.equal(tStack))
				{
					list.add(page);
					continue label;
				}
			}
		}
		return list;
	}
	
	@Override
	protected List<IGuidePage> getPage(Fluid fluid)
	{
		List<IGuidePage> list = new ArrayList();
		for(IGuidePage rawPage : getAllPage())
		{
			OilMillRecipe page = (OilMillRecipe) rawPage;
			if(page.foutput.getFluid() == fluid)
			{
				list.add(page);
				continue;
			}
		}
		return list;
	}
	
	private static class OilMillRecipe extends StandardPage
	{
		private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/cg/lever_oil_mill.png");
		
		ItemAbstractStack input;
		ItemStack output;
		FluidStack foutput;

		public OilMillRecipe(fle.core.recipe.FLEOilMillRecipe.OilMillRecipe recipe)
		{
			input = recipe.getInput();
			foutput = recipe.output2.copy();
			if(recipe.output1 != null)
			{
				ItemStack o = recipe.output1.copy();
				RecipeInfomation.setChance(o, recipe.getChance());
				output = o;
			}
		}

		@Override
		protected ItemAbstractStack[] getInputStacks()
		{
			return new ItemAbstractStack[]{input};
		}
		
		@Override
		protected ItemStack[] getOutputStacks()
		{
			return output != null ? new ItemStack[]{output} : new ItemStack[0];
		}

		@Override
		protected ItemStack[][] getInputStacksForDisplay()
		{
			return new ItemStack[][]{input.toList()};
		}
		
		@Override
		protected FluidStack[] getOutputFluidStacks() 
		{
			return foutput == null ? super.getOutputFluidStacks() : new FluidStack[]{foutput};
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
			case FLUID: return foutput != null ?
					new Rectangle(60, 56, (foutput.amount * 20) / 200, 8) : null;
			case ITEM : return index == 0 ? slotRect(61, 21) : slotRect(98, 21);
			case SOLID: return null;
			}
			return null;
		}
		
		@Override
		public void drawOther(GuiBookBase gui, int xOffset, int yOffset)
		{
			gui.drawTexturedModalRect(60 + xOffset, 56 + yOffset, 176, 16, 20, 6);
			super.drawOther(gui, xOffset, yOffset);
		}
		
		@Override
		public List<String> getToolTip(Type aType, int index)
		{
			return aType == Type.ITEM && index == 1 ? Arrays.asList(RecipeInfomation.getChanceInfo(output, false)) : super.getToolTip(aType, index);
		}
	}
}