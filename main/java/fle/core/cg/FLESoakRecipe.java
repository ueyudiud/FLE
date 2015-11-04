package fle.core.cg;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import fle.api.FleValue;
import fle.api.cg.GuiBookBase;
import fle.api.cg.StandardPage;
import fle.api.cg.StandardType;
import fle.api.recipe.ItemAbstractStack;
import fle.core.recipe.FLESoakRecipe.SoakRecipe;

public class FLESoakRecipe extends StandardType
{
	private List<IGuidePage> list = new ArrayList();
	
	private boolean init = false;
	
	private void init()
	{
		if(!init)
		{
			for(fle.core.recipe.FLESoakRecipe.SoakRecipe recipe : fle.core.recipe.FLESoakRecipe.getInstance().getRecipes())
			{
				list.add(new SoakPage(recipe));
			}
			init = true;
		}
	}
	
	@Override
	public String getGuideName()
	{
		return "soak";
	}

	@Override
	public String getTypeName()
	{
		return "Soak";
	}
	
	@Override
	public Rectangle getRecipeRect()
	{
		return new Rectangle(92, 22, 8, 42);
	}

	@Override
	public List<IGuidePage> getAllPage()
	{
		init();
		return list;
	}
	
	@Override
	protected List<IGuidePage> getPage(Fluid fluid)
	{
		List<IGuidePage> list = new ArrayList();
		for(IGuidePage rawPage : getAllPage())
		{
			SoakPage page = (SoakPage) rawPage;
			if(page.finput.getFluid() == fluid)
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
			SoakPage page = (SoakPage) rawPage;
			if(contain.isStackEqul(page.output))
			{
				list.add(page);
				continue label;
			}
			for(ItemStack tStack : page.display)
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
	
	private static class SoakPage extends StandardPage
	{
		private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/cg/soak.png");
		
		FluidStack finput;
		ItemAbstractStack input;
		ItemStack output;
		ItemStack[] display;
		int tick;

		public SoakPage(fle.core.recipe.FLESoakRecipe.SoakRecipe recipe)
		{
			finput = recipe.getFluidInput();
			input = recipe.getItemInput();
			display = input.toList();
			output = recipe.getOutput(display[0]);
			tick = recipe.tick;
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
			return new ItemStack[][]{display};
		}

		@Override
		public ResourceLocation getLocation()
		{
			return locate;
		}

		@Override
		public Rectangle getRectangle(Type aType, int index)
		{
			return aType == Type.FLUID ? new Rectangle(41, 18 + 50 - (50 * finput.amount) / 2000, 50, (50 * finput.amount) / 2000) : 
				aType == Type.ITEM ? index == 0 ? slotRect(101, 23) : slotRect(119, 23) : null;
		}
		
		@Override
		protected FluidStack[] getInputFluidStacks()
		{
			return new FluidStack[]{finput};
		}
		
		@Override
		public void drawOther(GuiBookBase gui, int xOffset, int yOffset)
		{
			gui.drawTexturedModalRect(41 + xOffset, 18 + yOffset, 176, 0, 50, 50);
			drawToolTip(gui.getFortRender(), 
					Arrays.asList(String.format("Recipe Time : %d", tick)));
			super.drawOther(gui, xOffset, yOffset);
		}
	}
}