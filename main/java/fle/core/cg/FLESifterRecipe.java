package fle.core.cg;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import fle.api.FleValue;
import fle.api.cg.StandardPage;
import fle.api.cg.StandardType;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.RecipeInfomation;
import fle.api.soild.Solid;
import fle.api.soild.SolidStack;

public class FLESifterRecipe extends StandardType
{
	private List<IGuidePage> list = new ArrayList();
	
	private boolean init = false;
	
	private void init()
	{
		if(!init)
		{
			for(fle.core.recipe.FLESifterRecipe.SifterRecipe recipe : fle.core.recipe.FLESifterRecipe.getInstance().getRecipes())
			{
				list.add(new SifterRecipe(recipe));
			}
			init = true;
		}
	}

	@Override
	public String getTypeName()
	{
		return "Sifter Recipe";
	}

	@Override
	public String getGuideName()
	{
		return "sifter";
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
			SifterRecipe page = (SifterRecipe) rawPage;
			if(page.input1 == null) continue;
			for(ItemStack tStack : page.input1.toList())
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
	
	@Override
	protected List<IGuidePage> getPage(Solid solid)
	{
		List<IGuidePage> list = new ArrayList();
		
		for(IGuidePage rawPage : getAllPage())
		{
			SifterRecipe page = (SifterRecipe) rawPage;
			if(page.input2 != null)
				if(page.input2.getObj() == solid)
				{
					list.add(page);
					continue;
				}
			if(page.output1.getObj() == solid)
			{
				list.add(page);
				continue;
			}
		}
		return list;
	}
	
	private static class SifterRecipe extends StandardPage
	{
		private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/cg/sifter.png");
		
		ItemAbstractStack input1;
		SolidStack input2;
		SolidStack output1;
		ItemStack output2;
		
		public SifterRecipe(fle.core.recipe.FLESifterRecipe.SifterRecipe recipe)
		{
			if(recipe.getInput() instanceof ItemAbstractStack)
			{
				input1 = (ItemAbstractStack) recipe.getInput();
				input2 = null;
			}
			else
			{
				input1 = null;
				input2 = new SolidStack((Solid) recipe.getInput());
			}
			output1 = recipe.output1;
			if(recipe.output2 != null)
			{
				ItemStack output = recipe.output2.copy();
				RecipeInfomation.setChance(output, recipe.getOutputChance());
				output2 = output;
			}
		}

		@Override
		protected ItemAbstractStack[] getInputStacks()
		{
			return input1 != null ? new ItemAbstractStack[]{input1} : new ItemAbstractStack[0];
		}

		@Override
		protected ItemStack[] getOutputStacks()
		{
			return output2 != null ? new ItemStack[]{output2} : new ItemStack[0];
		}

		@Override
		protected ItemStack[][] getInputStacksForDisplay()
		{
			return input1 != null ? new ItemStack[][]{input1.toList()} : new ItemStack[0][];
		}
		
		@Override
		protected SolidStack[] getInputSolidStacks()
		{
			return input2 != null ? new SolidStack[]{input2} : new SolidStack[0];
		}
		
		@Override
		protected SolidStack[] getOutputSolidStacks()
		{
			return new SolidStack[]{output1};
		}

		@Override
		protected ResourceLocation getLocation()
		{
			return locate;
		}

		@Override
		public Rectangle getRectangle(Type aType, int index)
		{
			return aType == Type.ITEM ? (index == 0 && input1 != null) ?
					slotRect(66, 23) : slotRect(92, 23): 
						aType == Type.SOLID ? (index == 0 && input2 != null) ?
								new Rectangle(66, 23 + 8, 16, 16 - 8) : 
									input2 != null ? new Rectangle(66, 49 + 8, 16, 8) :
										new Rectangle(66, 49 + 16 - (output1.getSize() * 16) / 200, 16, (output1.getSize() * 16) / 200): null;
		}
		
		@Override
		public List<String> getToolTip(Type aType, int index)
		{
			return aType == Type.ITEM ? (input1 != null ? index == 1 ? 
					Arrays.asList(RecipeInfomation.getChanceInfo(output2, false)) : new ArrayList() : 
						Arrays.asList(RecipeInfomation.getChanceInfo(output2, false))) : super.getToolTip(aType, index);
		}
	}
}