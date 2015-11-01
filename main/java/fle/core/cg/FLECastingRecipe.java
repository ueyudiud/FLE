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
import fle.api.material.MatterDictionary;
import fle.api.material.MatterDictionary.IFreezingRecipe;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.core.recipe.CastingPoolRecipe;

public class FLECastingRecipe extends StandardType
{
	private List<IGuidePage> list = new ArrayList();
	
	private boolean init = false;
	
	private void init()
	{
		if(!init)
		{
			for(IFreezingRecipe recipe : MatterDictionary.getFreezeRecipes())
			{
				if(recipe instanceof CastingPoolRecipe)
					list.add(new CastingPage((CastingPoolRecipe) recipe));
			}
			init = true;
		}
	}
	
	@Override
	public String getGuideName()
	{
		return "casting";
	}

	@Override
	public String getTypeName()
	{
		return "Casting";
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
			CastingPage page = (CastingPage) rawPage;
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
			CastingPage page = (CastingPage) rawPage;
			if(contain.isStackEqul(page.output))
			{
				list.add(page);
				continue label;
			}
			for(ItemStack[] tStacks : page.display)
			{
				for(ItemStack tStack : tStacks)
				{
					if(contain.isStackEqul(tStack))
					{
						list.add(page);
						continue label;
					}
				}
			}
		}
		return list;
	}
	
	private static class CastingPage extends StandardPage
	{
		private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/cg/casting_pool.png");
		
		FluidStack finput;
		ItemAbstractStack[] inputs;
		ItemStack output;
		ItemStack[][] display;

		public CastingPage(CastingPoolRecipe recipe)
		{
			finput = recipe.getInput();
			ItemStack[] s = recipe.getRecipeMap();
			inputs = new ItemAbstractStack[s.length];
			display = new ItemStack[s.length][];
			for (int i = 0; i < s.length; i++)
			{
				inputs[i] = new ItemBaseStack(s[i]);
				display[i] = inputs[i].toList();
			}
			output = recipe.getOutput();
		}

		@Override
		protected ItemAbstractStack[] getInputStacks()
		{
			return inputs;
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
			return aType == Type.FLUID ? new Rectangle(44, 21 + 60 - (60 * finput.amount) / 6000, 8, (60 * finput.amount) / 6000) : 
				aType == Type.ITEM ? index == 9 ? slotRect(130, 60) : slotRect(61 + (index % 3) * 17, 27 + (index / 3) * 17) : null;
		}
		
		@Override
		protected FluidStack[] getInputFluidStacks()
		{
			return new FluidStack[]{finput};
		}
		
		@Override
		public void drawOther(GuiBookBase gui, int xOffset, int yOffset)
		{
			gui.drawTexturedModalRect(44 + xOffset, 21 + yOffset, 176, 33, 8, 60);
			super.drawOther(gui, xOffset, yOffset);
		}
	}
}