package fle.api.cg;

import java.util.List;

import farcore.lib.stack.AbstractStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IRecipeDisplayHelper
{
	void bindTexture(ResourceLocation location);
	
	void drawItemStack(int x, int y, AbstractStack stack);
	
	void drawItemStack(int x, int y, AbstractStack stack, boolean randomDisplayStack);
	
	void drawItemStack(int x, int y, ItemStack stack);
	
	void drawFluidStack(int x, int y, int w, int h, Fluid stack);
	
	void drawFluidStack(int x, int y, int w, int h, float scale, FluidStack stack);
	
	void drawFluidStack(int x, int y, IFluidTank stack);
	
	void drawFluidStack(int x, int y, ItemStack stack);

	void drawTooltip(int x, int y, List<String> tooltips);

	void drawTooltip(FontRenderer renderer, int x, int y, List<String> tooltips);
	
	void drawString(int x, int y, String text);

	void drawString(int x, int y, String text, int rgba);

	void drawString(FontRenderer renderer, int x, int y, String text, int rgba);
	
	void drawTextureRect(int x, int y, int u, int v, int w, int h);

	void drawColorRect(int x, int y, int u, int v, int rgba);
	
	void drawLine(int x1, int y1, int x2, int y2, int rgba);
}