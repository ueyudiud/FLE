package fla.core.gui.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiIconButton extends GuiButton
{
	private ResourceLocation texture;
	private int textureX;
	private int textureY;
	private ItemStack itemStack;
	private boolean drawQuantity;
	private RenderItem renderItem;

	public GuiIconButton(int id1, int x, int y, int w, int h, int u, int v)
	{
		super(id1, x, y, w, h, "");
		itemStack = null;
		textureX = u;
		textureY = v;
	}
	public GuiIconButton(int id1, int x, int y, int w, int h, ResourceLocation texture1, int textureX1, 
			int textureY1)
	{
		super(id1, x, y, w, h, "");
		itemStack = null;
		texture = texture1;
		textureX = textureX1;
		textureY = textureY1;
	}
	public GuiIconButton(int id1, int x, int y, int w, int h, ItemStack icon, boolean drawQuantity1)
	{
		super(id1, x, y, w, h, "");
		itemStack = null;
		itemStack = icon;
		drawQuantity = drawQuantity1;
	}

	public void drawButton(Minecraft minecraft, int i, int j)
	{
		super.drawButton(minecraft, i, j);
		if (itemStack == null)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			if(texture != null)
			{
				minecraft.getTextureManager().bindTexture(texture);
			}
			drawTexturedModalRect(xPosition + 1, yPosition + 1, textureX, textureY, width - 2, height - 2);
		} 
		else
		{
			if (renderItem == null)
				renderItem = new RenderItem();
			renderItem.renderItemIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, itemStack, xPosition + 2, yPosition + 1);
			if (drawQuantity)
				renderItem.renderItemOverlayIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, itemStack, xPosition + 2, xPosition + 1);
		}
	}
}
