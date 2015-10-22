package fle.core.cg;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import fle.api.FleValue;
import fle.api.cg.GuiBookBase;
import fle.api.cg.StandardPage;
import fle.api.cg.StandardType;
import fle.api.recipe.ItemAbstractStack;
import fle.api.soild.Solid;
import fle.api.soild.SolidStack;

public class FLEStoneMillRecipe extends StandardType
{
	private List<IGuidePage> list = new ArrayList();
	
	private boolean init = false;
	
	private void init()
	{
		if(!init)
		{
			for(fle.core.recipe.FLEStoneMillRecipe.StoneMillRecipe recipe : fle.core.recipe.FLEStoneMillRecipe.getInstance().getRecipes())
			{
				list.add(new StoneMillRecipe(recipe));
			}
			init = true;
		}
	}
	
	@Override
	public String getGuideName()
	{
		return "stone_mill";
	}

	@Override
	public String getTypeName()
	{
		return "Stone Mill Recipe";
	}

	@Override
	public List<IGuidePage> getAllPage()
	{
		init();
		return list;
	}
	
	@Override
	protected List<IGuidePage> getPage(Solid solid)
	{
		List<IGuidePage> list = new ArrayList();
		for(IGuidePage rawPage : getAllPage())
		{
			StoneMillRecipe page = (StoneMillRecipe) rawPage;
			if(page.soutput == null) continue;
			if(page.soutput.getObj() == solid)
			{
				list.add(page);
				continue;
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
			StoneMillRecipe page = (StoneMillRecipe) rawPage;
			if(page.foutput == null) continue;
			if(page.foutput.getFluid() == fluid)
			{
				list.add(page);
				continue;
			}
		}
		return list;
	}
	
	@Override
	protected List<IGuidePage> getPage(ItemAbstractStack contain)
	{
		List<IGuidePage> list = new ArrayList();
		label:
		for(IGuidePage rawPage : getAllPage())
		{
			StoneMillRecipe page = (StoneMillRecipe) rawPage;
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
	
	private static class StoneMillRecipe extends StandardPage
	{
		private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/cg/stone_mill.png");
		
		ItemAbstractStack input;
		SolidStack soutput;
		FluidStack foutput;

		public StoneMillRecipe(fle.core.recipe.FLEStoneMillRecipe.StoneMillRecipe recipe)
		{
			input = recipe.getInput();
			if(recipe.getFluidOutput() != null)
				foutput = recipe.getFluidOutput().copy();
			if(recipe.getOutput() != null)
				soutput = recipe.getOutput().copy();
		}

		@Override
		protected ItemAbstractStack[] getInputStacks()
		{
			return new ItemAbstractStack[]{input};
		}
		
		@Override
		protected ItemStack[] getOutputStacks()
		{
			return new ItemStack[0];
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
		protected SolidStack[] getOutputSolidStacks()
		{
			return soutput == null ? super.getOutputSolidStacks() : new SolidStack[]{soutput};
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
			case FLUID: return foutput != null ?
					new Rectangle(117, 48 + 20 - (foutput.amount * 20) / 2000, 8, (foutput.amount * 20) / 2000) : null;
			case ITEM : return slotRect(65, 20);
			case SOLID: return soutput != null ?
					new Rectangle(65, 52 + 16 - (soutput.getSize() * 16) / 300, 16, (soutput.getSize() * 16) / 300) : null;
			}
			return null;
		}
		
		@Override
		public void drawOther(GuiBookBase gui, int xOffset, int yOffset)
		{
			//gui.drawTexturedModalRect(117 + xOffset, 48 + yOffset, 176, 33, 8, 60);
			super.drawOther(gui, xOffset, yOffset);
		}
	}
}