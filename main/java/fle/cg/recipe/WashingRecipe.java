package fle.cg.recipe;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import fle.api.cg.GuiBookBase;
import fle.api.cg.RecipeHandler;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.util.DropInfo;
import fle.api.util.WeightHelper.Stack;

public class WashingRecipe extends RecipeBase implements RecipeHandler
{
	protected ItemAbstractStack input;
	protected ItemStack[] outputs;
	protected Map<ItemStack, Double> outputMap;
	
	public WashingRecipe(ItemAbstractStack aInput, DropInfo info)
	{
		input = aInput;
		outputMap = new HashMap();
		for(Stack<ItemStack> output : info.drops.getList())
		{
			outputMap.put(output.getObj(), info.getDrop() * (double) output.getSize() / (double) info.drops.allWeight());
		}
		outputs = outputMap.keySet().toArray(new ItemStack[outputMap.size()]);
	}

	@Override
	public boolean match(ItemStack target)
	{
		for(ItemStack tStack : outputMap.keySet())
		{
			if(OreDictionary.itemMatches(target, tStack, false)) return true;
		}
		return input.isStackEqul(target);
	}

	@Override
	public boolean match(FluidStack target)
	{
		return target.getFluid() == FluidRegistry.WATER;
	}

	@Override
	public void drawRecipeBackground(GuiBookBase gui, int xOffset, int yOffset)
	{
		gui.bindTexture(locate1);
		gui.drawTexturedModalRect(xOffset, yOffset, 79, 0, 79, 58);
	}

	@Override
	public void drawRecipeFortground(GuiBookBase gui, int xOffset, int yOffset)
	{
		
	}

	@Override
	public void onUpdate(GuiBookBase gui)
	{
		
	}

	@Override
	public int getSlotContain()
	{
		return outputMap.size() + 1;
	}

	@Override
	public int getTankContain()
	{
		return 1;
	}

	@Override
	public Rectangle getSlotPosition(int index)
	{
		if(index == outputMap.size()) return new Rectangle(3, 3, 16, 16);
		return new Rectangle(23 + (index % 3) * 18, 3 + (index / 3) * 18, 16, 16);
	}

	@Override
	public Rectangle getTankPosition(int index)
	{
		return new Rectangle(2, 38, 18, 18);
	}

	@Override
	public ItemStack getShowStackInSlot(int index)
	{
		if(index == outputs.length) return input.toArray().get(0).copy();
		if(outputs[index] == null) return null;
		ItemStack ret = outputs[index].copy();
		return ret;
	}

	@Override
	public ItemAbstractStack getStackInSlot(int index)
	{
		if(index == outputs.length) return input;
		return new ItemBaseStack(outputs[index]);
	}

	@Override
	public FluidStack getFluidInTank(int index)
	{
		return new FluidStack(FluidRegistry.WATER, 1);
	}

	@Override
	public String getTip(int mouseX, int mouseY)
	{
		for(int i = 0; i < outputs.length; ++i)
		{
			if(getSlotPosition(i).contains(mouseX, mouseY))
			{
				return String.format("Chance : %g %%", (float) (outputMap.get(outputs[i]) * 100));
			}
		}
		return null;
	}
}