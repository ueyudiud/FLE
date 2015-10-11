package fle.api.cg;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import fle.api.cg.IGuideType.IGuidePage;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.soild.SolidStack;

public abstract class StandardPage extends IGuidePage
{
	protected int xSize = 176;
	protected int ySize = 166;
	int tick;

	protected <T> T getSelet(T[] ts)
	{
		return getSelect(64, ts);
	}
	protected <T> T getSelect(int updateTick, T[] ts)
	{
		return ts == null || ts.length == 0 ? null : ts[getSelect(updateTick, ts.length)];
	}
	
	protected int getSelect(int updateTick, int length)
	{
		return (tick / updateTick) % length;
	}

	protected abstract ItemAbstractStack[] getInputStacks();

	protected abstract ItemStack[] getOutputStacks();

	protected abstract ItemStack[][] getInputStacksForDisplay();

	protected ItemStack[] getOutputStacksForDisplay()
	{
		return getOutputStacks();
	}

	protected FluidStack[] getInputFluidStacks()
	{
		return new FluidStack[0];
	}

	protected FluidStack[] getOutputFluidStacks()
	{
		return new FluidStack[0];
	}

	protected SolidStack[] getInputSolidStacks()
	{
		return new SolidStack[0];
	}

	protected SolidStack[] getOutputSolidStacks()
	{
		return new SolidStack[0];
	}

	@Override
	public int getSize(Type aType)
	{
		switch(aType)
		{
		case ITEM : return getInputStacks().length + getOutputStacks().length;
		case FLUID: return getInputFluidStacks().length + getOutputFluidStacks().length;
		case SOLID: return getInputSolidStacks().length + getOutputSolidStacks().length;
		default: return 0;
		}
	}

	@Override
	public Object getObject(Type aType, int index)
	{
		switch(aType)
		{
		case ITEM : return index < getInputStacks().length ? getInputStacks()[index] : new ItemBaseStack(getOutputStacks()[index - getInputStacks().length]);
		case FLUID: return index < getInputFluidStacks().length ? getInputFluidStacks()[index] : getOutputFluidStacks()[index - getInputFluidStacks().length];
		case SOLID: return index < getInputSolidStacks().length ? getInputSolidStacks()[index] : getOutputSolidStacks()[index - getInputSolidStacks().length];
		default: return null;
		}
	}

	@Override
	public Object getObjectForDisplay(Type aType, int index)
	{
		Object obj;
		switch(aType)
		{
		case ITEM : obj = index < getInputStacksForDisplay().length ? getSelet(getInputStacksForDisplay()[index]) : getOutputStacksForDisplay()[index - getInputStacks().length];
		break;
		case FLUID: obj = index < getInputStacks().length ? getInputFluidStacks()[index] : getOutputFluidStacks()[index - getInputStacks().length];
		break;
		case SOLID: obj = index < getInputSolidStacks().length ? getInputSolidStacks()[index] : getOutputSolidStacks()[index - getInputSolidStacks().length];
		break;
		default: obj = null;
		}
		return obj instanceof ItemStack ? ((ItemStack) obj).copy() : obj instanceof FluidStack ?
				((FluidStack) obj).copy() : obj instanceof SolidStack ? ((SolidStack) obj).copy() :
					obj;
	}

	@Override
	public String getStackTip(Type aType, int index)
	{
		return null;
	}

	@Override
	public List<String> getToolTip(Type aType, int index) 
	{
		return new ArrayList();
	}

	@Override
	public void onUpdate(GuiBookBase guiBookBase)
	{
		++tick;
	}

	@Override
	public void drawBackground(GuiBookBase gui, int xOffset, int yOffset)
	{
		gui.bindTexture(getLocation());
		gui.drawTexturedModalRect(xOffset, yOffset, 0, 0, xSize, ySize);
	}

	protected abstract ResourceLocation getLocation();
	
	@Override
	public void drawOther(GuiBookBase gui, int xOffset, int yOffset)
	{
		
	}
	
	protected void drawToolTip(FontRenderer fr, List<String> list)
	{
		drawToolTip(fr, list, 10, 84);
	}
	
	protected void drawToolTip(FontRenderer fr, List<String> list, int x, int y)
	{
		if(list == null) return;
		int yOffset = y;
		for(String str : list)
		{
			fr.drawString(str, x, yOffset, 0xFF000000);
			yOffset += 10;
		}
	}
}