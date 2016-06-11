package farcore.lib.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiIconButton extends GuiButton
{
	private static final ResourceLocation backgroundLocation = new ResourceLocation("fle", "textures/gui/button_background.png");
	
	private ResourceLocation location;
	protected IIcon icon;
	protected boolean drawBackground;

	public GuiIconButton(int id, int x, int z, int s)
	{
		this(id, x, z, s, false, null, null);
	}
	public GuiIconButton(int id, int x, int z, int s, IIcon icon)
	{
		this(id, x, z, s, true, FarCore.bottonTextureMap.location, icon);
	}
	public GuiIconButton(int id, int x, int z, int s, boolean drawBackground, ResourceLocation location, IIcon icon)
	{
		super(id, x, z, s, s, "");
		this.location = location;
		this.drawBackground = drawBackground;
		this.icon = icon;
	}
	
	public GuiIconButton setVisible(boolean visible)
	{
		this.visible = visible;
		return this;
	}
	
	@Override
	public void drawButton(Minecraft minecraft, int i, int j)
	{
		if(visible)
		{
			this.field_146123_n = ((i >= this.xPosition) && (j >= this.yPosition) && (i < this.xPosition + this.width) && (j < this.yPosition + this.height));
			int k = getHoverState(this.field_146123_n);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			if(drawBackground)
			{
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				minecraft.renderEngine.bindTexture(backgroundLocation);
				int w;
				if(width == 18)
				{
					w = 0;
				}
				else if(width == 10)
				{
					w = 18;
				}
				else
				{
					w = 26;
				}
				drawTexturedModalRect(xPosition, yPosition, w, k * width, width, width);
				if(icon != null)
				{
					minecraft.renderEngine.bindTexture(location);
					drawTexturedModelRectFromIcon(xPosition + 1, yPosition + 1, icon, width - 2, width - 2);
				}
			}
			else
			{
				if(icon != null)
				{
					minecraft.renderEngine.bindTexture(location);
					drawTexturedModelRectFromIcon(xPosition + 1, yPosition + 1, icon, width - 2, width - 2);
				}
				if(k == 2)
				{
					drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0x44FFFFFF);
				}
			}
		}
	}
}