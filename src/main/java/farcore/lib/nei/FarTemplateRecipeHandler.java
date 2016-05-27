package farcore.lib.nei;

import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public abstract class FarTemplateRecipeHandler extends TemplateRecipeHandler
{
	public static int sOffsetY = 11;
	public static int sOffsetX = 5;

	public FarTemplateRecipeHandler()
	{
		this(true);
	}
	public FarTemplateRecipeHandler(boolean registerForInput)
	{
		if(registerForInput)
		{
			API.registerRecipeHandler(this);
		}
		API.registerUsageHandler(this);
	}
	
	public class BaseCachedRecipe extends CachedRecipe
	{
		public List<PositionedStack> products = new ArrayList();
		public List<PositionedStack> resources = new ArrayList();
		
		public BaseCachedRecipe()
		{
			super();
		}
		
		public List<PositionedStack> getIngredients()
		{
			return getCycledIngredients(FarTemplateRecipeHandler.this.cycleticks / 20, this.resources);
		}
		
		@Override
		public List<PositionedStack> getOtherStacks()
		{
			return products;
		}
		
		@Override
		public PositionedStack getResult()
		{
			return null;
		}
		
		public boolean matchCrafting(ItemStack output)
		{
			for(PositionedStack stack : products)
			{
				if(stack.contains(output))
				{
					return true;
				}
			}
			return false;
		}
		
		public boolean matchUsage(ItemStack input)
		{
			for(PositionedStack stack : resources)
			{
				if(stack.contains(input))
				{
					return true;
				}
			}
			return false;
		}
	}
	
	private ImmutableList<BaseCachedRecipe> recipes;

	protected abstract String getRecipeId();
	
	protected abstract void initRecipeList(List<BaseCachedRecipe> list);
	
	protected final List<BaseCachedRecipe> getRecipeList()
	{
		if(recipes == null)
		{
			List<BaseCachedRecipe> recipes = new ArrayList();
			initRecipeList(recipes);
			this.recipes = ImmutableList.copyOf(recipes);
		}
		return recipes;
	}
	
	public void drawBackground(int recipe)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    GuiDraw.changeTexture(getGuiTexture());
	    GuiDraw.drawTexturedModalRect(0, 0, sOffsetX, sOffsetY, 168, 79);
	}
	
	public static void drawText(int x, int y, String str, int color)
	{
		Minecraft.getMinecraft().fontRenderer.drawString(str, x, y, color);
	}
	
//	public void drawFluid(int x, int y, FluidTankInfo tank, int width, int height)
//	{
//		drawFluid(x, y, tank, width, height, false);
//	}
//	public void drawFluid(int x, int y, FluidTankInfo info, int width, int height, boolean lay)
//	{
//		if(info.fluid == null) return;
//		if (info.fluid.amount > 0)
//		{
//			IIcon fluidIcon = info.fluid.getFluid().getIcon();
//			if (fluidIcon != null)
//			{
//				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
//		        int color = info.fluid.getFluid().getColor(info.fluid);
//		        float red = (color >> 16 & 255) / 255.0F;
//		        float green = (color >> 8 & 255) / 255.0F;
//		        float blue = (color & 255) / 255.0F;
//				GL11.glColor4f(red, green, blue, 1.0F);
//				GL11.glEnable(GL11.GL_ALPHA);
//				double amount = MathHelper.clamp_float((float) info.fluid.amount / (float) info.capacity, 0F, 1F);
//				if(lay)
//				{
//					drawRepeated(fluidIcon, x, y, width * amount, height, 0);
//				}
//				else
//				{
//					drawRepeated(fluidIcon, x, y + height * (1 - amount), width, height * amount, 0);
//				}
//				GL11.glDisable(GL11.GL_ALPHA);
//				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//				Minecraft.getMinecraft().renderEngine.bindTexture(
//						new ResourceLocation(getGuiTexture()));
//			}
//		}
//	}
	
	protected void drawRepeated(IIcon icon, double x, double y, double width, double height, double z)
	{
		double iconWidthStep = ((double)icon.getMaxU() - (double)icon.getMinU()) / 16D;
		double iconHeightStep = ((double)icon.getMaxV() - (double)icon.getMinV()) / 16D;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		for (double cy = y; cy < y + height; cy += 16D)
		{
			double quadHeight = Math.min(16D, (height + y) - cy);
			double maxY = cy + quadHeight;
			double maxV = (double)icon.getMinV() + iconHeightStep * quadHeight;
			for (double cx = x; cx < x + width; cx += 16D)
			{
				double quadWidth = Math.min(16D, (width + x) - cx);
				double maxX = cx + quadWidth;
				double maxU = (double)icon.getMinU() + iconWidthStep * quadWidth;
				tessellator.addVertexWithUV(cx, maxY, z, icon.getMinU(), maxV);
				tessellator.addVertexWithUV(maxX, maxY, z, maxU, maxV);
				tessellator.addVertexWithUV(maxX, cy, z, maxU, icon.getMinV());
				tessellator.addVertexWithUV(cx, cy, z, icon.getMinU(), icon.getMinV());
			}
		}
		tessellator.draw();
	}
	
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if (outputId.equals(getRecipeId()))
		{
			this.arecipes.addAll(getRecipeList());
		}
		else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}

	public void loadCraftingRecipes(ItemStack result)
	{
		for (BaseCachedRecipe recipe : getRecipeList())
		{
			if (recipe.matchCrafting(result))
			{
				this.arecipes.add(recipe);
			}
		}
	}
	
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for (BaseCachedRecipe recipe : getRecipeList())
		{
			if (recipe.matchUsage(ingredient))
			{
				this.arecipes.add(recipe);
			}
		}
	}
	
	@Override
	public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe)
	{
		List<String> ret = super.handleItemTooltip(gui, stack, currenttip, recipe);
//		if(stack != null)
//		{
//			ItemInformation.addInformation(stack, ret);
//		}
		return ret;
	}
	
	void registerTransferRect(int x, int y, int w, int h, Class<? extends GuiContainer>...clazzs)
	{
		RecipeTransferRectHandler.registerRectsToGuis(ImmutableList.copyOf(clazzs), ImmutableList.of(new RecipeTransferRect(new Rectangle(x, y, w, h), getRecipeId(), new Object[0])));
	}

	private static final DecimalFormat formatP1 = new DecimalFormat("##############0.00");
	
	protected static String formatEnergy(long energy)
	{
		if(energy > 2000000000000L)
		{
			return formatP1.format((double) energy / 1E12D) + "TJ";
		}
		if(energy > 2000000000L)
		{
			return formatP1.format((double) energy / 1E9D) + "GJ";
		}
		if(energy > 2000000L)
		{
			return formatP1.format((double) energy / 1E6D) + "MJ";
		}
		if(energy > 2000L)
		{
			return formatP1.format((double) energy / 1E3D) + "kJ";
		}
		return energy + "J";
	}

	protected static String formatPower(long speed)
	{
		return formatPower(speed, true);
	}
	
	protected static String formatPower(long speed, boolean isTicked)
	{
		if(isTicked)
			speed = speed * 20;
		if(speed > 2000000000000L)
		{
			return formatP1.format((double) speed / 1E12D) + "TW";
		}
		if(speed > 2000000000L)
		{
			return formatP1.format((double) speed / 1E9D) + "GW";
		}
		if(speed > 2000000L)
		{
			return formatP1.format((double) speed / 1E6D) + "MW";
		}
		if(speed > 2000L)
		{
			return formatP1.format((double) speed / 1E3D) + "kW";
		}
		return speed + "W";
	}
}