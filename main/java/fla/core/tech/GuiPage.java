package fla.core.tech;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fla.api.tech.IPageGui;
import fla.api.tech.Page;

public class GuiPage extends GuiScreen implements IPageGui
{
	private Page page;
	protected int xSize = 176;
	protected int ySize = 166;
	
	public GuiPage(Page page)
	{
		this.page = page;
	}
	
	@Override
	public void drawScreen(int xMo, int yMo, float f)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(page.getTextureLocation());
		int xoffset = (width - xSize) / 2;
		int yoffset = (height - ySize) / 2;
		drawTexturedModalRect(xoffset, yoffset, 0, 0, xSize, ySize);
		page.drawOther(this);
		mc.getTextureManager().bindTexture(page.getTextureLocation());
		super.drawScreen(xMo, yMo, f);
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

	@Override
	public void drawString(int x, int y, int color, String str) 
	{
		fontRendererObj.drawString(str, x, y, color, false);
	}

	@Override
	public RenderItem getItemRender() 
	{
		return itemRender;
	}	
}