package fle.cg.recipe;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import fle.api.FleValue;
import fle.api.cg.StandardPage;
import fle.api.cg.StandardType;
import fle.api.cg.IGuideType.IGuidePage.Type;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.recipe.RecipeInfomation;
import fle.api.util.DropInfo;
import fle.core.recipe.WashingRecipe;

public class FLEWashingRecipe extends StandardType
{
	private List<IGuidePage> list = new ArrayList();
	
	private boolean init = false;
	
	private void init()
	{
		if(!init)
		{
			for(Entry<ItemAbstractStack, DropInfo> recipe : WashingRecipe.getRecipes().entrySet())
			{
				list.add(new WashingPage(recipe));
			}
			init = true;
		}
	}
	
	@Override
	public String getTypeName()
	{
		return "Washing";
	}
	
	@Override
	public String getGuideName()
	{
		return "washing";
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
			WashingPage page = (WashingPage) rawPage;
			for(ItemStack tStack : page.input.toList())
			{
				if(contain.isStackEqul(tStack))
				{
					list.add(page);
					continue label;
				}
			}
			for(ItemStack tStack : page.outputs)
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
	
	@Override
	protected List<IGuidePage> getPage(Fluid fluid)
	{
		return fluid == FluidRegistry.WATER ? getAllPage() : new ArrayList();
	}
	
	private static class WashingPage extends StandardPage
	{
		public static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/cg/ore_washing.png");
		
		public ItemAbstractStack input;
		public ItemStack[] outputs;
		
		public WashingPage(Entry<ItemAbstractStack, DropInfo> recipe)
		{
			input = recipe.getKey();
			Map<ItemStack, Double> map = recipe.getValue().drops.getContains();
			int i = 0;
			outputs = new ItemStack[map.size()];
			for(Entry<ItemStack, Double> e : map.entrySet())
			{
				ItemStack input = e.getKey();
				if(input == null) continue;
				input = input.copy();
				RecipeInfomation.setChance(input, e.getValue().floatValue());
				outputs[i] = input;
				++i;
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
			return outputs;
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
		
		private final FluidStack[] fs = new FluidStack[]{new FluidStack(FluidRegistry.WATER, 1)};
		
		@Override
		protected FluidStack[] getInputFluidStacks()
		{
			return fs;
		}

		@Override
		public Rectangle getRectangle(Type aType, int index)
		{
			return aType == Type.ITEM ? (index == 0 ? slotRect(42, 19) : slotRect(81 + 18 * ((index - 1) % 3), 19 + 18 * ((index - 1) / 3))) :
				(aType == Type.FLUID ? slotRect(62, 19) : null);
		}
		
		@Override
		public List<String> getToolTip(Type aType, int index)
		{
			return aType == Type.ITEM ? (index > 0 ? 
					Arrays.asList(FleValue.format_progress.format(RecipeInfomation.getChance(outputs[index - 1]))) : new ArrayList()) :
						super.getToolTip(aType, index);
		}
	}
}