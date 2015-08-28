package fle.cg;

import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.gui.ContainerEmpty;
import fle.api.gui.GuiCondition;

public abstract class GuiBookBase extends GuiContainer
{
	public GuiBookBase()
	{
		super(new ContainerEmpty());
	}

	static final ResourceLocation voidTexture = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/void.png");
	static final ResourceLocation backgroundTexture = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/cg/base.png");

	protected int xoffset;
	protected int yoffset;
	protected int xSize = 200;
	protected int ySize = 213;
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float zLevel,
			int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(getResourceLocation());
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		drawTexturedModalRect(xoffset, yoffset, 0, 0, xSize, ySize);
		drawRecipe(mouseX, mouseY);
	}
	
	public void drawRecipe(int mouseX, int mouseY)
	{
		RecipeHandler[] handlers = getShowingRecipe();
		for(int i = 0; i < handlers.length; ++i)
		{
			int xoffset = this.xoffset + 27 + 80 * (i % 2);
			int yoffset = this.yoffset + 17 + 60 * (i / 2);
			handlers[i].drawRecipeBackground(this, xoffset, yoffset);
		}
		for(int i = 0; i < handlers.length; ++i)
		{
			int xoffset = this.xoffset + 27 + 80 * (i % 2);
			int yoffset = this.yoffset + 17 + 60 * (i / 2);
	        RenderHelper.disableStandardItemLighting();
			for(int slotID = 0; slotID < handlers[i].getSlotContain(); ++slotID)
			{
				ItemStack tStack = handlers[i].getShowStackInSlot(slotID);
				if(tStack != null)
				{
					tStack = tStack.copy();
					Rectangle rect = handlers[i].getSlotPosition(slotID);
					String str;
					if(rect.contains(mouseX - xoffset, mouseY - yoffset))
					{
		                drawRect(xoffset + rect.x, yoffset + rect.y, xoffset + rect.x + rect.width, yoffset + rect.y + rect.height, -2130706433);
					}
					if(tStack.stackSize > 1)
					{
						str = EnumChatFormatting.WHITE.toString() + tStack.stackSize;
						drawItemStack(tStack, xoffset + rect.x, yoffset + rect.y, str);
					}
					else
					{
						drawItemStack(tStack, xoffset + rect.x, yoffset + rect.y, (String) null);
					}
				}
			}
	        RenderHelper.enableGUIStandardItemLighting();
			for(int tankID = 0; tankID < handlers[i].getTankContain(); ++tankID)
			{
				FluidStack tStack = handlers[i].getFluidInTank(tankID);
				if(tStack != null)
				{
					Rectangle rect = handlers[i].getTankPosition(tankID);
					drawFluid(xoffset + rect.x - this.xoffset, yoffset + rect.y - this.yoffset, new FluidTank(tStack, tStack.amount), rect.width, rect.height);
				}
			}
			handlers[i].drawRecipeFortground(this, xoffset, yoffset);
			Rectangle rect = new Rectangle(xoffset, yoffset, 80, 60);
			if(rect.contains(mouseX, mouseY))
			{
				String str = handlers[i].getTip(mouseX - xoffset, mouseY - yoffset);
				if(str != null)
				{
					drawTooltip(mouseX, mouseY, str);
				}
			}
		}
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		RecipeHandler[] recipes = getShowingRecipe();
		for(int i = 0; i < recipes.length; ++i)
		{
			recipes[i].onUpdate(this);
		}
	}
	
    private void drawItemStack(ItemStack aStack, int x, int y, String show)
    {
    	GL11.glTranslatef(0F, 0F, 0F);
        zLevel = 200.0F;
        itemRender.zLevel = 200.0F;
        FontRenderer font = null;
        if (aStack != null) font = aStack.getItem().getFontRenderer(aStack);
        if (font == null) font = fontRendererObj;
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        if(aStack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
        	aStack.setItemDamage(0);
        itemRender.renderItemAndEffectIntoGUI(font, this.mc.getTextureManager(), aStack, x, y);
        itemRender.renderItemOverlayIntoGUI(font, this.mc.getTextureManager(), aStack, x, y, show);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
    }
	
	public abstract RecipeHandler[] getShowingRecipe();
		
	public void bindTexture(ResourceLocation locate)
	{
		mc.renderEngine.bindTexture(locate);
	}
	
	@Override
	public void drawTexturedModalRect(int x, int y,
			int u, int v, int xSize, int ySize)
	{
		super.drawTexturedModalRect(x, y, u, v, xSize, ySize);
	}
	
	protected void drawCondition(int x, int y, GuiCondition type)
	{
		if(type != null)
		{
			mc.getTextureManager().bindTexture(FleAPI.conditionLocate);

			xoffset = (width - xSize) / 2;
			yoffset = (height - ySize) / 2;
			drawTexturedModelRectFromIcon(xoffset + x, yoffset + y, type.getIcon(), 16, 16);
			mc.renderEngine.bindTexture(getResourceLocation());
		}
	}
	protected void drawFluid(int x, int y, IFluidTank tank, int width, int height)
	{
		drawFluid(x, y, tank, width, height, false);
	}
	protected void drawFluid(int x, int y, IFluidTank tank, int width, int height, boolean lay)
	{
		FluidTankInfo info = tank.getInfo();
		xoffset = (this.width - xSize) / 2;
		yoffset = (this.height - ySize) / 2;
		
		if(info.fluid == null) return;
		if (info.fluid.amount > 0)
		{
			IIcon fluidIcon = info.fluid.getFluid().getIcon();
			if (fluidIcon != null)
			{
				mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
				double liquidHeight = lay ? height : (double) (info.fluid.amount * height) / (double)info.capacity;
				double liquidWidth = lay ? (double) (info.fluid.amount * width) / (double) info.capacity : width;
		        int color = info.fluid.getFluid().getColor(info.fluid);
		        float red = (color >> 16 & 255) / 255.0F;
		        float green = (color >> 8 & 255) / 255.0F;
		        float blue = (color & 255) / 255.0F;
				GL11.glColor4f(red, green, blue, 1.0F);
				drawRepeated(fluidIcon, xoffset + x + width - liquidWidth, yoffset + y + height - liquidHeight, liquidWidth, liquidHeight, zLevel);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				mc.renderEngine.bindTexture(getResourceLocation());
			}
		}
	}
	
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

	protected void drawAreaTooltip(int mouseX, int mouseY, String tooltip, int x, int y, int u, int v)
	{
		if (mouseX >= x && mouseX <= (x + u) && mouseY >= y && mouseY <= (y + v))
			drawTooltip(mouseX - xoffset, mouseY - yoffset, tooltip);
	}

	protected void drawTooltip(int x, int y, String tooltip)
	{
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		GL11.glDisable(32826);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(2896);
		GL11.glDisable(2929);
		x += 10;
		y -= 8;
		int width = fontRenderer.getStringWidth(tooltip) + 8;
		int height = 8;
		int backgroundColor = 255;
		int borderColor = 0x1D0051FF;
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glDisable(3008);
		GL11.glBlendFunc(770, 771);
		GL11.glShadeModel(7425);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		drawRectangle(tessellator, x - 3, y - 4, x + width + 3, y - 3, backgroundColor);
		drawRectangle(tessellator, x - 3, y + height + 3, x + width + 3, y + height + 4, backgroundColor);
		drawRectangle(tessellator, x - 3, y - 3, x + width + 3, y + height + 3, backgroundColor);
		drawRectangle(tessellator, x - 4, y - 3, x - 3, y + height + 3, backgroundColor);
		drawRectangle(tessellator, x + width + 3, y - 3, x + width + 4, y + height + 3, backgroundColor);
		drawRectangle(tessellator, x - 3, (y - 3) + 1, (x - 3) + 1, (y + height + 3) - 1, borderColor);
		drawRectangle(tessellator, x + width + 2, (y - 3) + 1, x + width + 3, (y + height + 3) - 1, borderColor);
		drawRectangle(tessellator, x - 3, y - 3, x + width + 3, (y - 3) + 1, borderColor);
		drawRectangle(tessellator, x - 3, y + height + 2, x + width + 3, y + height + 3, borderColor);
		tessellator.draw();
		GL11.glShadeModel(7424);
		GL11.glDisable(3042);
		GL11.glEnable(3008);
		GL11.glEnable(3553);
		fontRenderer.drawStringWithShadow(tooltip, x + 4, y, -2);
		GL11.glEnable(2896);
		GL11.glEnable(2929);
	}

	private void drawRectangle(Tessellator tessellator, int x1, int y1, int x2, int y2, int color)
	{
		tessellator.setColorRGBA(color >>> 24 & 0xff, color >>> 16 & 0xff, color >>> 8 & 0xff, color & 0xff);
		tessellator.addVertex(x2, y1, 300D);
		tessellator.addVertex(x1, y1, 300D);
		tessellator.addVertex(x1, y2, 300D);
		tessellator.addVertex(x2, y2, 300D);
	}
	
	protected void drawFleRect(int x, int y, int u, int v, int color)
	{
		mc.renderEngine.bindTexture(voidTexture);
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(f, f1, f2, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertex((double)x, (double)y + v, 0.0D);
        tessellator.addVertex((double)x + u, (double)y + v, 0.0D);
        tessellator.addVertex((double)x + u, (double)y, 0.0D);
        tessellator.addVertex((double)x, (double)y, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        mc.renderEngine.bindTexture(getResourceLocation());
	}

	protected ResourceLocation getResourceLocation()
	{
		return backgroundTexture;
	}
}