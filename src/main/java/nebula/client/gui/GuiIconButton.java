/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.gui;

import org.lwjgl.opengl.GL11;

import nebula.Nebula;
import nebula.client.ClientOverride;
import nebula.client.util.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author ueyudiud
 */
public class GuiIconButton extends GuiButton
{
	private static final ResourceLocation VOID = new ResourceLocation(Nebula.MODID, "textures/gui/void.png");
	private static final ResourceLocation BACKGROUND = new ResourceLocation(Nebula.MODID, "textures/gui/button_background.png");
	private ButtonSize size;
	private ResourceLocation texture;
	private int textureX;
	private int textureY;
	private ItemStack itemStack;
	private boolean drawQuantity;
	private RenderItem renderItem;
	
	public static enum ButtonSize
	{
		Standard(18),  Slot(16),  Small(10), Tiny(7);
		
		final int size;
		
		private ButtonSize(int aSize)
		{
			this.size = aSize;
		}
	}
	
	private boolean emptyDraw = false;
	
	public GuiIconButton(int id1, int x, int y, ButtonSize aSize, RenderItem render)
	{
		super(id1, x, y, aSize.size, aSize.size, "");
		this.emptyDraw = true;
		this.renderItem = render;
	}
	
	public GuiIconButton(int id1, int x, int y, ButtonSize aSize, int u, int v, RenderItem render)
	{
		super(id1, x, y, aSize.size, aSize.size, "");
		this.renderItem = render;
		this.itemStack = null;
		this.size = aSize;
		this.textureX = u;
		this.textureY = v;
	}
	
	public GuiIconButton(int id1, int x, int y, ButtonSize aSize, ResourceLocation texture1, int textureX1, int textureY1, RenderItem render)
	{
		super(id1, x, y, aSize.size, aSize.size, "");
		this.renderItem = render;
		this.itemStack = null;
		this.size = aSize;
		this.texture = texture1;
		this.textureX = textureX1;
		this.textureY = textureY1;
	}
	
	public GuiIconButton(int id1, int x, int y, ButtonSize aSize, ItemStack icon, boolean drawQuantity1, RenderItem render)
	{
		super(id1, x, y, aSize.size, aSize.size, "");
		this.renderItem = render;
		this.itemStack = icon;
		this.size = aSize;
		this.drawQuantity = drawQuantity1;
	}
	
	public GuiIconButton setVisible(boolean iv)
	{
		this.visible = iv;
		return this;
	}
	
	@Override
	public void drawButton(Minecraft minecraft, int i, int j)
	{
		if (this.visible)
		{
			this.hovered = ((i >= this.xPosition) && (j >= this.yPosition) && (i < this.xPosition + this.width) && (j < this.yPosition + this.height));
			int k = getHoverState(this.hovered);
			minecraft.getTextureManager().bindTexture(BACKGROUND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(3042);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(770, 771);
			if (this.emptyDraw)
			{
				if (k != 2)
				{
					return;
				}
				minecraft.getTextureManager().bindTexture(VOID);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
				drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0, this.width, this.height);
				return;
			}
			//Draw background.
			switch (this.size)
			{
			case Standard:
				drawTexturedModalRect(this.xPosition, this.yPosition, 0, k * this.size.size, this.width, this.height);
				break;
			case Small:
				drawTexturedModalRect(this.xPosition, this.yPosition, 18, k * this.size.size, this.width, this.height);
				break;
			case Tiny :
				drawTexturedModalRect(this.xPosition, this.yPosition, 28, k * this.size.size, this.width, this.height);
				break;
			default:;
			}
			mouseDragged(minecraft, i, j);
			//Draw item stack.
			if (this.itemStack == null)
			{
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				if (this.texture != null)
				{
					minecraft.getTextureManager().bindTexture(this.texture);
				}
				drawTexturedModalRect(this.xPosition + 1, this.yPosition + 1, this.textureX, this.textureY, this.width - 2, this.height - 2);
			}
			else
			{
				this.renderItem.renderItemIntoGUI(this.itemStack, this.xPosition + 2, this.yPosition + 1);
				if (this.drawQuantity)
				{
					ClientOverride.renderCustomItemOverlayIntoGUI(this.renderItem, Client.getFontRender(), this.itemStack, this.xPosition + 2, this.yPosition + 1, null);
				}
			}
		}
	}
}