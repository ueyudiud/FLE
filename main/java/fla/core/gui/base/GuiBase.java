package fla.core.gui.base;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

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
		}
	}
}
