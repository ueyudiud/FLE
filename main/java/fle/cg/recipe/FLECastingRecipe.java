package fle.cg.recipe;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import fle.api.FleValue;
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
		return "Casting";
	}

	@Override
	public String getTypeName()
	{
		return "casting";
	}

	@Override
	public List<IGuidePage> getAllPage()
	{
		return null;
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
		protected ResourceLocation getLocation()
		{
			return locate;
		}

		@Override
		public Rectangle getRectangle(Type aType, int index)
		{
			return aType == Type.FLUID ? new Rectangle(44, 21 + 60 - finput.amount / 6000, 8, finput.amount / 6000) : 
				aType == Type.ITEM ? index == 9 ? slotRect(130, 60) : slotRect(61 + (index % 3) * 17, 27 + (index / 3) * 17) : null;
		}
		
		@Override
		protected FluidStack[] getInputFluidStacks()
		{
			return new FluidStack[]{finput};
		}
	}
}