package fla.core.tech;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fla.api.tech.IPageGui;
import fla.api.tech.Page;

public class GuiPage extends GuiContainer implements IPageGui
{
	private Page page;
	
	public GuiPage(Page page)
	{
		super(new ContainerPage(page));
		this.page = page;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float a,
			int xMo, int yMo)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(page.getTextureLocation());
		int xoffset = (width - xSize) / 2;
		int yoffset = (height - ySize) / 2;
		drawTexturedModalRect(xoffset, yoffset, 0, 0, xSize, ySize);
		page.drawOther(this);
	}

	@Override
	public void drawTexturedRect(int x, int y, int u, int v, int xSize,
			int ySize) 
	{
		int xoffset = (width - xSize) / 2;
		int yoffset = (height - ySize) / 2;
		drawTexturedModalRect(xoffset + x, yoffset + y, u, v, xSize, ySize);
	}

	@Override
	public void bindTexture(ResourceLocation locate) 
	{
		mc.getTextureManager().bindTexture(locate);
	}	
}
