package fle.api.cg;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
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
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.cg.IGuideType.IGuidePage;
import fle.api.cg.IGuideType.IGuidePage.Type;
import fle.api.gui.FontFLERenderer;
import fle.api.gui.GuiCondition;
import fle.api.soild.SolidStack;
import fle.api.soild.SolidTank;
import fle.api.soild.SolidTankInfo;
import fle.api.util.FleLog;

@SideOnly(Side.CLIENT)
public abstract class GuiBookBase extends GuiScreen
{
	private static FontFLERenderer fontRender = new FontFLERenderer();
	
	public GuiBookBase()
	{
		super();
	}

	static final ResourceLocation voidTexture = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/void.png");
	@Deprecated
	static final ResourceLocation backgroundTexture = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/cg/base.png");
	
	protected int xoffset;
	protected int yoffset;
	protected int xSize = 166;
	protected int ySize = 176;
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float zLevel)
	{
		IGuidePage page = getShowingRecipe();
		xoffset = (width - xSize) / 2 - 5;
		yoffset = (height - ySize) / 2 + 5;
		try
		{
			drawRecipeBackground(page, mouseX, mouseY, zLevel);
			drawRecipe(page, mouseX, mouseY, zLevel);
			drawRecipeFrontground(page, mouseX, mouseY, zLevel);
		}
		catch(Throwable e)
		{
			FleLog.getLogger().catching(new RuntimeException("Fle API : Fail to load recipe.", e));
		}
	}

	protected abstract IGuideType getActiveType();
	
	protected abstract String getPageString();

	private Type type;
	private int id;
	private List<String> toolTip;
	private String stackTip;

	private final void drawRecipeBackground(IGuidePage page, int mouseX, int mouseY,
			float zLevel) 
	{
        GL11.glPushMatrix();
	    page.drawBackground(this, xoffset, yoffset);
	    GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	    RenderHelper.disableStandardItemLighting();
	    GL11.glDisable(GL11.GL_LIGHTING);
	    GL11.glDisable(GL11.GL_DEPTH_TEST);
	    super.drawScreen(mouseX, mouseY, zLevel);
	}
	
	private final void drawRecipe(IGuidePage page, int mouseX, int mouseY, float zLevel)
	{		
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glTranslatef((float) xoffset, (float) yoffset, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        
		Rectangle rect;
		String stackTip = null;
		String toolTip = null;
		
		Type selectType = null;
		int selectID = 0;
		//Start draw fluids.
		FluidStack fStack;
		for(int slotID = 0; slotID < page.getSize(Type.FLUID); ++slotID)
		{
			fStack = (FluidStack) page.getObjectForDisplay(Type.FLUID, slotID);
			if(fStack == null) continue;
			rect = page.getRectangle(Type.FLUID, slotID);
			stackTip = page.getStackTip(Type.FLUID, slotID);
			drawFluid(rect.x, rect.y, new FluidTank(fStack, fStack.amount), rect.width, rect.height);
			if(stackTip != null)
			{
				drawInfo(rect, stackTip);
			}
			if(rect.contains(mouseX - xoffset, mouseY - yoffset))
			{
				selectType = Type.FLUID;
				selectID = slotID;
				toolTip = fStack.getLocalizedName() + "x" + fStack.amount;
			}
		}
		//Start draw solids.
		SolidStack sStack;
		for(int slotID = 0; slotID < page.getSize(Type.SOLID); ++slotID)
		{
			sStack = (SolidStack) page.getObjectForDisplay(Type.SOLID, slotID);
			if(sStack == null) continue;
			rect = page.getRectangle(Type.SOLID, slotID);
			stackTip = page.getStackTip(Type.SOLID, slotID);
			drawSolid(rect.x, rect.y, new SolidTank(sStack.getSize(), sStack), rect.width, rect.height);
			if(stackTip != null)
			{
				drawInfo(rect, stackTip);
			}
			if(rect.contains(mouseX - xoffset, mouseY - yoffset))
			{
				selectType = Type.SOLID;
				selectID = slotID;
				toolTip = sStack.getObj().getLocalizedName(sStack) + "x" + sStack.getSize();
			}
		}
		//Start draw items.
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        short short1 = 240;
        short short2 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)short1 / 1.0F, (float)short2 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ItemStack tStack;
		for(int slotID = 0; slotID < page.getSize(Type.ITEM); ++slotID)
		{
			tStack = (ItemStack) page.getObjectForDisplay(Type.ITEM, slotID);
			if(tStack == null) continue;
			rect = page.getRectangle(Type.ITEM, slotID);
			stackTip = page.getStackTip(Type.ITEM, slotID);
			if(stackTip != null)
			{
				drawItemStack(tStack, rect.x, rect.y, stackTip);
			}
			else
			{
				if(tStack.stackSize > 1)
				{
					stackTip = EnumChatFormatting.WHITE.toString() + tStack.stackSize;
					drawItemStack(tStack, rect.x, rect.y, stackTip);
				}
				else
				{
					drawItemStack(tStack, rect.x, rect.y, (String) null);
				}
			}
			if(rect.contains(mouseX - xoffset, mouseY - yoffset))
			{
				selectType = Type.ITEM;
				selectID = slotID;
				toolTip = tStack.getDisplayName();
			}
		}
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		page.drawOther(this, 0, 0);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		if(selectType != null)
		{
			//Draw select rectangle;
			rect = page.getRectangle(selectType, selectID);
			Tessellator t = Tessellator.instance;
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glColorMask(true, true, true, false);
	        drawRect(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, 0x44FFFFFF);
            GL11.glColorMask(true, true, true, true);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        
	        id = selectID;
	        type = selectType;
	        this.stackTip = stackTip;
			ArrayList<String> list = new ArrayList();
			if(toolTip != null)
				list.add(toolTip);
			list.addAll(page.getToolTip(selectType, selectID));
			this.toolTip = list;
		}
		GL11.glPopMatrix();
	}
	
	private final void drawRecipeFrontground(IGuidePage page, int mouseX, int mouseY, float zLevel)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		String name = getActiveType().getTypeName();
		fontRendererObj.drawString(name, xoffset + (xSize + 5 - fontRendererObj.getStringWidth(name)) / 2, yoffset + 6, 0x00444444);
		name = getPageString();
		fontRendererObj.drawString(name, xoffset + (xSize + 5 - fontRendererObj.getStringWidth(name)) / 2, yoffset + ySize - 18 - 6, 0x00444444);
		
		if(type != null)
		{
			Rectangle rect = getShowingRecipe().getRectangle(type, id);
			drawAreaTooltip(mouseX, mouseY, toolTip, xoffset + rect.x, rect.y + yoffset, rect.width, rect.height);
			type = null;
			id = 0;
			toolTip = null;
			stackTip = null;
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
    public void updateScreen()
    {
        super.updateScreen();

        if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead)
        {
            this.mc.thePlayer.closeScreen();
        }
    }
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
    private void drawItemStack(ItemStack aStack, int x, int y, String show)
    {
        if(aStack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
        	aStack.setItemDamage(0);
        FontRenderer font = null;
        if (aStack != null) font = aStack.getItem().getFontRenderer(aStack);
        if (font == null) font = fontRendererObj;
        zLevel = 200F;
        itemRender.zLevel = 200F;
        itemRender.renderItemAndEffectIntoGUI(font, mc.getTextureManager(), aStack, x, y);
        itemRender.renderItemOverlayIntoGUI(font, mc.getTextureManager(), aStack, x, y, show);
        zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
    }

    public void drawInfo(Rectangle rect, String str)
    {
    	drawInfo(rect, str, 0xFFFFFFFF);
    }
    
    public void drawInfo(Rectangle rect, String str, int col)
    {
    	if(str != null)
    	{
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            fontRender.drawStringWithShadow(str, rect.x + rect.width - 2 - fontRender.getStringWidth(str), rect.y + rect.height - 10 + 3, col);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
    	}
    }
    
    public void drawProgressBar(Rectangle rect, double progress)
    {
        int woffset = (int)Math.round(rect.width - progress * rect.width);
        int k = (int)Math.round(255.0D - progress * 255.0D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        Tessellator tessellator = Tessellator.instance;
        int l = 255 - k << 16 | k << 8;
        int i1 = (255 - k) / 4 << 16 | 16128;
        renderQuad(tessellator, rect.x, rect.y, 13, 2, 0);
        renderQuad(tessellator, rect.x, rect.y, 12, 1, i1);
        renderQuad(tessellator, rect.x, rect.y, woffset, 1, l);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    private void renderQuad(Tessellator t, int x, int y, int w, int h, int col)
    {
        t.startDrawingQuads();
        t.setColorOpaque_I(col);
        t.addVertex((double)(x + 0), (double)(y + 0), 0.0D);
        t.addVertex((double)(x + 0), (double)(y + h), 0.0D);
        t.addVertex((double)(x + w), (double)(y + h), 0.0D);
        t.addVertex((double)(x + w), (double)(y + 0), 0.0D);
        t.draw();
    }
	
	public abstract IGuidePage getShowingRecipe();
		
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

	protected void drawCondition(int x, int y, GuiCondition type, boolean offsetHelper)
	{
		if(type != null)
		{
			mc.getTextureManager().bindTexture(FleAPI.conditionLocate);

			if(offsetHelper)
			{
				xoffset = (width - xSize) / 2;
				yoffset = (height - ySize) / 2;
				int pass = type.getRenderPass();
				for(int i = 0; i < pass; ++i)
					drawTexturedModelRectFromIcon(xoffset + x, yoffset + y, type.getIcon(i), 16, 16);
			}
			else
			{
				int pass = type.getRenderPass();
				for(int i = 0; i < pass; ++i)
					drawTexturedModelRectFromIcon(x, y, type.getIcon(i), 16, 16);
			}
		}
	}
	public void drawCondition(int x, int y, GuiCondition type)
	{
		drawCondition(x, y, type, false);
	}
	protected void drawFluid(int x, int y, IFluidTank tank, int width, int height)
	{
		drawFluid(x, y, tank, width, height, false);
	}
	protected void drawFluid(int x, int y, IFluidTank tank, int width, int height, boolean lay)
	{
		FluidTankInfo info = tank.getInfo();
		
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
				drawRepeated(fluidIcon, x + width - liquidWidth, y + height - liquidHeight, liquidWidth, liquidHeight, zLevel);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}
	protected void drawSolid(int x, int y, SolidTank tank, int width, int height)
	{
		drawSolid(x, y, tank, width, height, false);
	}
	protected void drawSolid(int x, int y, SolidTank tank, int width, int height, boolean lay)
	{
		SolidTankInfo info = tank.getInfo();
		
		if(info.solid == null) return;
		if (info.solid.getSize() > 0)
		{
			IIcon solidIcon = info.solid.getObj().getIcon();
			if (solidIcon != null)
			{
				mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
				double liquidHeight = lay ? height : (double) (info.solid.getSize() * height) / (double)info.capacity;
				double liquidWidth = lay ? (double) (info.solid.getSize() * width) / (double) info.capacity : width;
		        int color = info.solid.getObj().getColor(info.solid);
		        float red = (color >> 16 & 255) / 255.0F;
		        float green = (color >> 8 & 255) / 255.0F;
		        float blue = (color & 255) / 255.0F;
				GL11.glColor4f(red, green, blue, 1.0F);
				drawRepeated(solidIcon, x + width - liquidWidth, y + height - liquidHeight, liquidWidth, liquidHeight, zLevel);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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
			drawTooltip(mouseX, mouseY, tooltip);
	}
	protected void drawAreaTooltip(int mouseX, int mouseY, List<String> tooltip, int x, int y, int u, int v)
	{
		if(tooltip == null) return;
		if (mouseX >= x && mouseX <= (x + u) && mouseY >= y && mouseY <= (y + v))
			drawTooltip(mouseX, mouseY, tooltip);
	}
	protected void drawTooltip(int x, int y, String tooltip)
	{
		drawTooltip(x, y, Arrays.asList(tooltip));
	}
	protected void drawTooltip(int x, int y, List<String> tooltip)
	{
        if (!tooltip.isEmpty())
        {
        	FontRenderer font = Minecraft.getMinecraft().fontRenderer;
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = tooltip.iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                int l = font.getStringWidth(s);

                if (l > k)
                {
                    k = l;
                }
            }

            int j2 = x + 12;
            int k2 = y - 12;
            int i1 = 8;

            if (tooltip.size() > 1)
            {
                i1 += 2 + (tooltip.size() - 1) * 10;
            }

            if (j2 + k > this.width)
            {
                j2 -= 28 + k;
            }

            if (k2 + i1 + 6 > this.height)
            {
                k2 = this.height - i1 - 6;
            }

            this.zLevel = 300.0F;
            itemRender.zLevel = 300.0F;
            int j1 = -267386864;
            drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

            for (int i2 = 0; i2 < tooltip.size(); ++i2)
            {
                String s1 = (String)tooltip.get(i2);
                font.drawStringWithShadow(s1, j2, k2, -1);

                if (i2 == 0)
                {
                    k2 += 2;
                }

                k2 += 10;
            }

            zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
	}
	
	@Override
	protected void keyTyped(char chr, int keyCode)
	{
        if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode())
        {
            mc.thePlayer.closeScreen();
        }
	}

	public FontRenderer getFortRender()
	{
		return fontRendererObj;
	}
}