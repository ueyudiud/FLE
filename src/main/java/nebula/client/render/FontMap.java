package nebula.client.render;

import java.awt.image.BufferedImage;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FontMap implements IFontMap
{
	private String fontMap;
	private ResourceLocation location;
	private int readSize;
	private int[] characterWidth;
	
	public FontMap(ResourceLocation location, String fontMap)
	{
		this(location, fontMap, 16);
	}
	public FontMap(ResourceLocation location, String fontMap, int readSize)
	{
		this.location = location;
		this.fontMap = fontMap;
		this.readSize = readSize;
	}

	@Override
	public ResourceLocation getSource()
	{
		return location;
	}

	@Override
	public boolean shouldRender(char chr)
	{
		return fontMap.indexOf(chr) != -1;
	}

	@Override
	public void initalizeResource(BufferedImage bufferedimage)
	{
		characterWidth = new int[readSize * readSize];
		int w = bufferedimage.getWidth();
		int h = bufferedimage.getHeight();
		int[] RGB = new int[w * h];
		bufferedimage.getRGB(0, 0, w, h, RGB, 0, w);
		int w1 = w / readSize;
		int h1 = h / readSize;
		float width = 8.0F / w1;
		
		for (int i = 0; i < readSize * readSize; ++i)
		{
			int j1 = i % readSize;
			int k1 = i / readSize;
			
			if (i == 32)
			{
				characterWidth[i] = 4;
			}
			
			int l1;
			
			for (l1 = w1 - 1; l1 >= 0; --l1)
			{
				int i2 = j1 * w1 + l1;
				boolean flag1 = true;
				for (int j2 = 0; j2 < h1 && flag1; ++j2)
				{
					int k2 = (k1 * w1 + j2) * w;
					if ((RGB[i2 + k2] >> 24 & 255) != 0)
					{
						flag1 = false;
					}
				}
				if (!flag1)
				{
					break;
				}
			}
			++l1;
			characterWidth[i] = (int)(0.5D + l1 * width) + 1;
		}
	}

	@Override
	public int characterWidth(char chr)
	{
		return characterWidth[fontMap.indexOf(chr)];
	}

	@Override
	public int renderCharacter(char chr, boolean italic, FontRenderExtend render)
	{
		int i0 = fontMap.indexOf(chr);
		if(i0 == -1) return 0;
		int i = (i0 % 16) * 8;
		int j = (i0 / 16) * 8;
		int k = italic ? 1 : 0;
		render.bindTexture(location);
		int l = characterWidth[i0];
		float f = l - 0.01F;
		float[] pos = render.getPosition();
		GlStateManager.glBegin(5);
		GlStateManager.glTexCoord2f(i / 128.0F, j / 128.0F);
		GlStateManager.glVertex3f(pos[0] + k, pos[1], 0.0F);
		GlStateManager.glTexCoord2f(i / 128.0F, (j + 7.99F) / 128.0F);
		GlStateManager.glVertex3f(pos[0] - k, pos[1] + 7.99F, 0.0F);
		GlStateManager.glTexCoord2f((i + f - 1.0F) / 128.0F, j / 128.0F);
		GlStateManager.glVertex3f(pos[0] + f - 1.0F + k, pos[1], 0.0F);
		GlStateManager.glTexCoord2f((i + f - 1.0F) / 128.0F, (j + 7.99F) / 128.0F);
		GlStateManager.glVertex3f(pos[0] + f - 1.0F - k, pos[1] + 7.99F, 0.0F);
		GlStateManager.glEnd();
		return l;
	}
}