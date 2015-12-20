package flapi.gui;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import flapi.FleAPI;
import flapi.solid.SolidTank;
import flapi.solid.SolidTankInfo;
import flapi.util.FleValue;

public abstract class GuiContainerBase extends GuiContainer
{
	private static final ResourceLocation voidTexture = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/void.png");
	
	public ContainerBase container;
	protected int xoffset;
	protected int yoffset;

	public GuiContainerBase(ContainerBase container)
	{
		this(container, 176, 166);
	}

	public GuiContainerBase(ContainerBase container, int ySize)
	{
		this(container, 176, ySize);
	}

	public GuiContainerBase(ContainerBase container, int xSize, int ySize)
	{
		super(container);
		this.container = container;
		this.ySize = ySize;
		this.xSize = xSize;
	}
	
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		if(shouldRenderName())
		{
			String str = getName();
			fontRendererObj.drawString(str, (xSize - fontRendererObj.getStringWidth(str)) / 2, 6, 0x404040);
		}
	}

	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(getResourceLocation());
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		drawTexturedModalRect(xoffset, yoffset, 0, 0, xSize, ySize);
		drawUnderFluidAndSolid(xoffset, yoffset);
		GL11.glPushMatrix();
		for(FluidSlot slot : container.fluidSlotList)
		{
			slot.renderFluidInSlot(this);
		}
		for(SolidSlot slot : container.solidSlotList)
		{
			slot.renderSolidInSlot(this);
		}
		GL11.glPopMatrix();
		drawOther(xoffset, yoffset, x, y);
	}
	
	protected void drawUnderFluidAndSolid(int xO, int yO)
	{
		
	}
	
	protected abstract void drawOther(int aXOffset, int aYOffset, int aMouseXPosition, int aMouseYPosition);

	public boolean hasCustomName()
	{
		return container instanceof ContainerBase ? ((ContainerBase) container).inv.hasCustomInventoryName() : false;
	}
	
	public String getName()
	{
		return container instanceof ContainerBase ? ((ContainerBase) container).inv.getInventoryName() : "";
	}
	
	protected boolean shouldRenderName()
	{
		return true;
	}

	public abstract ResourceLocation getResourceLocation();
	
	protected void drawCondition(int x, int y, GuiCondition type)
	{
		if(type != null)
		{
			mc.getTextureManager().bindTexture(FleValue.conditionLocate);

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			xoffset = (width - xSize) / 2;
			yoffset = (height - ySize) / 2;
			int pass = type.getRenderPass();
			for(int i = 0; i < pass; ++i)
				drawTexturedModelRectFromIcon(xoffset + x, yoffset + y, type.getIcon(i), 16, 16);
			mc.renderEngine.bindTexture(getResourceLocation());
		}
	}
	public void drawFluid(int x, int y, IFluidTank tank, int width, int height)
	{
		drawFluid(x, y, tank, width, height, false);
	}
	public void drawFluid(int x, int y, IFluidTank tank, int width, int height, boolean lay)
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
		        int color = info.fluid.getFluid().getColor(info.fluid);
		        float red = (color >> 16 & 255) / 255.0F;
		        float green = (color >> 8 & 255) / 255.0F;
		        float blue = (color & 255) / 255.0F;
				GL11.glColor4f(red, green, blue, 1.0F);
				if(lay)
					drawRepeated(fluidIcon, xoffset + x, yoffset + y, (double) (info.fluid.amount * width) / (double)info.capacity, height, zLevel);
				else
					drawRepeated(fluidIcon, xoffset + x, yoffset + y + height - (double) (info.fluid.amount * height) / (double) info.capacity, width, (double) (info.fluid.amount * height) / (double) info.capacity, zLevel);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				mc.renderEngine.bindTexture(getResourceLocation());
			}
		}
	}
	public void drawSolid(int x, int y, SolidTank tank, int width, int height)
	{
		drawSolid(x, y, tank, width, height, false);
	}
	public void drawSolid(int x, int y, SolidTank tank, int width, int height, boolean lay)
	{
		SolidTankInfo info = tank.getInfo();
		xoffset = (this.width - xSize) / 2;
		yoffset = (this.height - ySize) / 2;
		
		if(info.solid == null) return;
		if (info.solid.size() > 0)
		{
			IIcon solidIcon = info.solid.get().getIcon();
			if (solidIcon != null)
			{
				mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
				double liquidHeight = lay ? height : (double) (info.solid.size() * height) / (double)info.capacity;
				double liquidWidth = lay ? (double) (info.solid.size() * width) / (double) info.capacity : width;
		        int color = info.solid.get().getColor(info.solid);
		        float red = (color >> 16 & 255) / 255.0F;
		        float green = (color >> 8 & 255) / 255.0F;
		        float blue = (color & 255) / 255.0F;
				GL11.glColor4f(red, green, blue, 1.0F);
				drawRepeated(solidIcon, xoffset + x + width - liquidWidth, yoffset + y + height - liquidHeight, liquidWidth, liquidHeight, zLevel);
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
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
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
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	protected void sendToContainer(int type, int contain)
	{
		FleAPI.mod.getNetworkHandler().sendToServer(FleAPI.mod.getNetworkHandler().getPacketMaker().makeGuiPacket((byte) type, contain));
	}
}