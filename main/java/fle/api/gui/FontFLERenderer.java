package fle.api.gui;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fle.api.FleAPI;
import fle.api.util.FleLog;

public class FontFLERenderer extends FontRenderer
{
	protected static final ResourceLocation locate = new ResourceLocation("minecraft", "textures/font/ascii.png");
	protected static final Map<Character, IIcon> charRegister = new HashMap();

	public static void registerFont(char chr, IIcon icon)
	{
		if(charRegister.containsKey(chr))
		{
			FleLog.getLogger().warn("FLE API: Some mod has already registed font " + chr + ".");
			return;
		}
		charRegister.put(chr, icon);
	}
	
	public FontFLERenderer()
	{
		super(Minecraft.getMinecraft().gameSettings, locate, Minecraft.getMinecraft().getTextureManager(), false);
	}
	
	@Override
	protected float renderUnicodeChar(char chr, boolean flag)
	{
		if(charRegister.containsKey(chr))
		{
			return renderChar(charRegister.get(chr), chr, flag);
		}
		else
		{
			return super.renderUnicodeChar(chr, flag);
		}
	}
	
	protected final float renderChar(IIcon icon, int chr, boolean flag)
	{
		//Need 8.0F because of minecraft font renderer.
		float f1 = 8.0F / (float) icon.getIconHeight();
		float f2 = flag ? 1.0F : 0.0F;
		bindTexture(FleAPI.fontLocate);
		float f3 = ((float) icon.getIconWidth()) * f1 - 0.01F;
		
		float minU = icon.getMinU();
		float minV = icon.getMinV();
		float maxU = icon.getMaxU();
		float maxV = icon.getMaxV();
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(minU, minV);
		GL11.glVertex3f(posX + f2, posY, 0.0F);
		GL11.glTexCoord2f(minU, maxV);
		GL11.glVertex3f(posX - f2, posY + 7.99F, 0.0F);
		GL11.glTexCoord2f(maxU, minV);
		GL11.glVertex3f(posX + f3 - 1.0F + f2, posY, 0.0F);
		GL11.glTexCoord2f(maxU, maxV);
		GL11.glVertex3f(posX + f3 - 1.0F - f2, posY + 7.99F, 0.0F);
		GL11.glEnd();
		return ((float) icon.getIconWidth()) * f1;
	}
}