package fla.core.gui.base;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import org.lwjgl.opengl.GL11;

import fla.api.recipe.ErrorType;
import fla.api.recipe.ItemState;

public abstract class GuiBase extends GuiContainer
{
	public ContainerBase container;
	protected int xoffset;
	protected int yoffset;

	public GuiBase(ContainerBase container)
	{
		this(container, 176, 166);
	}

	public GuiBase(ContainerBase container, int ySize)
	{
		this(container, 176, ySize);
	}

	public GuiBase(ContainerBase container, int xSize, int ySize)
	{
		super(container);
		this.container = container;
		this.ySize = ySize;
		this.xSize = xSize;
	}
	
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String str = I18n.format(getName(), new Object[0]);
		fontRendererObj.drawString(str, (xSize - fontRendererObj.getStringWidth(str)) / 2, 6, 0x404040);
	}

	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(getResourceLocation());
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		drawTexturedModalRect(xoffset, yoffset, 0, 0, xSize, ySize);
	}

	public abstract String getName();

	public abstract ResourceLocation getResourceLocation();
	
	protected void drawError(int x, int y, ErrorType type)
	{
		if(type != null)
		{
			mc.getTextureManager().bindTexture(ErrorType.locate);

			xoffset = (width - xSize) / 2;
			yoffset = (height - ySize) / 2;
			drawTexturedModalRect(xoffset + x, yoffset + y, type.getU(), type.getV(), 16, 16);
			mc.renderEngine.bindTexture(getResourceLocation());
		}
	}

	protected void drawState(int x, int y, ItemState state)
	{
		if(state != null)
		{
			mc.getTextureManager().bindTexture(ItemState.locate);

			xoffset = (width - xSize) / 2;
			yoffset = (height - ySize) / 2;
			drawTexturedModalRect(xoffset + x, yoffset + y, state.getU(), state.getV(), 16, 16);
			mc.renderEngine.bindTexture(getResourceLocation());
		}
	}
	protected void drawStates(int x, int y, ItemState[] state, int width, int height)
	{
		if(state != null)
		{
			mc.getTextureManager().bindTexture(ItemState.locate);

			xoffset = (this.width - xSize) / 2;
			yoffset = (this.height - ySize) / 2;
			for(int i = 0; i < width; ++i)
				for(int j = 0; j < height; ++j)
					drawTexturedModalRect(xoffset + x + i * 17, yoffset + y + j * 17, state[i + j * width].getU(), state[i + j * width].getV(), 16, 16);
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
				drawRepeated(fluidIcon, xoffset + x + width - liquidWidth, yoffset + y + height - liquidHeight, liquidWidth, liquidHeight, zLevel);
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
}